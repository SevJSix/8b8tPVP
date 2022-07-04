package me.sevj6.pvp.command.commands.admin;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RemoveArena implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String arenaName = args[0];
                if (PVPServer.getArenaManager().getArenaByName(arenaName) == null) {
                    Utils.sendMessage(player, "&cNo arena by the name &3" + arenaName + "&c exists");
                    return true;
                }
                PVPServer.getArenaManager().destructArena(arenaName);
                Utils.sendMessage(player, "&aSuccessfully removed &3" + arenaName);
            } else {
                Utils.sendMessage(player, "&cType an arena name to remove");
            }
        }
        return true;
    }
}
