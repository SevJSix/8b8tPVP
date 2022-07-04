package me.sevj6.pvp.portals.boiler;

import lombok.Getter;
import me.sevj6.pvp.arena.boiler.Arena;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Arrays;
import java.util.List;

@Getter
public class Portal implements IPortal {

    private final String name;
    private final World world;
    private final BlockPosition bottomCorner;
    private final BlockPosition topCorner;
    private final AxisAlignedBB boundingBox;
    private final Arena exitArena;
    private final List<Material> dontSpawnOn = Arrays.asList(Material.LAVA, Material.WATER, Material.STATIONARY_LAVA, Material.STATIONARY_WATER, Material.CACTUS, Material.FIRE, Material.MAGMA);

    public Portal(String name, World world, BlockPosition bottomCorner, BlockPosition topCorner, Arena exitArena) {
        this.name = name;
        this.world = world;
        this.bottomCorner = bottomCorner;
        this.topCorner = topCorner;
        this.exitArena = exitArena;
        this.boundingBox = new AxisAlignedBB(bottomCorner, topCorner);
    }

    @Override
    public void onPortalEnter(Arena arena, Player player) {
        World world = arena.getWorld();
        randomlySpawnPlayer(player, world, 25, 25);
    }

    @Override
    public void randomlySpawnPlayer(Player player, World world, int xRange, int zRange) {
        Location location = null;
        int attempts = 0;
        while (location == null) {
            attempts++;
            location = world.getHighestBlockAt(genRandomNumber(-xRange, xRange), genRandomNumber(-zRange, zRange)).getLocation();
            if (attempts > 500) break;
            if (dontSpawnOn.contains(location.getBlock().getType()) || dontSpawnOn.contains(location.getBlock().getRelative(BlockFace.DOWN).getType()))
                location = null;
        }
        player.teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }
}
