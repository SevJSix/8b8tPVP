package me.sevj6.pvp.kit;

import lombok.Getter;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.kit.util.KitIO;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:50 PM
 * This file was created as a part of 8b8tPVP
 */
@Getter
//TODO make this not chinese
public class Kit {
    private final UUID owner;
    private NBTTagList kitItems;
    private final String name;
    private final KitType type;

    public Kit(Player owner, String name, KitType type) {
        this(owner.getUniqueId(), name, type);
        if (!owner.isOnline()) throw new IllegalArgumentException("Player is not online");
        kitItems = ((CraftPlayer) owner).getHandle().inventory.a(new NBTTagList());
    }

    public Kit(UUID owner, String name, KitType type) {
        this.owner = owner;
        this.name = name;
        this.type = type;
        try {
            NBTTagCompound kitData = KitIO.loadKitData(owner, name, type);
            if (kitData == null) throw new IllegalArgumentException("Kit does not exist");
            kitItems = KitIO.loadKitData(owner, name, type).getList("InvContents", 10);
        } catch (Throwable t) {
            PVPServer.getInstance().getLogger().warning("Failed to load kit " + name + " for " + owner.toString());
            t.printStackTrace();
        }
    }

    public Player getOwnerAsPlayer() {
        return Bukkit.getPlayer(owner);
    }

    //Set the owners inventory to the kit's contents
    public void setOwnerLoadOut() {
        Player player = getOwnerAsPlayer();
        if (player == null || !player.isOnline()) return;
        setLoadOut(player);
    }

    public void setLoadOut(Player player) {
        if (player == null || !player.isOnline()) return;
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.inventory.b(getKitItems());
    }

    public void save() {
        try {
            KitIO.saveKitData(this);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    public enum KitType {
        GLOBAL, USER
    }
}
