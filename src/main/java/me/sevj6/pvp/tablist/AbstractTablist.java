package me.sevj6.pvp.tablist;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public abstract class AbstractTablist {

    public abstract void sendHeaderAndFooterList();

    public abstract String parseText(String text, Player player);

    public abstract List<Player> getOnlinePlayers();

    public abstract int getPlayerPing(Player player);
}
