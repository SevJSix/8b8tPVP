package me.sevj6.pvp.listener.listeners;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class CommandListener implements Listener {

    private final List<String> arenaAllowed;
    private final List<String> everywhereAllowed;
    private final String denied;
    private final boolean opBypass;

    public CommandListener() {
        arenaAllowed = PVPServer.getInstance().getConfig().getStringList("Command.ArenaAllowed");
        everywhereAllowed = PVPServer.getInstance().getConfig().getStringList("Command.EverywhereAllowed");
        everywhereAllowed.addAll(arenaAllowed);
        denied = PVPServer.getInstance().getConfig().getString("Command.DeniedMsg");
        opBypass = PVPServer.getInstance().getConfig().getBoolean("Command.OpBypass");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (opBypass) if (player.isOp()) return;
        String command = event.getMessage().split(" ")[0].replace("/", "");
        boolean cancel = false;
        if (Utils.isPlayerInArena(player)) {
            if (!arenaAllowed.contains(command.toLowerCase())) {
                cancel = true;
            }
        } else {
            if (!everywhereAllowed.contains(command.toLowerCase())) {
                cancel = true;
            }
        }
        if (cancel) {
            event.setCancelled(true);
            Utils.sendMessage(player, denied);
        }
    }
}
