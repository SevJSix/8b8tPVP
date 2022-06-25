package me.sevj6.pvp.listener;

import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.event.eventposters.*;
import me.sevj6.pvp.listener.listeners.CommandListener;
import me.sevj6.pvp.listener.listeners.DisableActivity;
import me.sevj6.pvp.listener.listeners.Miscellaneous;
import me.sevj6.pvp.listener.listeners.PvPListeners;
import me.sevj6.pvp.listener.listeners.exploits.ArrowVelocityFix;
import me.sevj6.pvp.listener.listeners.exploits.NormalPacketFly;
import me.sevj6.pvp.listener.listeners.exploits.PhaseRelatedPacketFly;
import me.sevj6.pvp.listener.listeners.frames.FrameListener;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class GeneralListenerManager extends Manager {
    public GeneralListenerManager() {
        super("general");
    }

    @Override // listeners and packet listeners that aren't specific to a feature get registered here
    public void init(PVPServer plugin) {
        Bukkit.getPluginManager().registerEvents(plugin.getInteractListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new DisableActivity(), plugin);
        Bukkit.getPluginManager().registerEvents(new Miscellaneous(), plugin);
        Bukkit.getPluginManager().registerEvents(new CommandListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new ArrowVelocityFix(), plugin);
        Bukkit.getPluginManager().registerEvents(new PvPListeners(), plugin);
        Bukkit.getPluginManager().registerEvents(new FrameListener(), plugin);
        plugin.getDispatcher().register(new ListenerArmSwing(), PacketPlayInArmAnimation.class);
        plugin.getDispatcher().register(new ListenerCrystalPlace(), PacketPlayInUseItem.class);
        plugin.getDispatcher().register(new ListenerTotemPop(), PacketPlayOutEntityStatus.class);
        plugin.getDispatcher().register(new ListenerUse32k(), PacketPlayInUseEntity.class);
        plugin.getDispatcher().register(new ListenerItemFrameUse(), PacketPlayInUseEntity.class);
        plugin.getDispatcher().register(new PhaseRelatedPacketFly(), PacketPlayInFlying.class, PacketPlayInFlying.PacketPlayInPosition.class, PacketPlayInFlying.PacketPlayInPositionLook.class);
        plugin.getDispatcher().register(new NormalPacketFly(), PacketPlayInTeleportAccept.class);
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
