package me.sevj6.pvp.arena;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

public class ArenaManager extends Manager {

    @Getter
    private final List<Arena> arenas;

    public ArenaManager() {
        super("ArenaManager");
        arenas = new ArrayList<>();
    }

    @Override
    public void init(PVPServer plugin) {
        Arena crystal = new Arena("Crystal", Bukkit.getWorld("world"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50));
        Arena dispenser32k = new Arena("Dispenser32k", Bukkit.getWorld("world_nether"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50));
        arenas.add(crystal);
        arenas.add(dispenser32k);
    }

    public Arena getArenaByName(String name) {
        for (Arena arena : arenas) {
            if (arena.getName().toLowerCase().equals(name.toLowerCase())) return arena;
        }
        return null;
    }

    @Override
    public void destruct(PVPServer plugin) {
        arenas.clear();
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
    }
}
