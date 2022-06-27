package me.sevj6.pvp.kit.gui.selectors;

import lombok.Getter;
import me.sevj6.pvp.kit.Kit;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.kit.gui.KitGuiBasic;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class KitGuiGlobal extends KitGuiBasic {

    @Getter
    private final KitManager kitManager;

    public KitGuiGlobal(Player player, KitManager kitManager) {
        super(player, 36, Utils.PREFIX + " &7➠&r " + "&9&lGlobal Kits&r", player);
        this.kitManager = kitManager;
        for (int i = 0; i < kitManager.getGlobalKits().size(); i++) {
            Kit kit = kitManager.getGlobalKits().get(i);
            ItemStack current = new ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = current.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&3" + kit.getName()));
            meta.setLore(Collections.singletonList(ChatColor.translateAlternateColorCodes('&', "&7&oGlobal Kit - Accessible to All Players")));
            current.setItemMeta(meta);
            net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(current);
            NBTTagCompound compound = nmsCopy.getTag();
            compound.setString("AttachedGlobalKit", kit.getName());
            nmsCopy.setTag(compound);
            getInventory().setItem(i, nmsCopy.asBukkitCopy());
        }
        ItemStack goBackButton = new ItemStack(Material.STONE_BUTTON);
        ItemMeta meta = goBackButton.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7Go Back"));
        goBackButton.setItemMeta(meta);
        getInventory().setItem(27, goBackButton);
    }

    @Override
    public void onSlotClick(int slot) {
        Player player = getPlayer();
        if (getItem(slot).getType() == Material.STONE_BUTTON) {
            KitGuiTypeSelector guiTypeSelector = new KitGuiTypeSelector(player);
            guiTypeSelector.open();
            return;
        }
        String kitNameFromItem = parseKitName(getItem(slot));
        if (kitNameFromItem == null) return;
        Kit kit = kitManager.getGlobalKits().stream().filter(k -> k.getName().equalsIgnoreCase(kitNameFromItem)).findAny().orElse(null);
        if (kit == null) return;
        kit.setLoadOut(player);
        player.closeInventory();
        sendKittedMessage(kit);
    }

    private String parseKitName(ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsCopy.hasTag()) return null;
        NBTTagCompound compound = nmsCopy.getTag();
        if (!compound.hasKey("AttachedGlobalKit")) return null;
        return compound.getString("AttachedGlobalKit");
    }
}
