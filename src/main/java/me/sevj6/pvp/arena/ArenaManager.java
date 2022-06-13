package me.sevj6.pvp.arena;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.arena.boiler.ArenaIO;
import me.sevj6.pvp.arena.boiler.ArenaWrapper;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class ArenaManager extends Manager implements ArenaWrapper {

    @Getter
    private final List<Arena> arenas;
    private final ArenaIO arenaIO;

    public ArenaManager() {
        super("ArenaManager");
        arenaIO = new ArenaIO();
        arenas = arenaIO.readAllArenas();
    }

    public Arena getArenaByName(String name) {
        return arenas.stream().filter(arena -> arena.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public void init(PVPServer plugin) {
        arenas.add(new Arena("Dispenser32k", Bukkit.getWorld("world_nether"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50)));
        arenas.add(new Arena("Crystal", Bukkit.getWorld("world"), new BlockPosition(-50, 50, -50), new BlockPosition(50, 0, 50)));
        plugin.getCommand("arenatest").setExecutor(new TestCommand(arenas.get(1)));
    }

    @Override
    public void destruct(PVPServer plugin) {
        try {
            Utils.log("Saving all arenas...");
            arenaIO.saveArena(arenas.toArray(new Arena[0]));
            arenas.clear();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {
    }

    @Override
    public void constructArena(String arenaName, World world, BlockPosition pos1, BlockPosition pos2) {
        arenas.add(new Arena(arenaName, world, pos1, pos2));
    }

    @Override
    public void destructArena(String arenaName) {
        Arena arena = arenas.stream().filter(a -> a.getName().equals(arenaName)).findAny().orElseThrow(() -> new NullPointerException("No arena with name " + arenaName + " found!"));
        arenas.remove(arena);
        arenaIO.deleteArena(arena.getName());
    }
}
