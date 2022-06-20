package me.sevj6.pvp.portals.boiler;

import lombok.Getter;
import me.sevj6.pvp.arena.boiler.Arena;
import net.minecraft.server.v1_12_R1.AxisAlignedBB;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

@Getter
public class Portal implements IPortal {

    private final String name;
    private final World world;
    private final BlockPosition bottomCorner;
    private final BlockPosition topCorner;
    private final AxisAlignedBB boundingBox;
    private final Arena exitArena;

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
        int x = genRandomNumber(-xRange, xRange), z = genRandomNumber(-zRange, zRange), y = 0;
        Location exitLocation = new Location(world, x, y, z, player.getLocation().getYaw(), player.getLocation().getPitch());
        exitLocation.setY(world.getHighestBlockYAt(exitLocation));
        player.teleport(exitLocation);
//        Location exitLoc = null;
//        int attempts = 0;
//        while (exitLoc == null) {
//            attempts++;
//            exitLoc = new Location(world, genRandomNumber(-xRange, xRange), 0, genRandomNumber(-zRange, zRange), player.getLocation().getYaw(), player.getLocation().getPitch());
//            exitLoc.setY(world.getHighestBlockYAt(exitLoc));
//            if (attempts >= 300) return;
//            if ((!exitLoc.getNearbyPlayers(10).isEmpty() || exitLoc.getNearbyEntitiesByType(EnderCrystal.class, 5).size() > 0))
//                exitLoc = null;
//        }
//        player.teleport(exitLoc);
    }
}
