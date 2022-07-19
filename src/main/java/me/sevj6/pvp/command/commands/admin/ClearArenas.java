package me.sevj6.pvp.command.commands.admin;

import lombok.SneakyThrows;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.util.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearArenas implements CommandExecutor {

    @SneakyThrows
    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (args.length > 0) {
            Arena arena = PVPServer.getArenaManager().getArenaByName(args[0]);
            if (arena == null) {
                Utils.sendMessage(sender, "&cEnter a valid arena name!");
                return true;
            }
            arena.getActivePlayers().forEach(player -> Utils.sendMessage(player, "&cYour current arena (" + arena.getName() + ") is being cleared!"));
            arena.clear();
        } else {
            Bukkit.getOnlinePlayers().stream().filter(player -> !Utils.isPlayerInArena(player)).forEach(player -> Utils.sendMessage(player, "&eNow clearing all server arenas."));
            for (Arena arena : PVPServer.getArenaManager().getArenas()) {
                for (Player activePlayer : arena.getActivePlayers()) {
                    Utils.sendMessage(activePlayer, "&cYour current arena (" + arena.getName() + ") is being cleared!");
                }
            }
            PVPServer.getArenaManager().getArenas().forEach(Arena::clear);
            Utils.removeEntities();
        }
        return true;
    }
}
