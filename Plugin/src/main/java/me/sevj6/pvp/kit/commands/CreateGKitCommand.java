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
 * @since 6/14/22/ 10:16 PM
 * This file was created as a part of 8b8tPVP
 */
@AllArgsConstructor
public class CreateGKitCommand implements CommandExecutor {
    private final KitManager manager;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("8b8tpvp.creategkit")) {
                if (args.length >= 1) {
                    if (!(manager.getGlobalKits().size() >= 36)) {
                        String name = args[0];
                        if (!manager.isKitRegistered(name, null)) {
                            Kit kit = new Kit(null, name);
                            NBTTagList items = new NBTTagList();
                            ((CraftInventoryPlayer) player.getInventory()).getInventory().a(items);
                            kit.setKitItems(items, true);
                            manager.registerKit(kit);
                            Utils.sendMessage(player, "&3Successfully created kit with name&r&a " + kit.getName());
                        } else Utils.sendMessage(player, "&cA kit with that name already exists");
                    } else Utils.sendMessage(player, "&cThe maximum amount of global kits &4(36) &chave been created");
                } else Utils.sendMessage(sender, "&c/creategkit <name>");
            } else Utils.sendMessage(sender, "&cYou do not have permission to run this command");
        } else Utils.sendMessage(sender, "&cYou must be a player to run this command");
        return true;
    }
}
