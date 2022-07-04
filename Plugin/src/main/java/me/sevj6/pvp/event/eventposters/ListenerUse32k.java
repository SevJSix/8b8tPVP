package me.sevj6.pvp.event.eventposters;

import me.sevj6.pvp.event.PlayerUse32kEvent;
import me.sevj6.pvp.util.Utils;
import me.txmc.protocolapi.PacketEvent;
import me.txmc.protocolapi.PacketListener;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PacketPlayInUseEntity;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public class ListenerUse32k implements PacketListener {

    @Override
    public void incoming(PacketEvent.Incoming event) throws Throwable {
        PacketPlayInUseEntity packet = (PacketPlayInUseEntity) event.getPacket();
        if (!packet.a().equals(PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)) return;
        Player attacker = event.getPlayer();
        EnumHand hand = packet.b();
        ItemStack stackInHand = (hand == EnumHand.OFF_HAND) ? attacker.getInventory().getItemInOffHand() : attacker.getInventory().getItemInMainHand();
        if (Utils.isIllegallyEnchanted(stackInHand)) {
            World world = ((CraftWorld) attacker.getWorld()).getHandle();
            Optional.ofNullable(packet.a(world)).ifPresent(entity -> {
                PlayerUse32kEvent use32kEvent = new PlayerUse32kEvent(attacker, entity.getBukkitEntity(), stackInHand, (hand == EnumHand.OFF_HAND) ? 45 : attacker.getInventory().getHeldItemSlot());
                Bukkit.getServer().getPluginManager().callEvent(use32kEvent);
                if (use32kEvent.isCancelled()) event.setCancelled(true);
            });
        }
    }

    @Override
    public void outgoing(PacketEvent.Outgoing event) throws Throwable {

    }
}
