package me.sevj6.pvp;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:43 PM
 * This file was created as a part of 8b8tPVP
 */

@RequiredArgsConstructor
@Getter
public abstract class Manager {
    
    @NonNull
    private final String name;

    protected File dataFolder;

    public abstract void init(PVPServer plugin);

    public abstract void destruct(PVPServer plugin);

    public abstract void reloadConfig(ConfigurationSection config);

    public File getDataFolder() {
        if (dataFolder == null) dataFolder = new File(PVPServer.getInstance().getDataFolder(), getName());
        if (!dataFolder.exists()) dataFolder.mkdirs();
        return dataFolder;
    }
}
