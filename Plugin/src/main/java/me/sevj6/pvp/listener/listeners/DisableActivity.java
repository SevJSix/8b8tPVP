package me.sevj6.pvp.listener.listeners;

import lombok.SneakyThrows;
import me.sevj6.pvp.event.PlayerPlaceCrystalEvent;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.reflect.Method;
import java.util.Objects;

public class DisableActivity implements Listener {

    private final org.bukkit.World SWORD_FIGHT;

    public DisableActivity() {
        this.SWORD_FIGHT = Bukkit.getWorld("swordfight");
    }

    public static void sendBlockChangePacket(Player player, Location... locations) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        for (Location l : locations) {
            handle.playerConnection.sendPacket(new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(l.getX(), l.getY(), l.getZ())));

        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if (playerNotInArena(player)) {
            event.setCancelled(true);
            sendBlockChangePacket(player, event.getBlock().getLocation());
        }
    }

    // prevent regular nether/end portals
    @EventHandler
    public void onPortal(EntityPortalEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPortalEnter(EntityPortalExitEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlowUp(EntityExplodeEvent event) {
        Location location = event.getLocation();
        if (Utils.isPositionInArena(new BlockPosition(location.getX(), location.getY(), location.getZ()))) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        try {
            if (player == null || event.getClickedBlock() == null) return;
            if (player.getWorld().getName().equalsIgnoreCase("world_the_end")) return;
            if (playerNotInArena(player)) {
                event.setCancelled(true);
                sendBlockChangePacket(player, event.getClickedBlock().getLocation());
            }
        } catch (Throwable ignored) {
        }
    }

    @EventHandler
    @SneakyThrows
    public void onAnvilUse(PrepareAnvilEvent event) {
        Location location = event.getInventory().getLocation();
        World world = ((CraftWorld) location.getWorld()).getHandle();
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        BlockAnvil blockAnvil = (BlockAnvil) world.getType(pos).getBlock();
        Method blockDataM = Block.class.getDeclaredMethod("w", IBlockData.class);
        blockDataM.setAccessible(true);
        IBlockData data = blockAnvil.getBlockData().set(BlockAnvil.FACING, EnumDirection.NORTH).set(BlockAnvil.DAMAGE, 0);
        blockDataM.invoke(blockAnvil, data);
        EntityPlayer player = ((CraftPlayer) Objects.requireNonNull(location.getNearbyPlayers(5).stream().findFirst().orElse(null))).getHandle();
        world.setTypeAndData(pos, blockAnvil.getPlacedState(world, pos, EnumDirection.NORTH, 0F, 0F, 0f, 0, player).set(BlockAnvil.DAMAGE, 0), 0);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (playerNotInArena(event.getEntity())) {
            event.getDrops().clear();
            event.setDroppedExp(0);
            event.setKeepInventory(true);
        }
    }

    @EventHandler
    public void onGrow(BlockGrowEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        Player player = (Player) event.getEntity();
        if (playerNotInArena(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onDamageByBlock(EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();
        if (playerNotInArena(player) || playerNotInArena(victim)) event.setCancelled(true);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        Player player = (Player) event.getDamager();
        if (playerNotInArena(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if (playerNotInArena(event.getPlayer())) {
            PlayerTeleportEvent.TeleportCause cause = event.getCause();
            if (cause == PlayerTeleportEvent.TeleportCause.ENDER_PEARL || cause == PlayerTeleportEvent.TeleportCause.CHORUS_FRUIT) {
                event.setTo(event.getFrom());
            }
        }
    }

    @EventHandler
    public void onCrystalSpawn(PlayerPlaceCrystalEvent event) {
        if (SWORD_FIGHT.equals(event.getPlayer().getWorld())) {
            cancelCrystal(event);
            return;
        }
        if (Utils.isPlayerInArena(event.getPlayer())) return;
        cancelCrystal(event);
    }

    private void cancelCrystal(PlayerPlaceCrystalEvent event) {
        event.setCancelled(true);
        if (event.getCrystal() == null) return;
        event.getCrystal().setInvulnerable(true);
        event.getCrystal().die();
    }

    @EventHandler
    public void onBow(ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() instanceof Player) {
            Player player = (Player) event.getEntity().getShooter();
            if (event.getEntity() instanceof Arrow || event.getEntity() instanceof ThrownPotion) {
                if (Utils.isPlayerInArena(player)) return;
                event.setCancelled(true);
                if ((event.getEntity() instanceof Arrow)) {
                    Utils.sendMessage(player, "&cYou cannot shoot arrows here");
                } else {
                    Utils.sendMessage(player, "&cYou cannot throw potions here");
                }
            }
        }
    }

    @EventHandler
    public void onEchest(InventoryOpenEvent event) {
        if (event.getPlayer().isOp()) return;
        if (event.getInventory().getType() == InventoryType.ENDER_CHEST) event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreak(HangingBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onLiquidFlow(BlockFromToEvent event) {
        Location location = event.getBlock().getLocation();
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        if (!Utils.isPositionInArena(pos)) {
            Material type = event.getBlock().getType();
            if (type == Material.LAVA || type == Material.STATIONARY_LAVA ||
                    type == Material.WATER || type == Material.STATIONARY_WATER) {
                event.setCancelled(true);
            }
        }
    }

    private boolean playerNotInArena(Player player) {
        if (((CraftPlayer) player).getHandle().isCreativeAndOp()) return false;
        return !Utils.isPlayerInArena(player);
    }
}
