package me.sevj6.pvp.arena.boiler;

import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Arena extends AbstractArena {

    protected List<Material> invalidMaterials = Arrays.asList(Material.AIR, Material.BEDROCK);

    public Arena(String name, World world, int x1, int y1, int z1, int x2, int y2, int z2) {
        super(name, world, x1, y1, z1, x2, y2, z2);
    }

    public Arena(String name, World world, BlockPosition pos1, BlockPosition pos2) {
        this(name, world, pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
    }

    public void loadArenaChunks() {
        getAllLocations(getFirstPosition(), getSecondPosition()).forEach(location -> {
            if (!location.getChunk().isLoaded()) location.getChunk().load();
        });
    }

    @Override
    public void clear() {
        long start = System.currentTimeMillis();
        findAllBlocks(getAllLocations(getFirstPosition(), getSecondPosition())).forEach(block -> {
            block.setType(Material.AIR);
        });
        long finish = (System.currentTimeMillis() - start);
        Bukkit.getOnlinePlayers().stream().filter(this::isPlayerInArena).forEach(p -> Utils.sendMessage(p, String.format("&aFinished clearing your current arena in&r&3 %dms&r", finish)));
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
                    if (invalidMaterials.contains(location.getWorld().getBlockAt(location).getType())) continue;
                    locations.add(location);
                }
            }
        }
        return locations;
    }

    @Override
    public List<Block> findAllBlocks(List<Location> locations) {
        return getAllLocations(getFirstPosition(), getSecondPosition()).stream().map(Location::getBlock).collect(Collectors.toList());
    }

    @Override
    public boolean isPlayerInArena(Player player) {
        if (getWorld() == null) throw new NullPointerException("World in arena '" + getName() + "' is null!");
        if (getWorld() != player.getWorld()) return false;
        Location playerLoc = player.getLocation();
        AxisAlignedBB playerBox = new AxisAlignedBB(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getX(), playerLoc.getY() + 1, playerLoc.getZ());
        AxisAlignedBB arenaBox = new AxisAlignedBB(getFirstPosition(), getSecondPosition());
        return arenaBox.intersects(playerBox);
    }

    @Override
    public boolean isPositionInArena(BlockPosition pos) {
        if (getWorld() == null) throw new NullPointerException("World in arena '" + getName() + "' is null!");
        AxisAlignedBB posBox = new AxisAlignedBB(pos);
        AxisAlignedBB arenaBox = new AxisAlignedBB(getFirstPosition(), getSecondPosition());
        return arenaBox.intersects(posBox);
    }
}
