package me.sevj6.pvp;

import lombok.Getter;
import me.sevj6.pvp.arena.ArenaManager;
import me.sevj6.pvp.command.GeneralCommandManager;
import me.sevj6.pvp.kit.KitManager;
import me.sevj6.pvp.listener.GeneralListenerManager;
import me.sevj6.pvp.listener.listeners.InteractListener;
import me.sevj6.pvp.listener.tablist.Sorter;
import me.sevj6.pvp.listener.tablist.Tablist8b8t;
import me.sevj6.pvp.portals.PortalManager;
import me.sevj6.pvp.util.ViolationManager;
import me.txmc.protocolapi.PacketEventDispatcher;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.ClassProcessor;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Getter
public final class PVPServer extends JavaPlugin {

    public static final long START_TIME = System.currentTimeMillis();

    @Getter
    public static ArenaManager arenaManager;

    @Getter
    private static PVPServer instance;

    private Tablist8b8t tablist;
    private Sorter sorter;
    private InteractListener interactListener;
    private PacketEventDispatcher dispatcher;
    private List<Manager> managers;
    private Location spawn;
    private Location kitCreator;
    private List<ViolationManager> violationManagers;
    private ScheduledExecutorService service;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        instance = this;
        managers = new ArrayList<>();
        dispatcher = new PacketEventDispatcher(this);
        interactListener = new InteractListener();
        arenaManager = new ArenaManager();
        kitManager = new KitManager();
        sorter = new Sorter();
        tablist = new Tablist8b8t(this);
        violationManagers = new ArrayList<>();
        addManager(arenaManager);
        addManager(kitManager);
        addManager(new PortalManager());
        addManager(new GeneralListenerManager());
        addManager(new GeneralCommandManager());
        managers.forEach(m -> m.init(this));
        service = Executors.newScheduledThreadPool(4);
        service.scheduleAtFixedRate(() -> violationManagers.forEach(ViolationManager::decrementAll), 0, 1, TimeUnit.SECONDS);

        kitCreator = new Location(
                Bukkit.getWorld(PVPServer.getInstance().getConfig().getString("KitCreator.world")),
                PVPServer.getInstance().getConfig().getDouble("KitCreator.x"),
                PVPServer.getInstance().getConfig().getDouble("KitCreator.y"),
                PVPServer.getInstance().getConfig().getDouble("KitCreator.z"),
                (float) PVPServer.getInstance().getConfig().getDouble("KitCreator.yaw"),
                (float) PVPServer.getInstance().getConfig().getDouble("KitCreator.pitch")
        );
        spawn = new Location(
                Bukkit.getWorld(PVPServer.getInstance().getConfig().getString("Hub.world")),
                PVPServer.getInstance().getConfig().getDouble("Hub.x"),
                PVPServer.getInstance().getConfig().getDouble("Hub.y"),
                PVPServer.getInstance().getConfig().getDouble("Hub.z"),
                (float) PVPServer.getInstance().getConfig().getDouble("Hub.yaw"),
                (float) PVPServer.getInstance().getConfig().getDouble("Hub.pitch")
        );
        dispatchCommand("gamerule doFireTick false");
        dispatchCommand("gamerule announceAdvancements false");
        dispatchCommand("gamerule mobGriefing false");
        dispatchCommand("gamerule doDaylightCycle false");
        dispatchCommand("gamerule doWeatherCycle false");
        dispatchCommand("gamerule commandBlockOutput false");
    }

    private void dispatchCommand(String command) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public void addManager(Manager manager) {
        managers.add(manager);
    }

    @Override
    public void onDisable() {
        managers.forEach(m -> m.destruct(this));
    }

    public void registerListener(Listener listener) {
        if (ClassProcessor.hasAnnotation(listener)) ClassProcessor.process(listener);
        getServer().getPluginManager().registerEvents(listener, this);
    }

    public void registerViolationManager(ViolationManager manager) {
        if (violationManagers.contains(manager)) return;
        violationManagers.add(manager);
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
