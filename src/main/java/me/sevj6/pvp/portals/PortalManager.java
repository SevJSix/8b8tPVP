package me.sevj6.pvp.portals;

import lombok.Getter;
import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.portals.boiler.Portal;
import me.sevj6.pvp.portals.boiler.PortalIO;
import me.sevj6.pvp.portals.command.CreatePortal;
import me.sevj6.pvp.portals.command.RemovePortal;
import me.sevj6.pvp.portals.listener.PlayerMoveListener;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class PortalManager extends Manager {

    @Getter
    private List<Portal> portals;
    private PortalIO portalIO;

    public PortalManager() {
        super("portals");
    }

    public Portal getPortalByName(String name) {
        return portals.stream().filter(portal -> portal.getName().equals(name)).findAny().orElse(null);
    }

    @Override
    public void init(PVPServer plugin) {
        portalIO = new PortalIO();
        portals = portalIO.readAllPortals();
        plugin.getCommand("createportal").setExecutor(new CreatePortal(this, plugin.getInteractListener()));
        plugin.getCommand("removeportal").setExecutor(new RemovePortal(this));
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), plugin);
    }

    @Override
    public void destruct(PVPServer plugin) {
        try {
            Utils.log("Saving all portals...");
            portalIO.savePortal(portals.toArray(new Portal[0]));
            portals.clear();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }


    public void createPortal(String name, World world, BlockPosition bottomCorner, BlockPosition topCorner, Arena exitArena) {
        portals.add(new Portal(name, world, bottomCorner, topCorner, exitArena));
    }

    public void removePortal(String portalName) {
        Portal portal = portals.stream().filter(p -> p.getName().equals(portalName)).findAny().orElseThrow(() -> new NullPointerException("No portal with name " + portalName + " found!"));
        portals.remove(portal);
        portalIO.deletePortal(portal.getName());
    }
}
