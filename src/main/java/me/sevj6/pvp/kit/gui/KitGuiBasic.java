package me.sevj6.pvp.kit.gui;

import me.sevj6.pvp.kit.Kit;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class KitGuiBasic extends KitGui {

    private final Inventory inventory;
    private final Player player;
    private final int size;
    private final String title;

    public KitGuiBasic(Player player, int size, String title, InventoryHolder holder) {
        this.player = player;
        this.size = size;
        this.title = title;
        this.inventory = Bukkit.createInventory(holder, size, ChatColor.translateAlternateColorCodes('&', title));
    }

    public void sendKittedMessage(Kit kit) {
        Utils.sendMessage(player, "&3Set your loadout to &a" + kit.getName());
    }

    public void open() {
        EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        entityPlayer.openContainer(((CraftInventory) inventory).getInventory());
    }

    public boolean isGuiOpen() {
        return player.getOpenInventory().equals(this);
    }

    @Override
    public Inventory getTopInventory() {
        return inventory;
    }

    @Override
    public Inventory getBottomInventory() {
        return inventory;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public InventoryType getType() {
        return InventoryType.CREATIVE;
    }

    public int getSize() {
        return size;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void onSlotClick(int slot) {

    }
}
