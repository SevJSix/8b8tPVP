package me.sevj6.pvp.arena.command;

import me.sevj6.pvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Wand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String name, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // generate pos1 setter
            ItemStack ironAxe = new ItemStack(Material.IRON_AXE);
            ItemMeta ironAxeMeta = ironAxe.getItemMeta();
            ironAxeMeta.setLocalizedName(ChatColor.GREEN + "Position 1 Setter");
            ironAxe.setItemMeta(ironAxeMeta);

            // generate pos2 setter
            ItemStack goldAxe = new ItemStack(Material.GOLD_AXE);
            ItemMeta goldAxeMeta = goldAxe.getItemMeta();
            goldAxeMeta.setLocalizedName(ChatColor.RED + "Position 2 Setter");
            goldAxe.setItemMeta(goldAxeMeta);
            player.getInventory().addItem(ironAxe, goldAxe);
            Utils.sendMessage(player, "&aGave you some wands to select a position");
        }
        return true;
    }
}
