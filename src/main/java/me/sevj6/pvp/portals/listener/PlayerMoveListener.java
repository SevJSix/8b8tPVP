package me.sevj6.pvp.portals.listener;

import me.sevj6.pvp.portals.PortalManager;
import me.sevj6.pvp.portals.boiler.Portal;
import me.sevj6.pvp.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final PortalManager portalManager;

    public PlayerMoveListener(PortalManager portalManager) {
        this.portalManager = portalManager;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (Portal portal : portalManager.getPortals()) {
            if (Utils.isPlayerInPortal(player, portal)) {
                portal.onPortalEnter(portal.getExitArena(), player);
            }
        }
    }
}
