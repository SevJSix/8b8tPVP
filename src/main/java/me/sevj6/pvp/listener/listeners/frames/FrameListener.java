package me.sevj6.pvp.listener.listeners.frames;

import me.sevj6.pvp.event.PlayerPlaceItemInItemFrameEvent;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftItemFrame;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class FrameListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        Player player = event.getPlayer();
        if (Utils.isPlayerInArena(player)) return;
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        if (frame.getItem() == null) return;
        frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
        FrameInventory frameInventory = new FrameInventory("&18b&98t &6PvP&r", frame.getItem());
        frameInventory.open(player);
    }

    @EventHandler
    public void onHangingPlaceEvent(HangingPlaceEvent event) {
        if (!event.getPlayer().isOp()) event.setCancelled(true);
    }

    @EventHandler
    public void onHangingBreakByEntity(HangingBreakByEntityEvent event) {
        if (!event.getRemover().isOp()) event.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ItemFrame) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onItemFramePlace(PlayerPlaceItemInItemFrameEvent event) {
        if (!event.getPlayer().isOp()) {
            event.setCancelled(true);
            Location location = event.getItemFrame().getLocation();
            World world = ((CraftWorld) event.getItemFrame().getWorld()).getHandle();
            EntityItemFrame entityItemFrame = new EntityItemFrame(world, new BlockPosition(location.getX(), location.getY(), location.getZ()), ((CraftItemFrame) event.getItemFrame()).getHandle().direction);
            event.getItemFrame().remove();
            entityItemFrame.setItem(new ItemStack(Item.getById(0)));
            Utils.run(() -> {
                world.addEntity(entityItemFrame);
            });
        }
    }
}
