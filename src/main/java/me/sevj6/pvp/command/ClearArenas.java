package me.sevj6.pvp.command;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearArenas implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        Bukkit.getOnlinePlayers().forEach(player -> Utils.sendMessage(player, "&eNow clearing all server arenas. Expect momentary lag"));
        for (Arena arena : PVPServer.getArenaManager().getArenas()) {
            for (Player activePlayer : arena.getActivePlayers()) {
                Utils.sendMessage(activePlayer, "&cYour current arena (" + arena.getName() + ") is being cleared!");
            }
        }
        PVPServer.getArenaManager().getArenas().forEach(Arena::clear);
        Utils.removeEntities();
        return true;
    }
}
