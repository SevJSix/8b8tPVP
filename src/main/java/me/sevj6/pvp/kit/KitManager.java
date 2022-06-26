package me.sevj6.pvp.kit;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.kit.commands.CreateGKitCommand;
import me.sevj6.pvp.kit.commands.CreateUKitCommand;
import me.sevj6.pvp.kit.commands.KitCommand;
import me.sevj6.pvp.kit.listener.InventoryClick;
import me.sevj6.pvp.kit.listener.LoadUserKitsListener;
import me.sevj6.pvp.kit.util.KitIO;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:46 PM
 * This file was created as a part of 8b8tPVP
 */
@Getter
public class KitManager extends Manager {
    @Getter
    private static File managerDataFolder;
    private List<Kit> globalKits;
    private HashMap<UUID, List<Kit>> userKits;

    public KitManager() {
        super("Kits");
    }

    @Override
    public void init(PVPServer plugin) {
        globalKits = new ArrayList<>();
        userKits = new HashMap<>();
        managerDataFolder = super.getDataFolder();
        plugin.getServer().getPluginManager().registerEvents(new LoadUserKitsListener(this), plugin);
        plugin.getServer().getPluginManager().registerEvents(new InventoryClick(this), plugin);
        plugin.getCommand("creategkit").setExecutor(new CreateGKitCommand(this));
        plugin.getCommand("createukit").setExecutor(new CreateUKitCommand(this));
        plugin.getCommand("kit").setExecutor(new KitCommand(this));
        try {
            loadAllGlobalKits();
        } catch (Throwable t) {
            Utils.log("&cFailed to load the global kits please see the stacktrace below for more info");
            t.printStackTrace();
        }
    }

    public List<Kit> getKits(Player player) {
        return userKits.get(player.getUniqueId());
    }

    public void loadUserKits(Player player) throws Throwable {
        UUID uuid = player.getUniqueId();
        Utils.log("&aLoading user kits from &3" + player.getName());
        File uuidDataFolder = new File(KitIO.getKitDataFolder(), String.valueOf(uuid));
        if (!uuidDataFolder.exists()) uuidDataFolder.mkdirs();
        for (File file : Arrays.stream(uuidDataFolder.listFiles()).filter(File::isFile).filter(f -> f.getName().endsWith(".kit")).collect(Collectors.toList())) {
            String name = file.getName().replace(".kit", "");
            NBTTagCompound compound = KitIO.loadKitData(file);
            if (compound == null) throw new IOException("Failed to read kit data properly");
            Kit kit = new Kit(player, name);
            kit.setKitItems(compound.getList("InvContents", 10), false);
            registerKit(kit);
            Utils.log("&3User kit&r&a " + kit.getName() + "&r&3 loaded successfully!");
        }
    }

    private void loadAllGlobalKits() throws Throwable {
        Utils.log("&aLoading all global kits into memory!");
        for (File file : Arrays.stream(KitIO.getKitDataFolder().listFiles()).filter(File::isFile).filter(f -> f.getName().endsWith(".kit")).collect(Collectors.toList())) {
            String name = file.getName().replace(".kit", "");
            NBTTagCompound compound = KitIO.loadKitData(file);
            if (compound == null) throw new IOException("Failed to read kit data properly");
            Kit kit = new Kit(null, name);
            kit.setKitItems(compound.getList("InvContents", 10), false);
            registerKit(kit);
            Utils.log("&3Global kit&r&a " + kit.getName() + "&r&3 loaded successfully!");
        }
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }

    /**
     * @param name the name of the kit to check
     * @return true if the kit exists false otherwise
     */
    public boolean isKitRegistered(String name, UUID owner) {
        if (owner == null) return globalKits.stream().map(Kit::getName).anyMatch(n -> n.equals(name));
        if (!userKits.containsKey(owner)) return false;
        return userKits.get(owner).stream().map(Kit::getName).anyMatch(n -> n.equals(name));
    }

    /**
     * @param kit the kit to register to the list
     * @return true if registration was successful false otherwise
     */
    public boolean registerKit(Kit kit) {
        if (isKitRegistered(kit.getName(), ((kit.isGlobalKit()) ? null : kit.getOwner().getUniqueId()))) return false;
        if (kit.isGlobalKit()) {
            globalKits.add(kit);
            System.out.println("Registering global kit");
        } else {
            System.out.println("Registering user kit to " + kit.getOwner().getName());
            if (userKits.containsKey(kit.getOwner().getUniqueId())) {
                userKits.get(kit.getOwner().getUniqueId()).add(kit);
            } else userKits.put(kit.getOwner().getUniqueId(), new ArrayList<>(Collections.singletonList(kit)));
        }
        return true;
    }

    public Kit getKitByName(String name) {
        return null;
    }
}
