package me.sevj6.pvp.kit;

import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 4/13/22/ 12:05 AM
 * This file was created as a part of 8b8tPVP
 */
@AllArgsConstructor
public class KitSaveTest implements CommandExecutor {
    private final KitManager manager;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player player = (Player) sender;
        Kit kit = new Kit(player.getUniqueId(), player.getInventory(), "TestKit");
        kit.save();
        return true;
    }
}
