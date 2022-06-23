package me.sevj6.pvp.event.eventposters;

import me.sevj6.pvp.event.PlayerPlaceItemInItemFrameEvent;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

public class ListenerItemFrameUse implements PacketListener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
        if (packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.INTERACT_AT)) {
            World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
            Entity entity = packet.a(world);
            if (entity instanceof EntityItemFrame) {
                ItemFrame frame = (ItemFrame) entity.getBukkitEntity();
                EntityItemFrame entityItemFrame = (EntityItemFrame) entity;
                if (!entityItemFrame.getItem().isEmpty()) return;
                ItemStack item = (packet.b().equals(EnumHand.OFF_HAND)) ? event.getPlayer().getInventory().getItemInOffHand() : event.getPlayer().getInventory().getItemInMainHand();
                PlayerPlaceItemInItemFrameEvent frameEvent = new PlayerPlaceItemInItemFrameEvent(event.getPlayer(), frame, item);
                Bukkit.getServer().getPluginManager().callEvent(frameEvent);
                if (event.isCancelled()) event.setCancelled(true);
            }
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing outgoing) throws Throwable {

    }
}
