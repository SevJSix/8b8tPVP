package me.sevj6.pvp.kit;

import lombok.Getter;
import me.sevj6.pvp.kit.util.KitIO;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author 254n_m
 * @since 4/12/22/ 11:50 PM
 * This file was created as a part of 8b8tPVP
 */
@Getter
//TODO make this not chinese
public class Kit {
    private final Player owner;
    private final String name;
    private NBTTagList kitItems;

    public Kit(@Nullable Player owner, String name) {
        this.owner = owner;
        this.name = name;
        load((owner == null) ? null : owner.getUniqueId());
    }

    public void setKitItems(NBTTagList kitItems, boolean save) {
        this.kitItems = kitItems;
        if (save) {
            try {
                KitIO.saveKitData(this);
            } catch (Throwable t) {
                Utils.log("&cFailed to save kit to disk! Please see the stacktrace below for more info");
                t.printStackTrace();
            }
        }
    }

    public boolean delete() throws Throwable {
        return KitIO.deleteKit(this);
    }

    //Set the owners inventory to the kit's contents
    public void setOwnerLoadOut() {
        if (owner == null || !owner.isOnline()) return;
        setLoadOut(owner);
    }

    public void setLoadOut(Player player) {
        if (player == null || !player.isOnline()) return;
        EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.inventory.b(getKitItems());
    }

    public void load(UUID id) {
        try {
            NBTTagCompound kitData = KitIO.loadKitData(id, getName());
            if (kitData == null) {
                kitItems = null;
                return;
            }
            kitItems = kitData.getList("InvContents", 10);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public boolean isGlobalKit() {
        return owner == null;
    }
}
