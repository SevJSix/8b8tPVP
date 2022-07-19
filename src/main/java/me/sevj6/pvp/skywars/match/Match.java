package me.sevj6.pvp.skywars.match;

import com.destroystokyo.paper.Title;
import lombok.Getter;
import lombok.Setter;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.util.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Getter
public class Match {

    private final List<Player> players;
    private final List<Location> spawnLocations;
    private final List<Block> glassBlocks;
    private final Arena arena;
    @Setter
    private boolean inProgress;

    public Match(List<Location> spawnLocations, Arena arena) {
        this.players = new ArrayList<>();
        this.spawnLocations = spawnLocations;
        this.arena = arena;
        this.glassBlocks = new ArrayList<>();
        inProgress = false;
    }

    public void queuePlayer(Player player) {
        if (players.size() >= 15) return;
        players.add(player);
        Utils.sendMessage(player, "&aYou have joined the queue.");
        players.forEach(p -> Utils.sendMessage(p, "&3" + player.getName() + " has joined the queue! " + "&e" + players.size() + "/15"));
    }

    public void genGlass() {
        for (Location spawnLocation : spawnLocations) {
            Block block = spawnLocation.getBlock();
            glassBlocks.add(block.getRelative(0, -1, 0));
            glassBlocks.add(block.getRelative(1, 0, 0));
            glassBlocks.add(block.getRelative(-1, 0, 0));
            glassBlocks.add(block.getRelative(0, 0, 1));
            glassBlocks.add(block.getRelative(0, 0, -1));
            glassBlocks.add(block.getRelative(1, 1, 0));
            glassBlocks.add(block.getRelative(-1, 1, 0));
            glassBlocks.add(block.getRelative(0, 1, 1));
            glassBlocks.add(block.getRelative(0, 1, -1));
            glassBlocks.add(block.getRelative(0, 2, 0));
        }
        glassBlocks.forEach(block -> {
            if (!block.getChunk().isLoaded()) block.getChunk().load();
            block.setType(Material.GLASS);
        });
    }

    public void deleteGlass() {
        glassBlocks.forEach(block -> {
            if (!block.getChunk().isLoaded()) block.getChunk().load();
            block.setType(Material.AIR);
        });
    }

    public void start() {
        for (int i = 0; i < spawnLocations.size(); i++) {
            try {
                if (players.get(i) == null) return;
                Player player = players.get(i);
                Location location = spawnLocations.get(i);
                player.teleport(location);
            } catch (Throwable ignored) {
            }
        }
        new Timer().scheduleAtFixedRate(new TimerTask() {
            int countDown = 5;

            @Override
            public void run() {
                players.forEach(player -> {
                    if (countDown == 0) {
                        player.sendTitle(new Title(ChatColor.RED + "GO!", "", 0, 25, 5));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 5.0F);
                    } else {
                        player.sendTitle(new Title(ChatColor.YELLOW + String.valueOf(countDown)));
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 10.0F, 1.0F);
                    }
                });
                if (countDown == 0) {
                    this.cancel();
                    // delete blocks in bukkit's main thread
                    Utils.run(() -> deleteGlass());
                    inProgress = true;
                } else {
                    countDown--;
                }
            }
        }, 400L, 1000L);
    }
}
