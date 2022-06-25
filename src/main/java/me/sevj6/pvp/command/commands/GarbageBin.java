package me.sevj6.pvp.command.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class GarbageBin implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Inventory bin = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&9&lGarbage Bin"));
            player.openInventory(bin);
        }
        return true;
    }
}
