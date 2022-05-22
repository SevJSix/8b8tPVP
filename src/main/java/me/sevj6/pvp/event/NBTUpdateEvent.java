package me.sevj6.pvp.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class NBTUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final NBTTagCompound compound;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static class Item extends NBTUpdateEvent {

        public Item(NBTTagCompound compound) {
            super(compound);
        }
    }

    public static class TileEntity extends NBTUpdateEvent {

        public TileEntity(NBTTagCompound compound) {
            super(compound);
        }
    }

    public static class Entity extends NBTUpdateEvent {

        public Entity(NBTTagCompound compound) {
            super(compound);
        }
    }
}
