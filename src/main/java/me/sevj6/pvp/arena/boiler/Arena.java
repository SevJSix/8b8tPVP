package me.sevj6.pvp.arena.boiler;

import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Arena extends AbstractArena {

    private final List<Location> locations = getAllLocations(getFirstPosition(), getSecondPosition());
    protected List<Material> invalidMaterials = Arrays.asList(Material.AIR, Material.BEDROCK, Material.DISPENSER);

    public Arena(String name, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(name, world, x1, y1, z1, x2, y2, z2);
    }

    public Arena(String name, World world, BlockPosition pos1, BlockPosition pos2) {
        this(name, world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }

    public void loadArenaChunks() {
        locations.forEach(location -> {
            if (!location.getChunk().isLoaded()) location.getChunk().load();
        });
    }

    @Override
    public void clear() {
        findAllBlocks(locations).forEach(block -> {
            BlockPosition posAt = new BlockPosition(block.getX(), block.getY(), block.getZ());
            ((CraftWorld) getWorld()).getHandle().setAir(posAt);
        });
    }

    @Override
    public List<Player> getActivePlayers() {
        List<Player> players = new ArrayList<>();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (isPlayerInArena(onlinePlayer)) players.add(onlinePlayer);
        }
        return players;
    }

    @Override
    public List<Location> getAllLocations(BlockPosition pos1, BlockPosition pos2) {
        if (getWorld() == null) throw new NullPointerException("World in arena '" + getName() + "' is null!");
        List<Location> locations = new ArrayList<>();
        for (int x = getMinX(); x <= getMaxX(); x++) {
            for (int y = getMinY(); y <= getMaxY(); y++) {
                for (int z = getMinZ(); z <= getMaxZ(); z++) {
                    Location location = new Location(getWorld(), x, y, z);
                    locations.add(location);
                }
            }
        }
        return locations;
    }

    @Override
    public List<Block> findAllBlocks(List<Location> locations) {
        return locations.stream().map(Location::getBlock).collect(Collectors.toList());
    }

    @Override
    public boolean isPlayerInArena(Player player) {
        if (getWorld() == null) throw new NullPointerException("World in arena '" + getName() + "' is null!");
        if (getWorld() != player.getWorld()) return false;
        for (int x = getMinX(); x <= getMaxX(); x++) {
            for (int y = getMinY(); y <= getMaxY(); y++) {
                for (int z = getMinZ(); z <= getMaxZ(); z++) {
                    if (player.getLocation().getBlock().getLocation().equals(getWorld().getBlockAt(new Location(getWorld(), x, y, z)).getLocation()))
                        return true;
                }
            }
        }
        return false;
    }
}
