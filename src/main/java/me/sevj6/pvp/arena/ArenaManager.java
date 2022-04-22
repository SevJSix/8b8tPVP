package me.sevj6.pvp.arena;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
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
        arenas.add(new Arena("Dispenser32k", Bukkit.getWorld("world_nether"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50)));
        arenas.add(new Arena("Crystal", Bukkit.getWorld("world"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50)));
        plugin.getCommand("arenatest").setExecutor(new TestCommand(arenas.get(1)));
    }

    @Override
    public void destruct(PVPServer plugin) {
        arenas.clear();
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
    }
}
