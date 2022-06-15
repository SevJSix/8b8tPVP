package me.sevj6.pvp.portals.command;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.mechanics.InteractListener;
import me.sevj6.pvp.portals.PortalManager;
import me.sevj6.pvp.util.Utils;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class CreatePortal implements CommandExecutor {

    private final PortalManager portalManager;
    private final InteractListener interactListener;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            World world = player.getWorld();
            if (args.length > 0) {
                if (interactListener.getPos1() == null || interactListener.getPos2() == null) {
                    Utils.sendMessage(player, "&cSelect a full bounding box first");
                    return true;
                }
                String portalName = args[0];
                Arena arena = PVPServer.getArenaManager().getArenaByName(args[1]);
                portalManager.createPortal(portalName, world, interactListener.getPos1(), interactListener.getPos2(), arena);
                Utils.sendMessage(player, "&aSuccessfully created &3" + portalName);
            } else {
                Utils.sendMessage(player, "&cType an arena name to define by");
            }
        }
        return true;
    }
}
