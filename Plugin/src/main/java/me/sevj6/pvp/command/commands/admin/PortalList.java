package me.sevj6.pvp.command.commands.admin;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.portals.PortalManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class PortalList implements CommandExecutor {

    private final PortalManager portalManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            portalManager.getPortals().forEach(portal -> {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        portal.getName() + "\n" + " &aWorld: &3" + portal.getWorld().getName() + "\n" +
                                "Area: " + portal.getBoundingBox().toString() + "\n" + "Exit Arena: " + portal.getExitArena().getName() + "\n"));
            });
        }
        return true;
    }
}
