package me.sevj6.pvp.util.customevent.eventposters;

import me.sevj6.pvp.util.customevent.TotemPopEvent;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.Entity;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.concurrent.CompletableFuture;

public class ListenerTotemPop implements PacketListener {

    @GetField(clazz = PacketPlayOutEntityStatus.class, name = "b")
    private Field opCode;

    @GetField(clazz = PacketPlayOutEntityStatus.class, name = "a")
    private Field entityId;

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {

    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {
        PacketPlayOutEntityStatus packet = (PacketPlayOutEntityStatus) event.getPacket();
        byte code = opCode.getByte(packet);
        if (!(code == 35)) return;
        World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
        Entity entity = world.getEntity(entityId.getInt(packet));
        if (entity == null || entity.getWorld() != world) return;
        if (world.getEntity(entityId.getInt(packet)) instanceof EntityPlayer) {
            Player player = (Player) entity.getBukkitEntity();
            TotemPopEvent popEvent = new TotemPopEvent(player);
            Bukkit.getServer().getPluginManager().callEvent(popEvent);
        }
    }
}
