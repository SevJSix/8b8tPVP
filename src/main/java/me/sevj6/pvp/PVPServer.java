package me.sevj6.pvp;

import lombok.Getter;
import me.sevj6.pvp.arena.ArenaManager;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.arena.create.InteractListener;
import me.sevj6.pvp.arena.create.command.CreateArena;
import me.sevj6.pvp.arena.create.command.RemoveArena;
import me.sevj6.pvp.arena.create.command.Wand;
import me.sevj6.pvp.command.ArenaList;
import me.sevj6.pvp.command.ClearArenas;
import me.sevj6.pvp.event.TestListener;
import me.sevj6.pvp.event.eventposters.ListenerCrystalPlace;
import me.sevj6.pvp.event.eventposters.ListenerTotemPop;
import me.sevj6.pvp.event.eventposters.ListenerUse32k;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.mechanics.DisableActivity;
import me.sevj6.pvp.tablist.Tablist8b8t;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class PVPServer extends JavaPlugin {

    public static final long START_TIME = System.currentTimeMillis();
    @Getter
    private static PVPServer instance;
    @Getter
    private static ArenaManager arenaManager;
    private PacketEventDispatcher dispatcher;
    private List<Manager> managers;

    @Override
    public void onEnable() {
        instance = this;
        managers = new ArrayList<>();
        saveDefaultConfig();
        dispatcher = new PacketEventDispatcher(this);
        dispatcher.register(new ListenerCrystalPlace(), PacketPlayInUseItem.class);
        dispatcher.register(new ListenerTotemPop(), PacketPlayOutEntityStatus.class);
        dispatcher.register(new ListenerUse32k(), PacketPlayInUseEntity.class);
        InteractListener interactListener = new InteractListener();
        Bukkit.getPluginManager().registerEvents(interactListener, this);
        DisableActivity disableActivity = new DisableActivity();
        dispatcher.register(disableActivity, PacketPlayInBlockDig.class, PacketPlayInUseEntity.class, PacketPlayInBlockPlace.class, PacketPlayInUseItem.class, PacketPlayInVehicleMove.class, PacketPlayInSteerVehicle.class, PacketPlayInUpdateSign.class);
        Bukkit.getPluginManager().registerEvents(new TestListener(), this);
        Bukkit.getPluginManager().registerEvents(disableActivity, this);
        addManager(new KitManager());
        arenaManager = new ArenaManager();
        addManager(arenaManager);
        managers.forEach(m -> m.init(this));
        arenaManager.getArenas().forEach(Arena::loadArenaChunks);
        getCommand("clear").setExecutor(new ClearArenas());
        getCommand("arenalist").setExecutor(new ArenaList());
        getCommand("arenacreate").setExecutor(new CreateArena(interactListener));
        getCommand("arenaremove").setExecutor(new RemoveArena());
        getCommand("wand").setExecutor(new Wand());
        new Tablist8b8t(this);
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void registerListener(Listener listener) {
        if (ClassProcessor.hasAnnotation(listener)) ClassProcessor.process(listener);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @SafeVarargs
    public final void registerListener(PacketListener listener, Class<? extends Packet<?>>... packets) {
        dispatcher.register(listener, packets);
    }

    public void registerCommand(String name, CommandExecutor command) {
        CraftServer cs = (CraftServer) Bukkit.getServer();
        if (ClassProcessor.hasAnnotation(command)) ClassProcessor.process(command);
        cs.getCommandMap().register(name, new org.bukkit.command.Command(name) {
            @Override
            public boolean execute(CommandSender sender, String commandLabel, String[] args) {
                command.onCommand(sender, this, commandLabel, args);
                return true;
            }
        });

    }

    public ConfigurationSection getModuleConfig(Manager manager) {
        return getConfig().getConfigurationSection(manager.getName());
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        getManagers().forEach(m -> {
            ConfigurationSection section = getConfig().getConfigurationSection(m.getName());
            if (section != null) m.reloadConfig(section);
        });
    }
}
