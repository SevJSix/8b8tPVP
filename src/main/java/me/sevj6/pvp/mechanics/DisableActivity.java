package me.sevj6.pvp.mechanics;

import me.sevj6.pvp.util.Utils;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class DisableActivity implements PacketListener, Listener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        if (event.getPlayer().isOp() || Utils.isPlayerInArena(event.getPlayer())) return;
        CraftPlayer player = (CraftPlayer) event.getPlayer();
        World world = ((CraftWorld) event.getPlayer().getWorld()).getHandle();
        if (event.getPacket() instanceof PacketPlayInUseEntity) {
            PacketPlayInUseEntity packetUseEntity = (PacketPlayInUseEntity) event.getPacket();
            if (!packetUseEntity.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) return;
            event.setCancelled(true);
        } else if (event.getPacket() instanceof PacketPlayInBlockDig) {
            PacketPlayInBlockDig packetBlockDig = (PacketPlayInBlockDig) event.getPacket();
            PacketPlayInBlockDig.EnumPlayerDigType type = packetBlockDig.c();
            if (type.equals(PacketPlayInBlockDig.EnumPlayerDigType.ABORT_DESTROY_BLOCK) || type.equals(PacketPlayInBlockDig.EnumPlayerDigType.START_DESTROY_BLOCK)
                    || type.equals(PacketPlayInBlockDig.EnumPlayerDigType.STOP_DESTROY_BLOCK) || !Utils.isPositionInArena(packetBlockDig.a())) {
                event.setCancelled(true);
                PacketPlayOutBlockChange blockChange = new PacketPlayOutBlockChange(world, packetBlockDig.a());
                player.getHandle().playerConnection.sendPacket(blockChange);
            }
        } else if (event.getPacket() instanceof PacketPlayInUseItem) {
            PacketPlayInUseItem packet = (PacketPlayInUseItem) event.getPacket();
            if (world.getTileEntity(packet.a()) != null) {
                event.setCancelled(true);
                PacketPlayOutBlockChange blockChange = new PacketPlayOutBlockChange(world, packet.a());
                player.getHandle().playerConnection.sendPacket(blockChange);
            }
        } else {
            event.setCancelled(true);
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.getPlayer().isOp() || Utils.isPlayerInArena(event.getPlayer())) return;
        Location loc = event.getBlockPlaced().getLocation();
        if (Utils.isPositionInArena(new BlockPosition(loc.getX(), loc.getY(), loc.getZ()))) return;
        event.setCancelled(true);
    }
}
