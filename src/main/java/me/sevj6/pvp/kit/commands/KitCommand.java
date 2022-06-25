package me.sevj6.pvp.kit.commands;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.kit.Kit;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 6/14/22/ 11:44 PM
 * This file was created as a part of 8b8tPVP
 */
@AllArgsConstructor
public class KitCommand implements CommandExecutor {
    private KitManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = ((Player) sender);
            if (args.length == 1) {
                String name = args[0];
                Kit kit = manager.getGlobalKits().stream().filter(k -> k.getName().equalsIgnoreCase(name)).findAny().orElse(null);
                if (kit == null) {
                    Utils.sendMessage(player, "&cNo kit with the name&r&a " + name + "&r&c exists");
                    return true;
                }
                kit.setLoadOut(player);
                Utils.sendMessage(player, "&3Set your kit to&r&a " + kit.getName());
            } else {
                //TODO: Make a kit selection gui
            }
        } else Utils.sendMessage(sender, "&cYou must be a player to use this command");
        return true;
    }
}
