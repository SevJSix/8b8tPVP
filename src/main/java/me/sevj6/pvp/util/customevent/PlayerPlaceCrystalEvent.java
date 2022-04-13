package me.sevj6.pvp.util.customevent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerPlaceCrystalEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final EntityEnderCrystal crystal;
    private final Location crystalLocation;
    private final Location placeLocation;
    private boolean cancelled;

    public static HandlerList getHandlerList() {
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

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
