package me.sevj6.pvp.listener.listeners;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.event.PlayerPlaceCrystalEvent;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutBlockChange;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class DisableActivity implements Listener {

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
    public void onPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (playerNotInArena(player)) {
            event.setCancelled(true);
            sendBlockChangePacket(player, event.getBlockPlaced().getLocation(), event.getBlock().getLocation(), event.getBlockAgainst().getLocation());
        } else if (event.getBlockPlaced().getType().equals(Material.ENDER_CHEST)) {
            Location location = event.getBlockPlaced().getLocation();
            Bukkit.getScheduler().runTaskLater(PVPServer.getInstance(), () -> {
                if (location.getWorld().getBlockAt(location).getType() == Material.ENDER_CHEST) {
                    location.getWorld().getBlockAt(location).setType(Material.AIR);
                }
            }, (20L * 120L));
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        try {
            if (player == null || event.getClickedBlock() == null) return;
            if (playerNotInArena(player)) {
                event.setCancelled(true);
                sendBlockChangePacket(player, event.getClickedBlock().getLocation());
            }
        } catch (Throwable ignored) {
        }
    }

    @EventHandler
    public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked() instanceof ItemFrame) {
            ItemFrame frame = (ItemFrame) event.getRightClicked();
            frame.setRotation(frame.getRotation().rotateCounterClockwise());
            return;
        }
        if (playerNotInArena(event.getPlayer()) || !Utils.isPositionInArena(new BlockPosition(event.getClickedPosition().getX(), event.getClickedPosition().getY(), event.getClickedPosition().getZ())))
            event.setCancelled(true);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        if (playerNotInArena(event.getPlayer())) event.setCancelled(true);
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
    public void onPlaceCrystal(PlayerPlaceCrystalEvent event) {
        if (playerNotInArena(event.getPlayer())) event.setCancelled(true);
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
    public void onExplode(BlockExplodeEvent event) {
        Location location = event.getBlock().getLocation();
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        if (!Utils.isPositionInArena(pos)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemFramePlace(HangingPlaceEvent event) {
        if (playerNotInArena(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameBreak(HangingBreakEvent event) {
        Location location = event.getEntity().getLocation();
        BlockPosition pos = new BlockPosition(location.getX(), location.getY(), location.getZ());
        if (!Utils.isPositionInArena(pos)) event.setCancelled(true);
    }

    @EventHandler
    public void onItemFrameBreakByEntity(HangingBreakByEntityEvent event) {
        if (event.getRemover() instanceof Player) {
            Player player = (Player) event.getRemover();
            if (playerNotInArena(player)) event.setCancelled(true);
        }
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

    private void sendBlockChangePacket(Player player, Location... locations) {
        EntityPlayer handle = ((CraftPlayer) player).getHandle();
        for (Location l : locations) {
            handle.playerConnection.sendPacket(new PacketPlayOutBlockChange(((CraftWorld) player.getWorld()).getHandle(), new BlockPosition(l.getX(), l.getY(), l.getZ())));

        }
    }
}
