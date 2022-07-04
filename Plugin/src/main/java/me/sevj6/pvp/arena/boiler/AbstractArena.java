package me.sevj6.pvp.arena.boiler;

import lombok.Getter;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public abstract class AbstractArena {

    private final String name;
    private final World world;
    private final int x1, y1, z1;
    private final int x2, y2, z2;
    private final BlockPosition firstPosition;
    private final BlockPosition secondPosition;

    protected AbstractArena(String name, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        this.name = name;
        this.world = world;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.x2 = x2;
        this.y2 = y2;
        this.z2 = z2;
        this.firstPosition = new BlockPosition(x1, y1, z1);
        this.secondPosition = new BlockPosition(x2, y2, z2);
    }

    public abstract void clear();

    public abstract List<Player> getActivePlayers();

    public abstract List<Entity> getAllEntities();

    public abstract List<Location> getAllLocations(BlockPosition pos1, BlockPosition pos2);

    public abstract List<Block> findAllBlocks(List<Location> locations);

    public abstract boolean isPlayerInArena(Player player);

    public abstract boolean isPositionInArena(BlockPosition pos);

    public int getMinX() {
        return Math.min(firstPosition.getX(), secondPosition.getX());
    }

    public int getMinY() {
        return Math.min(firstPosition.getY(), secondPosition.getY());
    }

    public int getMinZ() {
        return Math.min(firstPosition.getZ(), secondPosition.getZ());
    }

    public int getMaxX() {
        return Math.max(firstPosition.getX(), secondPosition.getX());
    }

    public int getMaxY() {
        return Math.max(firstPosition.getY(), secondPosition.getY());
    }

    public int getMaxZ() {
        return Math.max(firstPosition.getZ(), secondPosition.getZ());
    }
}
