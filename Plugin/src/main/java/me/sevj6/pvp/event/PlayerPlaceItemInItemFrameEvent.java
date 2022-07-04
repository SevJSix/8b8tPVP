package me.sevj6.pvp.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

@Getter
@Setter
public class PlayerPlaceItemInItemFrameEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final ItemFrame itemFrame;
    private boolean cancelled;
    private ItemStack itemStack;

    public PlayerPlaceItemInItemFrameEvent(Player player, ItemFrame itemFrame, ItemStack itemStack) {
        this.player = player;
        this.itemFrame = itemFrame;
        this.itemStack = itemStack;
    }

    public static HandlerList getHandlerList() {
        return handlers;
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
