package me.sevj6.pvp.command;

import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.command.commands.Kill;
import me.sevj6.pvp.command.commands.KitCreator;
import org.bukkit.configuration.ConfigurationSection;

public class GeneralCommandManager extends Manager {

    public GeneralCommandManager() {
        super("commands");
    }

    @Override // commands that are not admin commands get registered here
    public void init(PVPServer plugin) {
        plugin.getCommand("kill").setExecutor(new Kill());
        plugin.getCommand("kitcreator").setExecutor(new KitCreator());
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
