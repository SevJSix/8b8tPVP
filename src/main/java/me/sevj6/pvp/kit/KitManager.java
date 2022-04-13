package me.sevj6.pvp.kit;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:46 PM
 * This file was created as a part of 8b8tPVP
 */
@Getter
public class KitManager extends Manager {
    private List<Kit> kits;
    @Getter
    private static File managerDataFolder;
    public KitManager() {
        super("Kits");
    }

    @Override
    public void init(PVPServer plugin) {
        kits = new ArrayList<>();
        managerDataFolder = super.getDataFolder();
        plugin.registerCommand("kst", new KitSaveTest(this));
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
