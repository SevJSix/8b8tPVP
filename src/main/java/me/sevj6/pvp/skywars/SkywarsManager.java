package me.sevj6.pvp.skywars;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.skywars.match.Match;
import me.sevj6.pvp.skywars.test.MatchTest;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class SkywarsManager extends Manager {

    @Getter
    private static SkywarsManager instance;

    @Getter
    private Match match;

    public SkywarsManager() {
        super("skywars");
        instance = this;
        match = null;
    }

    public void registerMatch(Match match) {
        this.match = match;
    }

    public boolean isPlayerInMatch(Player player) {
        return match.getPlayers().contains(player);
    }

    @Override
    public void init(PVPServer plugin) {
        plugin.getCommand("matchtest").setExecutor(new MatchTest());
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
