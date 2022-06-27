package me.sevj6.pvp.kit.commands;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.kit.Kit;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventoryPlayer;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 6/14/22/ 11:16 PM
 * This file was created as a part of 8b8tPVP
 */
@AllArgsConstructor
public class CreateUKitCommand implements CommandExecutor {
    private final KitManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("8b8tpvp.createukit")) {
                if (args.length >= 1) {
                    boolean tooManyKits = manager.getKits(player) != null && manager.getKits(player).size() >= 9;
                    if (!tooManyKits) {
                        String name = args[0];
                        if (!manager.isKitRegistered(name, player.getUniqueId())) {
                            Kit kit = new Kit(player, name);
                            NBTTagList items = new NBTTagList();
                            ((CraftInventoryPlayer) player.getInventory()).getInventory().a(items);
                            kit.setKitItems(items, true);
                            manager.registerKit(kit);
                            Utils.sendMessage(player, "&3Successfully created kit with name&r&a " + kit.getName());
                        } else Utils.sendMessage(player, "&cA kit with that name already exists");
                    } else Utils.sendMessage(player, "&cYou can only have a maximum of 9 user kits");
                } else Utils.sendMessage(sender, "&c/createukit <name>");
            } else Utils.sendMessage(sender, "&cYou do not have permission to run this command");
        } else Utils.sendMessage(sender, "&cYou must be a player to run this command");
        return true;
    }
}
