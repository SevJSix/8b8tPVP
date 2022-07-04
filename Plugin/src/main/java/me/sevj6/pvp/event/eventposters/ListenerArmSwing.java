package me.sevj6.pvp.event.eventposters;

import me.sevj6.pvp.event.PlayerInputEvent;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.PacketPlayInArmAnimation;
import org.bukkit.Bukkit;

public class ListenerArmSwing implements PacketListener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInArmAnimation swingPacket = (PacketPlayInArmAnimation) event.getPacket();
        PlayerInputEvent inputEvent = new PlayerInputEvent(event.getPlayer(), swingPacket.a());
        Bukkit.getServer().getPluginManager().callEvent(inputEvent);
    }

    @Override
    public void outgoing(PacketEvent.Outgoing outgoing) throws Throwable {

    }
}
