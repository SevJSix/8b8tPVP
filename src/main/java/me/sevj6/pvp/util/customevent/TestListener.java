package me.sevj6.pvp.util.customevent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    @EventHandler
    public void onPop(TotemPopEvent event) {
        System.out.println("popped");
    }

    @EventHandler
    public void onCrystal(PlayerPlaceCrystalEvent event) {
        System.out.println("player has placed a crystal at " + event.getCrystalLocation());
    }
}
