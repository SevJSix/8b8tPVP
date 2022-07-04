package me.sevj6.pvp.command.commands;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class Help implements CommandExecutor {

    private final List<String> helpList;

    public Help() {
        this.helpList = PVPServer.getInstance().getConfig().getStringList("HelpList");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Utils.sendMessage(player, "&aNow showing useful server commands.");
            String message = ChatColor.translateAlternateColorCodes('&', String.join("\n", helpList));
            player.sendMessage(message);
        }
        return true;
    }
}
