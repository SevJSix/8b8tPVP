package me.sevj6.pvp.kit.gui.selectors;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.kit.gui.KitGuiBasic;
import me.sevj6.pvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class KitGuiTypeSelector extends KitGuiBasic {

    public KitGuiTypeSelector(Player player) {
        super(player, 9, Utils.PREFIX + " &7➠&r " + "&1Kits", null);
        ItemStack globalKitsItem = new ItemStack(Material.EMPTY_MAP);
        ItemMeta meta = globalKitsItem.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&lGlobal Kits"));
        globalKitsItem.setItemMeta(meta);
        ItemStack userKitsItem = new ItemStack(Material.MAP);
        ItemMeta userItemMeta = userKitsItem.getItemMeta();
        userItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&2&lUser Kits &r&2(" + player.getName() + ")"));
        userKitsItem.setItemMeta(userItemMeta);
        getInventory().setItem(3, globalKitsItem);
        getInventory().setItem(5, userKitsItem);
    }

    @Override
    public void onSlotClick(int slot) {
        Player player = getPlayer();
        if (slot == 3) {
            KitGuiGlobal guiGlobal = new KitGuiGlobal(player, PVPServer.getInstance().getKitManager());
            guiGlobal.open();
        } else if (slot == 5) {
            KitGuiUser guiUser = new KitGuiUser(player, PVPServer.getInstance().getKitManager());
            guiUser.open();
        }
    }
}
