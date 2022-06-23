package me.sevj6.pvp.listener.listeners.frames;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public interface IFrame {

    void open(Player player);

    Inventory getInventory();

    String getName();
}
