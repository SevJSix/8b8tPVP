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
        this.inventory = Bukkit.createInventory(null, 9, parse(name, item));
        this.item = item;
        item.setAmount(item.getMaxStackSize());
        for (int i = 0; i < inventory.getContents().length; i++) {
            inventory.setItem(i, item);
        }
    }

    private String parse(String text, ItemStack item) {
        String matType = "&r\u23d0 " + "&9ItemType: &l" + item.getType().toString() + "&r" + " \u23d0";
        String itemAmount = "&9StackSize: &l" + item.getAmount() + "&r" + " \u23d0";
        String numberOfEnchants = (item.getEnchantments().size() == 0 || item.getEnchantments() == null) ? "" : "&9EnchantCount: &l" + item.getEnchantments().size() + "&r" + " \u23d0";
        text = text.replace("%mat%", matType).replace("%size%", itemAmount).replace("%ench%", numberOfEnchants);
        return ChatColor.translateAlternateColorCodes('&', text);
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
