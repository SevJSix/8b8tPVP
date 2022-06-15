package me.sevj6.pvp.portals.boiler;

import me.sevj6.pvp.arena.boiler.Arena;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public interface IPortal {

    void onPortalEnter(Arena arena, Player player);

    void randomlySpawnPlayer(Player player, World world, int xRange, int zRange);

    default int genRandomNumber(int min, int max) {
        ThreadLocalRandom rand = ThreadLocalRandom.current();
        return rand.nextInt(min, max);
    }
}
