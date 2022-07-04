package me.sevj6.pvp.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.DamageSource;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.CreatureSpawnEvent;

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

    public void explodeCrystal() {
        if (crystal != null) {
            Utils.run(() -> {
                crystal.damageEntity(DamageSource.playerAttack(((CraftPlayer) player).getHandle()), 10.0F);
            });
        }
    }

    public void generateCrystals(int amount, boolean explode) {
        World world = ((CraftWorld) player.getWorld()).getHandle();
        Utils.run(() -> {
            for (int i = 0; i < amount; i++) {
                EntityEnderCrystal newCrystal = new EntityEnderCrystal(world, crystalLocation.getX(), crystalLocation.getY(), crystalLocation.getZ());
                newCrystal.setBeamTarget(null);
                newCrystal.setShowingBottom(false);
                world.addEntity(newCrystal, CreatureSpawnEvent.SpawnReason.CUSTOM);
                if (explode) {
                    newCrystal.damageEntity(DamageSource.playerAttack(((CraftPlayer) player).getHandle()), 10.0F);
                }
            }
        });
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
