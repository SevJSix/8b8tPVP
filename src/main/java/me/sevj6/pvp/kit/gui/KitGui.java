package me.sevj6.pvp.kit.gui;

import org.bukkit.inventory.InventoryView;

public abstract class KitGui extends InventoryView {

    public abstract void onSlotClick(int slot);
}
