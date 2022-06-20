package me.sevj6.pvp.listener;

import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.event.TestListener;
import me.sevj6.pvp.event.eventposters.ListenerArmSwing;
import me.sevj6.pvp.event.eventposters.ListenerCrystalPlace;
import me.sevj6.pvp.event.eventposters.ListenerTotemPop;
import me.sevj6.pvp.event.eventposters.ListenerUse32k;
import me.sevj6.pvp.listener.listeners.CommandListener;
import me.sevj6.pvp.listener.listeners.DisableActivity;
import me.sevj6.pvp.listener.listeners.ExploitFixes;
import me.sevj6.pvp.listener.listeners.Miscellaneous;
import me.sevj6.pvp.listener.tablist.Tablist8b8t;
import net.minecraft.server.v1_12_R1.PacketPlayInArmAnimation;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_12_R1.PacketPlayInUseItem;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class GeneralListenerManager extends Manager {
    public GeneralListenerManager() {
        super("general");
    }

    @Override // listeners and packet listeners that aren't specific to a feature get registered here
    public void init(PVPServer plugin) {
        Bukkit.getPluginManager().registerEvents(plugin.getInteractListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new TestListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DisableActivity(), plugin);
        Bukkit.getPluginManager().registerEvents(new Miscellaneous(), plugin);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ExploitFixes(), plugin);
        plugin.getDispatcher().register(new ListenerArmSwing(), PacketPlayInArmAnimation.class);
        plugin.getDispatcher().register(new ListenerCrystalPlace(), PacketPlayInUseItem.class);
        plugin.getDispatcher().register(new ListenerTotemPop(), PacketPlayOutEntityStatus.class);
        plugin.getDispatcher().register(new ListenerUse32k(), PacketPlayInUseEntity.class);
        new Tablist8b8t(plugin);
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
