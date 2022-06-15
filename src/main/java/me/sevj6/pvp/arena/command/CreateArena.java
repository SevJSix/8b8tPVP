package me.sevj6.pvp.arena.command;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.mechanics.InteractListener;
import me.sevj6.pvp.util.Utils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CreateArena implements CommandExecutor {

    private final InteractListener interactListener;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            if (args.length > 0) {
                if (interactListener.getPos1() == null || interactListener.getPos2() == null) {
                    Utils.sendMessage(player, "&cSelect a full bounding box first");
                    return true;
                }
                String arenaName = args[0];
                PVPServer.getArenaManager().constructArena(arenaName, world, interactListener.getPos1(), interactListener.getPos2());
                Utils.sendMessage(player, "&aSuccessfully created &3" + arenaName);
            } else {
                Utils.sendMessage(player, "&cType an arena name to define by");
            }
        }
        return true;
    }
}
