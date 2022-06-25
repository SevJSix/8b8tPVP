package me.sevj6.pvp.listener.listeners;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.Item;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class Miscellaneous implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        event.setRespawnLocation(PVPServer.getInstance().getSpawn());
    }

    @EventHandler
    public void onDrink(PlayerItemConsumeEvent event) {
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
        if (event.getItem().getItemMeta() != null && event.getItem().getItemMeta() instanceof PotionMeta) {
            event.setReplacement(new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onWorldSwitch(PlayerChangedWorldEvent event) {
        Utils.parsePlayerListName(event.getPlayer());
    }


    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (event.getEntity() instanceof EnderDragon) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFallDamageTake(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(0.0D);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendTitle(new Title("", "", 0, 0, 0));
        player.sendActionBar("");
        player.teleport(PVPServer.getInstance().getSpawn());
        Utils.parsePlayerListName(player);
    }

    @EventHandler
    public void onMotd(PaperServerListPingEvent event) {
        event.setNumPlayers(Integer.MAX_VALUE);
        event.setMaxPlayers(-1);
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase("world")) return;
        Player player = event.getPlayer();
        Location location = player.getLocation();
        AxisAlignedBB parkourBox = new AxisAlignedBB(new BlockPosition(1, 119, 1), new BlockPosition(-1, 118, -1));
        AxisAlignedBB playerBox = new AxisAlignedBB(new BlockPosition(location.getX(), location.getY(), location.getZ()), new BlockPosition(location.getX(), location.getY() + 1, location.getZ()));
        if (parkourBox.intersects(playerBox)) {
            player.sendTitle(new Title("parkour ahhhh nigga cuh"));
            EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
            for (int i = 0; i < entityPlayer.defaultContainer.items.size(); i++) {
                Item item = entityPlayer.defaultContainer.items.get(i).getItem();
                if (item != null && item.getName().contains("elytra")) {
                    entityPlayer.defaultContainer.setItem(i, net.minecraft.server.v1_12_R1.ItemStack.fromBukkitCopy(new ItemStack(Material.AIR)));
                }
            }
        }
    }

    @EventHandler
    public void onBlockDestroy(EntityChangeBlockEvent event) {
        event.setCancelled(event.getEntity() instanceof EnderDragon || event.getEntity() instanceof Enderman);
    }
}
