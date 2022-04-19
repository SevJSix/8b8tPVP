package me.sevj6.pvp.arena;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityItemFrame;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class Test implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        Bukkit.getOnlinePlayers().forEach(player -> Utils.sendMessage(player, "&eNow clearing all server arenas. Expect momentary lag"));
        for (Arena arena : PVPServer.getArenaManager().getArenas()) {
            for (Player activePlayer : arena.getActivePlayers()) {
                Utils.sendMessage(activePlayer, "&cYou are currently inside of arena " + arena.getName());
            }
        }
        PVPServer.getArenaManager().getArenas().forEach(Arena::clear);
        Utils.removeEntities();
        return true;
    }
}
