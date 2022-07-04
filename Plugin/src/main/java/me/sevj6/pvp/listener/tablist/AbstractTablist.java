package me.sevj6.pvp.listener.tablist;

import org.bukkit.entity.Player;

import java.util.List;
public interface AbstractTablist {

    void sendHeaderAndFooterList();

     String parseText(String text, Player player);

     List<Player> getOnlinePlayers();

     int getPlayerPing(Player player);
}
