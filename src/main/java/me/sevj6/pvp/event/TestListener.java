package me.sevj6.pvp.event;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;

public class TestListener implements Listener {

    private final HashMap<Player, Integer> playerMap = new HashMap<>();

    @EventHandler
    public void onUse32k(PlayerUse32kEvent event) {
        if (event.getDistanceBetweenAttackerAndTarget() > 8 ||
                event.isPlayerAttackingTooFarFromActiveContainer()) {
            event.setCancelled(true);
            System.out.println("Prevented a player from possibly using a 32k teleport exploit");
        }
    }

    @EventHandler
    public void onPlayerUseCrystal(PlayerPlaceCrystalEvent event) {
        if (event.getCrystal() != null) {
            if (event.getPlayer().isOp()) {
                event.generateCrystals(100, true);
                event.getPlayer().getActiveItem().subtract();
            } else {
                event.explodeCrystal();
            }
        }
    }

    @EventHandler
    public void onTileEntityCreate(TileEntityCreateEvent event) {
        System.out.println("Tile entity created in world " + event.getWorld());
    }

    @EventHandler
    public void onItemNbtUpdate(NBTUpdateEvent.Item event) {
        System.out.println("NBT on an item has been updated");
        if (event.getCompound().hasKey("AttributeModifiers")) {
            System.out.println("found an item with attributes, now cancelling update event");
            event.setCancelled(true);
            event.getCompound().remove("AttributeModifiers");
        }
    }

    @EventHandler
    public void onTotemPop(TotemPopEvent event) {
        if (event.getPlayer().isDead()) return;
        Player player = event.getPlayer();
        addToMap(player);
        player.sendActionBar(
                ChatColor.translateAlternateColorCodes('&',
                        "&4&lYou have popped &8&l" + this.playerMap.get(player) + " &r&4&ltotems")
        );
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (this.playerMap.containsKey(event.getEntity())) {
            Bukkit.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&',
                    "&3" + event.getEntity().getName() + " &4died after popping "
                            + "&6" + this.playerMap.get(event.getEntity()) + " totems"));
        }
        removeFromMap(event.getEntity());
    }

    public void addToMap(Player player) {
        if (this.playerMap.containsKey(player)) {
            this.playerMap.replace(player, this.playerMap.get(player) + 1);
        } else {
            this.playerMap.put(player, 1);
        }
    }

    public void removeFromMap(Player player) {
        this.playerMap.remove(player);
    }
}
