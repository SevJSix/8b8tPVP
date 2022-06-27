package me.sevj6.pvp.kit.util;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.kit.Kit;
import net.minecraft.server.v1_12_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import javax.annotation.Nullable;
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

    public static File getKitDataFolder() {
        return kitDataFolder;
    }

    public static boolean deleteKit(Kit kit) throws Throwable {
        File kitFile;
        if (kit.getOwner() == null) {
            kitFile = new File(kitDataFolder, kit.getName().concat(".kit"));
        } else {
            File ownerDir = new File(kitDataFolder, String.valueOf(kit.getOwner().getUniqueId()));
            if (!ownerDir.exists()) ownerDir.mkdirs();
            kitFile = new File(ownerDir, kit.getName().concat(".kit"));
        }
        if (!kitFile.exists()) {
            return false;
        } else {
            kitFile.delete();
            return true;
        }
    }

    public static void saveKitData(Kit kit) throws Throwable {
        File kitFile;
        if (kit.getOwner() == null) { //Save a global kit
            kitFile = new File(kitDataFolder, kit.getName().concat(".kit"));
        } else {
            File ownerDir = new File(kitDataFolder, String.valueOf(kit.getOwner().getUniqueId()));
            if (!ownerDir.exists()) ownerDir.mkdirs();
            kitFile = new File(ownerDir, kit.getName().concat(".kit"));
        }
        if (!kitFile.exists()) kitFile.createNewFile();
        NBTTagCompound compound = new NBTTagCompound();
        compound.set("InvContents", kit.getKitItems());
        FileOutputStream fos = new FileOutputStream(kitFile);
        DataOutputStream out = new DataOutputStream(fos);
        NBTCompressedStreamTools.writeNBT(compound, out);
        out.flush();
        out.close();
        fos.close();
    }

    /**
     * Loads kit data
     *
     * @param owner if null the kit is considered a global kit otherwise user
     * @param name  The name if the kit
     * @return The kit's data in NBT form
     * @throws Throwable Thrown if the server process does not have sufficient permissions to read the kit file
     */
    public static NBTTagCompound loadKitData(@Nullable UUID owner, String name) throws Throwable {
        File kitFile;
        if (owner == null) { //Load a global kit
            kitFile = new File(kitDataFolder, name.concat(".kit"));
        } else kitFile = new File(kitDataFolder, owner + "/" + name.concat(".kit"));
        if (!kitFile.exists()) return null;
        return loadKitData(kitFile);
    }

    public static NBTTagCompound loadKitData(File file) throws Throwable {
        FileInputStream fis = new FileInputStream(file);
        DataInputStream in = new DataInputStream(fis);
        NBTTagCompound compound = NBTCompressedStreamTools.readNBT(in);
        in.close();
        fis.close();
        return compound;
    }
}
