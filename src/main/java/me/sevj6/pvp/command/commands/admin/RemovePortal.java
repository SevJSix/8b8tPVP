package me.sevj6.pvp.command.commands.admin;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.portals.PortalManager;
import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class RemovePortal implements CommandExecutor {

    private final PortalManager portalManager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                String portalName = args[0];
                if (portalManager.getPortalByName(portalName) == null) {
                    Utils.sendMessage(player, "&cNo portal by the name &3" + portalName + "&c exists");
                    return true;
                }
                portalManager.removePortal(portalName);
                Utils.sendMessage(player, "&aSuccessfully removed &3" + portalName);
            } else {
                Utils.sendMessage(player, "&cType a portal name to remove");
            }
        }
        return true;
    }
}
