package me.sevj6.pvp.listener.listeners.frames;

import me.sevj6.pvp.util.Utils;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

public class FrameListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        if (Utils.isPlayerInArena(player)) return;
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        if (frame.getItem() == null) return;
        frame.setRotation(frame.getRotation().rotateCounterClockwise());
        FrameInventory frameInventory = new FrameInventory("&18b&98t &6PvP&r", frame.getItem());
        frameInventory.open(player);
    }
}
