package me.sevj6.pvp.arena.boiler;

import lombok.Getter;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.ArenaManager;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArenaIO {

    @Getter
    private final File dataFolder;
    ArenaManager manager = PVPServer.getArenaManager();

    public ArenaIO() {
        dataFolder = new File(PVPServer.getInstance().getDataFolder(), "Arenas");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public Arena readArena(String name) throws Throwable {
        File arenaFile = new File(dataFolder, name + ".arena");
        if (!arenaFile.exists()) throw new IOException("No arena with the name " + name + " could be found!");
        return parseArena(arenaFile);
    }

    public List<Arena> readAllArenas() {
        return Arrays.stream(Objects.requireNonNull(dataFolder.listFiles())).map(this::parseArena).collect(Collectors.toList());
    }

    public void saveArena(Arena... arenas) throws Throwable {
        for (Arena arena : arenas) {
            File file = new File(dataFolder, arena.getName() + ".arena");
            if (!file.exists()) file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            DataOutputStream out = new DataOutputStream(fos);
            out.writeUTF(arena.getWorld().getName());
            out.writeInt(arena.getX1());
            out.writeInt(arena.getY1());
            out.writeInt(arena.getZ1());
            out.writeInt(arena.getX2());
            out.writeInt(arena.getY2());
            out.writeInt(arena.getZ2());
            out.flush();
            out.close();
            fos.close();
        }
    }

    private Arena parseArena(File file) {
        try {
            String name = file.getName().split("\\.")[0];
            FileInputStream fis = new FileInputStream(name);
            DataInputStream in = new DataInputStream(fis);
            Arena arena = new Arena(name, Bukkit.getWorld(in.readUTF()),
                    new BlockPosition(in.readInt(), in.readInt(), in.readInt()),
                    new BlockPosition(in.readInt(), in.readInt(), in.readInt()));
            if (in.available() > 0) throw new IOException("Arena file had more data than accepted: " + file.getName());
            in.close();
            fis.close();
            return arena;
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
    public void deleteArena(String name) {
        File file = new File(dataFolder, name + ".arena");
        if (file.exists()) file.delete();
    }
    //A method to delete all arenas
    public void deleteAllArenas() {
        for (File file : Objects.requireNonNull(dataFolder.listFiles())) {
            file.delete();
        }
    }
}
