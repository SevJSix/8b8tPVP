package me.sevj6.pvp.event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    @EventHandler
    public void onPop(TotemPopEvent event) {
        System.out.println("popped");
    }

    @EventHandler
    public void onCrystal(PlayerPlaceCrystalEvent event) {
        event.explodeCrystal();
    }

    @EventHandler
    public void on32k(PlayerUse32kEvent event) {
        if (event.getDistanceBetweenAttackerAndTarget() > 8 ||  event.isPlayerAttackingTooFarFromActiveContainer()) {
            event.setCancelled(true);
            event.getPlayer().getInventory().remove(event.getPlayer().getInventory().getItem(event.getItemSlot()));
            System.out.println("prevented " + event.getPlayer().getName() + " from possibly 32k blink teleporting");
        }
    }
}
