package me.sevj6.pvp.kit.listener;

import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.kit.gui.selectors.KitGuiGlobal;
import me.sevj6.pvp.kit.gui.selectors.KitGuiTypeSelector;
import me.sevj6.pvp.kit.gui.selectors.KitGuiUser;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

public class InventoryClick implements Listener {

    private final KitManager kitManager;

    public InventoryClick(KitManager kitManager) {
        this.kitManager = kitManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        InventoryView view = event.getView();
        Player player = (Player) view.getPlayer();
        int slot = event.getSlot();
        if (inventory == null || inventory.getTitle() == null) return;
        if (inventory.getHolder() == null && ChatColor.stripColor(inventory.getTitle()).contains("Kits")) {
            event.setCancelled(true);
            KitGuiTypeSelector typeSelector = new KitGuiTypeSelector(player);
            typeSelector.onSlotClick(slot);
        } else if (ChatColor.stripColor(inventory.getTitle()).contains("Global Kits")) {
            event.setCancelled(true);
            KitGuiGlobal guiGlobal = new KitGuiGlobal(player, kitManager);
            guiGlobal.onSlotClick(slot);
        } else if (ChatColor.stripColor(inventory.getTitle()).contains("User Kits")) {
            event.setCancelled(true);
            KitGuiUser guiUser = new KitGuiUser(player, kitManager);
            guiUser.onSlotClick(slot);
        }
    }
}
