package me.sevj6.pvp.listener.listeners;

import com.destroystokyo.paper.Title;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import me.sevj6.pvp.PVPServer;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
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
    public void onFallDamageTake(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
            event.setDamage(0.0D);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.sendTitle(new Title("", "", 0, 0, 0));
        player.sendActionBar("");
    }

    @EventHandler
    public void onMotd(PaperServerListPingEvent event) {
        event.setNumPlayers(Integer.MAX_VALUE);
        event.setMaxPlayers(-1);
    }
}
