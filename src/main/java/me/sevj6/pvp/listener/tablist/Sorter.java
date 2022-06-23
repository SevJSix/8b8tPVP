package me.sevj6.pvp.listener.tablist;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

@Getter
public class Sorter {

    private final Scoreboard scoreboard;
    private final Team hubTeam;
    private final Team netherTeam;
    private final Team kitCreatorTeam;
    private final Team noCrystalTeam;
    private final String hubPrefix = "[&2hub&r] ";
    private final String netherPrefix = "[&cnether&r] ";
    private final String kitCreatorPrefix = "[&5kitcreator&r] ";
    private final String noCrystalPrefix = "[&3nocrystal&r] ";

    public Sorter() {
        this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.hubTeam = scoreboard.registerNewTeam("overworld");
        this.netherTeam = scoreboard.registerNewTeam("nether");
        this.kitCreatorTeam = scoreboard.registerNewTeam("end");
        this.noCrystalTeam = scoreboard.registerNewTeam("nocrystal");
        hubTeam.setPrefix(parse("[&2hub&r]"));
        netherTeam.setPrefix(parse("[&cnether&r]"));
        kitCreatorTeam.setPrefix(parse("[&5kitcreator&r]"));
        noCrystalTeam.setPrefix(parse("[&3nocrystal&r]"));
    }

    public void set(Player player, Team team) {
        Team current = getTeam(player);
        if (current != null) removePlayer(player, current);
        addPlayer(player, team);
    }

    public void addPlayer(Player player, Team team) {
        team.addPlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public void removePlayer(Player player, Team team) {
        team.removePlayer(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public Team getTeam(Player player) {
        return scoreboard.getPlayerTeam(Bukkit.getOfflinePlayer(player.getUniqueId()));
    }

    public String parse(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
