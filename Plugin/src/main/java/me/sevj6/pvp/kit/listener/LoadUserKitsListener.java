package me.sevj6.pvp.kit.listener;

import me.sevj6.pvp.kit.KitManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoadUserKitsListener implements Listener {

    private final KitManager kitManager;

    public LoadUserKitsListener(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        try {
            kitManager.loadUserKits(player);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
