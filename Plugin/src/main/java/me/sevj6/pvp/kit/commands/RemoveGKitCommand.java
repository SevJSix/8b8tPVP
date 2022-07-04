package me.sevj6.pvp.kit.commands;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.kit.Kit;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AllArgsConstructor
public class RemoveGKitCommand implements CommandExecutor {

    private final KitManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("8b8tpvp.removegkit")) {
                if (args.length >= 1) {
                    String name = args[0];
                    Kit kit = manager.getGlobalKits().stream().filter(k -> k.getName().equalsIgnoreCase(name)).findAny().orElse(null);
                    if (kit == null) {
                        Utils.sendMessage(player, "&cNo kit with the name &a" + name + "&c exists");
                        return true;
                    }
                    manager.removeKit(player, kit);
                } else Utils.sendMessage(sender, "&c/removegkit <name>");
            } else Utils.sendMessage(sender, "&cYou do not have permission to run this command");
        } else Utils.sendMessage(sender, "&cYou must be a player to run this command");
        return true;
    }
}
