package me.sevj6.pvp.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
@Getter
public class PlayerUse32kEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final Entity target;
    private final ItemStack item32k;
    private final int itemSlot;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public double getDistanceBetweenAttackerAndTarget() {
        return player.getLocation().distance(target.getLocation());
    }

    public boolean isPlayerAttackingTooFarFromActiveContainer() {
        if (player.getOpenInventory() != null) {
            InventoryView container = player.getOpenInventory();
            return container.getBottomInventory().getLocation().distance(player.getLocation()) > 7.5 ||
                    container.getTopInventory().getLocation().distance(player.getLocation()) > 7.5;
        }
        return false;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
