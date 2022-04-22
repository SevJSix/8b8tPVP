package me.sevj6.pvp.arena.create;

import lombok.Getter;
import lombok.Setter;
import me.sevj6.pvp.util.Utils;
import net.minecraft.server.v1_12_R1.BlockPosition;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Predicate;

public class InteractListener implements Listener {

    @Getter
    @Setter
    private BlockPosition pos1, pos2;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || !event.getAction().equals(Action.LEFT_CLICK_BLOCK))
            return;
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        Material inHand = player.getInventory().getItemInMainHand().getType();
        if (determine(inHand).test(Material.GOLD_AXE)) {
            event.setCancelled(true);
            setPos2(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            sendPositionMessage(player, getPos2(), false);
        } else if (determine(inHand).test(Material.IRON_AXE)) {
            event.setCancelled(true);
            setPos1(new BlockPosition(block.getX(), block.getY(), block.getZ()));
            sendPositionMessage(player, getPos1(), true);
        }
    }

    private void sendPositionMessage(Player player, BlockPosition pos, boolean firstPosition) {
        if (firstPosition) {
            Utils.sendMessage(player, "&aSet pos1 to &3" + pos.getX() + "&a, &3" + pos.getY() + "&a, &3" + pos.getZ());
        } else {
            Utils.sendMessage(player, "&aSet pos2 to &3" + pos.getX() + "&a, &3" + pos.getY() + "&a, &3" + pos.getZ());
        }
    }

    private Predicate<Material> determine(Material material) {
        return Predicate.isEqual(material);
    }
}
