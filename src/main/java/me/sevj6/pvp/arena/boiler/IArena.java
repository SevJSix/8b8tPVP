package me.sevj6.pvp.arena.boiler;

import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.World;

public interface IArena {

    void constructArena(String arenaName, World world, BlockPosition pos1, BlockPosition pos2);

    void destructArena(String arenaName);
}
