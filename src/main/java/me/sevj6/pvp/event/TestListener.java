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
        System.out.println(event.getPlayer() + " is using a 32k against " + event.getTarget());
    }
}
