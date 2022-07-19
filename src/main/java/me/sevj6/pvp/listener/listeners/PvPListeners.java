package me.sevj6.pvp.listener.listeners;

import lombok.SneakyThrows;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.ItemUtil;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.util.Vector;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PvPListeners implements Listener {

    // Exploding Arrows in terrain arena
    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (event.isCancelled()) return;
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
            Entity entity = event.getEntity();
            Player player = (Player) event.getEntity().getShooter();
            if (!Utils.isPlayerInArena(player)) return;
            if (!player.getWorld().getName().equalsIgnoreCase("terrain")) return;
            EntityArrow arrow = (EntityArrow) ((CraftEntity) entity).getHandle();
            arrow.die();
            Vector vector = entity.getVelocity();
            World world = ((CraftWorld) entity.getWorld()).getHandle();
            EntityTNTPrimed tnt = new EntityTNTPrimed(world, entity.getLocation().getX(), entity.getLocation().getY() - 1, entity.getLocation().getZ(), ((CraftPlayer) player).getHandle());
            tnt.setFuseTicks(15);
            setVelocity(tnt, vector);
            world.addEntity(tnt);
        }
    }

    private void setVelocity(net.minecraft.server.v1_12_R1.Entity entity, Vector velocity) {
        entity.motX = velocity.getX();
        entity.motY = velocity.getY();
        entity.motZ = velocity.getZ();
    }

    @SneakyThrows
    private void explodeTNT(EntityTNTPrimed entityTNTPrimed) {
        Method explodeM = EntityTNTPrimed.class.getDeclaredMethod("explode");
        explodeM.setAccessible(true);
        explodeM.invoke(entityTNTPrimed);
        entityTNTPrimed.die();
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Arrays.stream(event.getInventory().getContents()).forEach(ItemUtil::revertEnchants);
        Arrays.stream(event.getPlayer().getInventory().getContents()).forEach(ItemUtil::revertEnchants);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Arrays.stream(event.getInventory().getContents()).forEach(ItemUtil::revertEnchants);
        Arrays.stream(event.getPlayer().getInventory().getContents()).forEach(ItemUtil::revertEnchants);
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent event) {
        if (ItemUtil.is32k(event.getItem().getItemStack())) {
            event.setCancelled(true);
            event.getItem().remove();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (ItemUtil.is32k(event.getItemDrop().getItemStack())) {
            event.getItemDrop().getItemStack().setAmount(-1);
        }
    }

    @EventHandler
    public void onBucketEmpty(PlayerBucketEmptyEvent event) {
        if (Utils.isPlayerInArena(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onBucketFill(PlayerBucketFillEvent event) {
        if (Utils.isPlayerInArena(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        Location location = event.getBlock().getLocation();
        BlockPosition pistonPos = new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ());
        Location affectedBlockLocation = event.getBlock().getRelative(event.getDirection()).getLocation();
        BlockPosition affectedPos = new BlockPosition(affectedBlockLocation.getBlockX(), affectedBlockLocation.getBlockY(), affectedBlockLocation.getBlockZ());
        boolean pushLogic = (Utils.isPositionInArena(pistonPos) && !Utils.isPositionInArena(affectedPos))
                || (!Utils.isPositionInArena(pistonPos) && Utils.isPositionInArena(affectedPos))
                || (!Utils.isPositionInArena(pistonPos) && !Utils.isPositionInArena(affectedPos));
        event.setCancelled(pushLogic);
    }

    @SneakyThrows
    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (!Utils.isPlayerInArena(event.getPlayer()) && !event.getPlayer().isOp()) {
            event.setCancelled(true);
            DisableActivity.sendBlockChangePacket(event.getPlayer(), event.getBlockPlaced().getLocation(), event.getBlock().getLocation(), event.getBlockAgainst().getLocation());
            return;
        }
        if (event.getBlockPlaced() == null) return;
        Block block = event.getBlockPlaced();
        if (block.getType() == Material.REDSTONE_WIRE) {
            event.setCancelled(true);
            return;
        }
        if (block.getType().equals(Material.SPONGE)) {
            if (!isBlockInWater(block)) return;
            World world = ((CraftWorld) event.getBlockPlaced().getWorld()).getHandle();
            Location location = block.getLocation();
            IBlockData blockData = world.getType(new BlockPosition(location.getBlockX(), location.getBlockY(), location.getBlockZ()));
            BlockSponge sponge = (BlockSponge) blockData.getBlock();
            Method absorbM = BlockSponge.class.getDeclaredMethod("b", World.class, BlockPosition.class);
            absorbM.setAccessible(true);
            int range = 5;
            for (int x = location.getBlockX() - range; x <= location.getBlockX() + range; x++) {
                for (int y = location.getBlockY() - range; y <= location.getBlockY() + range; y++) {
                    for (int z = location.getBlockZ() - range; z <= location.getBlockZ() + range; z++) {
                        BlockPosition pos = new BlockPosition(x, y, z);
                        absorbM.invoke(sponge, world, pos);
                    }
                }
            }
            return;
        }
        if (block.getType().equals(org.bukkit.Material.BEDROCK)) {
            event.setCancelled(true);
            return;
        }
        if (!(block.getState() instanceof ShulkerBox)) return;
        ItemUtil.revertShulker(block);
    }

    private boolean isBlockInWater(Block block) {
        for (BlockFace value : BlockFace.values()) {
            if (block.getRelative(value).getType().equals(Material.WATER) || block.getRelative(value).getType().equals(Material.STATIONARY_WATER))
                return true;
        }
        return false;
    }

    @SneakyThrows
    @EventHandler
    public void onDispense(BlockDispenseEvent event) {
        Block block = event.getBlock();
        boolean isDispensing32k = is32kShulker(event.getItem()) && !block.getWorld().getName().equalsIgnoreCase("world_nether");
        event.setCancelled(block.getLocation().getY() <= 1 || block.getLocation().getY() >= 255 || isDispensing32k);
        if (isDispensing32k) {
            Bukkit.getScheduler().runTask(PVPServer.getInstance(), () -> clearDispenser(block));
        }
    }

    @SneakyThrows
    private void clearDispenser(Block block) {
        World world = ((CraftWorld) block.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(block.getX(), block.getY(), block.getZ());
        TileEntityDispenser dispenser = (TileEntityDispenser) world.getTileEntity(pos);
        if (dispenser == null) return;
        Field items = TileEntityDispenser.class.getDeclaredField("items");
        items.setAccessible(true);
        NonNullList<net.minecraft.server.v1_12_R1.ItemStack> itemStacks = (NonNullList<net.minecraft.server.v1_12_R1.ItemStack>) items.get(dispenser);
        itemStacks.clear();
        items.set(dispenser, itemStacks);
    }

    public boolean is32kShulker(ItemStack itemStack) {
        if (!ItemUtil.isShulker(itemStack)) return false;
        ShulkerBox shulkerBox = (ShulkerBox) ((BlockStateMeta) itemStack.getItemMeta()).getBlockState();
        for (ItemStack content : shulkerBox.getInventory().getContents()) {
            if (ItemUtil.is32k(content)) return true;
        }
        return false;
    }
}
