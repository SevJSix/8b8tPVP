package me.sevj6.pvp.mechanics;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class UsefulFeatures implements Listener {

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
}
