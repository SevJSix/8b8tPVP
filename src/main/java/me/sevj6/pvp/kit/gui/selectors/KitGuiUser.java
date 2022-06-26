package me.sevj6.pvp.kit.gui.selectors;

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
import org.bukkit.inventory.meta.ItemMeta;

public class KitGuiUser extends KitGuiBasic {

    private final KitManager kitManager;

    public KitGuiUser(Player player, KitManager kitManager) {
        super(player, 9, Utils.PREFIX + " &7➠&r " + "&2&lUser Kits&r", player);
        this.kitManager = kitManager;
        for (int i = 0; i < kitManager.getKits(player).size(); i++) {
            Kit kit = kitManager.getKits(player).get(i);
            org.bukkit.inventory.ItemStack current = new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD);
            ItemMeta meta = current.getItemMeta();
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a" + kit.getName()));
            current.setItemMeta(meta);
            net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(current);
            NBTTagCompound compound = nmsCopy.getTag();
            compound.setString("AttachedUserKit", kit.getName());
            nmsCopy.setTag(compound);
            getInventory().setItem(i, nmsCopy.asBukkitCopy());
        }
    }

    @Override
    public void onSlotClick(int slot) {
        Player player = getPlayer();
        String kitNameFromItem = parseKitName(getItem(slot));
        if (kitNameFromItem == null) return;
        Kit kit = kitManager.getKits(player).stream().filter(k -> k.getName().equalsIgnoreCase(kitNameFromItem)).findAny().orElse(null);
        if (kit == null) return;
        kit.setLoadOut(player);
        player.closeInventory();
        sendKittedMessage(kit);
    }

    private String parseKitName(org.bukkit.inventory.ItemStack itemStack) {
        net.minecraft.server.v1_12_R1.ItemStack nmsCopy = CraftItemStack.asNMSCopy(itemStack);
        if (!nmsCopy.hasTag()) return null;
        NBTTagCompound compound = nmsCopy.getTag();
        if (!compound.hasKey("AttachedUserKit")) return null;
        return compound.getString("AttachedUserKit");
    }
}
