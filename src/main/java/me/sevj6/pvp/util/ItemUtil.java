package me.sevj6.pvp.util;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtil {

    private static boolean isInvalid(ItemStack itemStack) {
        return itemStack == null || itemStack.getType().equals(Material.AIR);
    }

    public static boolean is32k(ItemStack itemStack) {
        if (isInvalid(itemStack)) return false;
        if (!itemStack.hasItemMeta()) return false;
        if (!itemStack.getItemMeta().hasEnchants()) return false;
        return itemStack.getEnchantments().entrySet().stream().anyMatch(entry -> entry.getValue() > entry.getKey().getMaxLevel());
    }

    public static void revertEnchants(ItemStack itemStack) {
        if (!is32k(itemStack)) return;
        itemStack.getEnchantments().forEach((key, value) -> {
            itemStack.removeEnchantment(key);
            itemStack.addEnchantment(key, key.getMaxLevel());
            itemStack.setAmount(itemStack.getMaxStackSize());
        });
    }

    public static boolean isShulker(Object object) {
        if (object instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) object;
            if (itemStack.hasItemMeta()) {
                ItemMeta meta = itemStack.getItemMeta();
                if (meta instanceof BlockStateMeta) {
                    BlockStateMeta bsm = (BlockStateMeta) meta;
                    return bsm.getBlockState() instanceof ShulkerBox;
                }
            }
        } else if (object instanceof Block) {
            return ((Block) object).getState() instanceof ShulkerBox;
        }
        return false;
    }
}
