package me.sevj6.pvp;

import lombok.Getter;
import me.txmc.protocolapi.PacketEventDispatcher;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class PVPServer extends JavaPlugin {

    @Getter
    public static final long START_TIME = System.currentTimeMillis();
    @Getter
    private PacketEventDispatcher dispatcher;
    @Getter
    private List<Manager> managers;

    public static PVPServer getInstance() {
        return getPlugin(PVPServer.class);
    }

    public void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onEnable() {
        dispatcher = new PacketEventDispatcher(this);
        managers = new ArrayList<>();
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
