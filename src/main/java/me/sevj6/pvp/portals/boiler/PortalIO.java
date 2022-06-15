package me.sevj6.pvp.portals.boiler;

import lombok.Getter;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class PortalIO {

    @Getter
    private final File dataFolder;

    public PortalIO() {
        dataFolder = new File(PVPServer.getInstance().getDataFolder(), "Portals");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public Portal readArena(String name) throws Throwable {
        File portalFile = new File(dataFolder, name + ".portal");
        if (!portalFile.exists()) throw new IOException("No portal with the name " + name + " could be found!");
        return parsePortal(portalFile);
    }

    public List<Portal> readAllPortals() {
        return Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).map(this::parsePortal).collect(Collectors.toList());
    }

    public void savePortal(Portal... portals) throws Throwable {
        for (Portal portal : portals) {
            File file = new File(dataFolder, portal.getName() + ".arena");
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream out = new DataOutputStream(fos);
            out.writeUTF(portal.getWorld().getName());
            out.writeInt(portal.getBottomCorner().getX());
            out.writeInt(portal.getBottomCorner().getY());
            out.writeInt(portal.getBottomCorner().getZ());
            out.writeInt(portal.getTopCorner().getX());
            out.writeInt(portal.getTopCorner().getY());
            out.writeInt(portal.getTopCorner().getZ());
            Arena arena = portal.getExitArena();
            out.writeUTF(arena.getName());
            out.flush();
            out.close();
            fos.close();
        }
    }

    private Portal parsePortal(File file) {
        try {
            String name = file.getName().split("\\.")[0];
            FileInputStream fis = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fis);
            Portal portal = new Portal(name, Bukkit.getWorld(in.readUTF()),
                    new BlockPosition(in.readInt(), in.readInt(), in.readInt()),
                    new BlockPosition(in.readInt(), in.readInt(), in.readInt()), PVPServer.getArenaManager().getArenaByName(in.readUTF()));
            if (in.available() > 0) throw new IOException("Portal file had more data than accepted: " + file.getName());
            in.close();
            fis.close();
            return portal;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }

    public void deletePortal(String name) {
        File file = new File(dataFolder, name + ".portal");
        if (file.exists()) file.delete();
    }
}
