package me.sevj6.pvp.command.commands;

import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Discord implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        Utils.sendMessage(sender, "&ahttps://discord.gg/T34ZFHjySc");
        return true;
    }
}
