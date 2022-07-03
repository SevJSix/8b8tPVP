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
import org.bukkit.block.ShulkerBox;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class PvPListeners implements Listener {

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
