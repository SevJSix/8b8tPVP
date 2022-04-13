package me.sevj6.pvp.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.txmc.protocolapi.reflection.ClassProcessor;
import me.txmc.protocolapi.reflection.GetMethod;
import net.minecraft.server.v1_12_R1.Item;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:50 PM
 * This file was created as a part of 8b8tPVP
 */
@RequiredArgsConstructor
@Getter
public class Kit {
    private final UUID owner;
    private final PlayerInventory inventory;
    private final String name;
    @GetMethod(clazz = NBTTagCompound.class, name = "write", sig = DataOutput.class)
    private Method writeM;

    public void save() {
        try {
            File playerFolder = new File(KitManager.getManagerDataFolder(), owner.toString());
            if (!playerFolder.exists()) playerFolder.mkdirs();
            File kitFile = new File(playerFolder, String.format("%s.kit", name));
            FileOutputStream fos = new FileOutputStream(kitFile);
            DataOutputStream out = new DataOutputStream(fos);
            out.writeUTF(owner.toString());
            out.writeUTF(name);
            writeItemStack(out, inventory.getItemInMainHand());
            out.flush();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void writeItemStack(DataOutputStream out, ItemStack bItem) throws Throwable {
        net.minecraft.server.v1_12_R1.ItemStack itemStack = CraftItemStack.asNMSCopy(bItem);
        if (!itemStack.isEmpty() && itemStack.getItem() != null) {
            out.writeShort(Item.getId(itemStack.getItem()));
            out.writeByte(itemStack.getCount());
            out.writeShort(itemStack.getData());
            NBTTagCompound tag = null;
            if (itemStack.getItem().usesDurability() || itemStack.getItem().p()) {
                itemStack = itemStack.cloneItemStack();
                CraftItemStack.setItemMeta(itemStack, CraftItemStack.getItemMeta(itemStack));
                tag = itemStack.getTag();
            }

            writeNBTTagCompound(tag, out);
        } else {
            out.writeShort(-1);
        }
    }

    private void writeNBTTagCompound(NBTTagCompound tag, DataOutputStream dos) throws Throwable {
        if (writeM == null) ClassProcessor.process(this);
        if (tag == null) {
            dos.writeByte(0);
            return;
        }
        dos.writeByte(tag.getTypeId());
        if (tag.getTypeId() != 0) {
            dos.writeUTF("");
            writeM.invoke(tag, dos);
        }
    }
}
