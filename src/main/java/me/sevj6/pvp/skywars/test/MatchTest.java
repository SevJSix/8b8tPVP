package me.sevj6.pvp.skywars.test;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.skywars.SkywarsManager;
import me.sevj6.pvp.skywars.match.Match;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class MatchTest implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            List<Location> locations = Arrays.asList(new Location(world, 0.5, 150, 0.5),
                    new Location(world, 7.5, 150, 7.5),
                    new Location(world, -7.5, 150, -7.5),
                    new Location(world, 0.5, 150, 7.5),
                    new Location(world, 0.5, 150, -7.5),
                    new Location(world, 7.5, 150, 0.5),
                    new Location(world, -7.5, 150, 0.5));
            Match match = new Match(locations, PVPServer.getArenaManager().getArenaByName("skywars"));
            SkywarsManager.getInstance().registerMatch(match);
            match.queuePlayer(player);
            match.genGlass();
            match.start();
        }
        return true;
    }
}
