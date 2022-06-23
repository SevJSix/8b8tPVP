package me.sevj6.pvp.listener.listeners.frames;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class FrameInventory implements IFrame {

    private final Inventory inventory;
    private final String name;
    @Getter
    private final ItemStack item;

    public FrameInventory(String name, ItemStack item) {
        this.name = name;
        this.inventory = Bukkit.createInventory(null, 9, ChatColor.translateAlternateColorCodes('&', name));
        this.item = item;
        item.setAmount(item.getMaxStackSize());
        for (int i = 0; i < inventory.getContents().length; i++) {
            inventory.setItem(i, item);
        }
    }

    @Override
    public void open(Player player) {
        ((CraftPlayer) player).getHandle().openContainer(((CraftInventory) inventory).getInventory());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public String getName() {
        return name;
    }
}
