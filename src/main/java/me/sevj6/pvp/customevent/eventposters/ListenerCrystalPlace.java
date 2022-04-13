package me.sevj6.pvp.customevent.eventposters;

import me.sevj6.pvp.customevent.PlayerPlaceCrystalEvent;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PacketPlayInUseItem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftEnderCrystal;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.Player;

public class ListenerCrystalPlace implements PacketListener {


    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInUseItem packet = (PacketPlayInUseItem) event.getPacket();
        Player player = event.getPlayer();
        BlockPosition placePos = packet.a();
        if (placePos == null) return;
        Location placeLocation = new Location(player.getWorld(), placePos.getX(), placePos.getY(), placePos.getZ());
        boolean holdingCrystal = (packet.c().equals(EnumHand.OFF_HAND)) ? player.getInventory().getItemInOffHand().getType() == org.bukkit.Material.END_CRYSTAL : player.getInventory().getItemInMainHand().getType() == org.bukkit.Material.END_CRYSTAL;
        org.bukkit.Material typeAtPos = placeLocation.getBlock().getType();
        boolean isAttemptingToCrystal = holdingCrystal && (typeAtPos == org.bukkit.Material.OBSIDIAN || typeAtPos == org.bukkit.Material.BEDROCK);
        if (isAttemptingToCrystal) {
            Location crystalLocation = placeLocation.clone().add(0.5, 1, 0.5);
            EntityEnderCrystal enderCrystal = crystalLocation.getNearbyEntitiesByType(EnderCrystal.class, 0, 1, 0).stream().map(crystal -> ((CraftEnderCrystal) crystal).getHandle()).findAny().orElse(null);
            PlayerPlaceCrystalEvent crystalEvent = new PlayerPlaceCrystalEvent(player, enderCrystal, crystalLocation, placeLocation);
            Bukkit.getServer().getPluginManager().callEvent(crystalEvent);
            if (crystalEvent.isCancelled()) event.setCancelled(true);
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }
}
