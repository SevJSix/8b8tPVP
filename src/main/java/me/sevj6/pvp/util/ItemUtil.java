package me.sevj6.pvp.util;

import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.ShulkerBox;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

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

    public static final List<Item> illegals = Arrays.asList(
            Item.getById(7), //Bedrock
            Item.getById(166), //Barrier
            Item.getById(120), // End portal frames
            Item.getById(52), //Monster spawner
            Item.getById(255), // Structure block
            Item.getById(217), //Structure void
            Item.getById(383), //Spawn egg
            Item.getById(211), //Chain Command Block
            Item.getById(210), //Repeating Command Block
            Item.getById(137), //Command Block
            Item.getById(422), //Command Block Minecart
            Item.getById(453), //Knowledge book
            Item.getById(208), //Grass path
            Item.getById(60), //Farmland
            Item.getById(31) //Shrubs
    );

    public static boolean containsIllegals(NBTTagList tagList) {
        if (tagList == null) return false;
        if (tagList.size() == 0) return false;
        for (int i = 0; i < tagList.size(); i++) {
            NBTTagCompound compound = tagList.get(i);
            Item item = compound.hasKeyOfType("id", 8) ? Item.b(compound.getString("id")) : Item.getItemOf(Blocks.AIR);
            if (ItemUtil.illegals.contains(item)) return true;
        }
        return false;
    }
}
