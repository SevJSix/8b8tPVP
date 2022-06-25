package me.sevj6.pvp.command.commands;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KitCreator implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            player.teleport(PVPServer.getInstance().getKitCreator());
            Utils.sendMessage(player, "&asending you to the &3kitcreator");
        }
        return true;
    }
}
