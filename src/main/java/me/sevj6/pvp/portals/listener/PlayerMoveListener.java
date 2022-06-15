package me.sevj6.pvp.portals.listener;

import com.destroystokyo.paper.Title;
import me.sevj6.pvp.portals.PortalManager;
import me.sevj6.pvp.portals.boiler.Portal;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
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
            if (portal.getBoundingBox().intersects(((CraftPlayer) player).getHandle().getBoundingBox())) {
                portal.onPortalEnter(portal.getExitArena(), player);
                player.sendTitle(new Title(ChatColor.translateAlternateColorCodes('&', "&e&lNOW ENTERING &a&l" + portal.getExitArena().getName()),
                        ChatColor.translateAlternateColorCodes('&', "&3Players in arena: &l" + portal.getExitArena().getActivePlayers().size()), 10, 32, 10));
            }
        }
    }
}
