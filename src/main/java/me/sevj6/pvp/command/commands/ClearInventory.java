package me.sevj6.pvp.command.commands;

import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventory implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Utils.sendMessage(player, "&aNow clearing your inventory");
            player.getInventory().clear();
            player.updateInventory();
        }
        return true;
    }
}
