package me.sevj6.pvp.kit.util;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.kit.Kit;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import java.io.*;
import java.util.UUID;

/**
 * @author 254n_m
 * @since 4/13/22/ 12:09 AM
 * This file was created as a part of 8b8tPVP
 */
public class KitIO {
    private static final File kitDataFolder;

    static {
        kitDataFolder = new File(PVPServer.getInstance().getDataFolder(), "Kits");
        if (!kitDataFolder.exists()) kitDataFolder.mkdirs();
    }

    public static void saveKitData(Kit kit) throws Throwable {
        File kitFile;
        if (kit.getType() == Kit.KitType.GLOBAL) {
            kitFile = new File(kitDataFolder, kit.getName().concat(".kit"));
        } else kitFile = new File(kitDataFolder, kit.getOwner() + "/" + kit.getName().concat(".kit"));
        if (!kitFile.exists()) kitFile.createNewFile();
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("Name", kit.getName());
        compound.setString("Type", kit.getType().name());
//        ((CraftInventoryPlayer) inventory).getInventory().a(invContents);
        compound.set("InvContents", kit.getKitItems());
        FileOutputStream fos = new FileOutputStream(kitFile);
        DataOutputStream out = new DataOutputStream(fos);
        NBTCompressedStreamTools.writeNBT(compound, out);
        out.flush();
        out.close();
        fos.close();
    }

    public static NBTTagCompound loadKitData(UUID owner, String name, Kit.KitType type) throws Throwable {
        File kitFile;
        if (type == Kit.KitType.GLOBAL) {
            kitFile = new File(kitDataFolder, name.concat(".kit"));
        } else kitFile = new File(kitDataFolder, owner + "/" + name.concat(".kit"));
        if (!kitFile.exists()) return null;
        FileInputStream fis = new FileInputStream(kitFile);
        DataInputStream in = new DataInputStream(fis);
        NBTTagCompound compound = NBTCompressedStreamTools.readNBT(in);
        in.close();
        fis.close();
        return compound;
    }
}
