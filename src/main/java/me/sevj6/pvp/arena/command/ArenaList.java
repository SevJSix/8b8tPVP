package me.sevj6.pvp.arena.command;

import me.sevj6.pvp.PVPServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ArenaList implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            PVPServer.getArenaManager().getArenas().forEach(arena -> {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        arena.getName() + "\n" + " &aWorld: &3" + arena.getWorld().getName() + "\n" + " &aRegion: &3pos1[" + arena.getFirstPosition().getX() + ", " + arena.getFirstPosition().getY() + ", " + arena.getFirstPosition().getZ() + "] pos2[" + arena.getSecondPosition().getX() + ", " + arena.getSecondPosition().getY() + ", " + arena.getSecondPosition().getZ() + "]\n"));
            });
        }
        return true;
    }
}
