package me.sevj6.pvp.util;

import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.arena.boiler.Arena;
import me.sevj6.pvp.listener.tablist.Sorter;
import me.sevj6.pvp.portals.boiler.Portal;
import me.txmc.protocolapi.PacketEvent;
import net.minecraft.server.v1_12_R1.*;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class Utils {
    public static final String PREFIX = "&6[&18b&98t&6]&r";
    private static final DecimalFormat format = new DecimalFormat("#.##");
    private static final List<EntityType> invalidEntityTypes = Arrays.asList(EntityType.PLAYER, EntityType.ITEM_FRAME, EntityType.ARMOR_STAND);

    public static String translateChars(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static String getFormattedInterval(long ms) {
        long seconds = ms / 1000L % 60L;
        long minutes = ms / 60000L % 60L;
        long hours = ms / 3600000L % 24L;
        long days = ms / 86400000L;
        return String.format("%dd %02dh %02dm %02ds", days, hours, minutes, seconds);
    }

    public static ChatColor getTPSColor(String input) {
        if (!input.equals("*20")) {
            String toDouble = input.split("\\.")[0];
            double tps = Double.parseDouble(toDouble);
            if (tps >= 18.0D) {
                return ChatColor.GREEN;
            } else {
                return tps >= 13.0D ? ChatColor.YELLOW : ChatColor.RED;
            }
        } else {
            return ChatColor.GREEN;
        }
    }

    /**
     * Runs a task on bukkit's main thread
     *
     * @param runnable The task to be run
     */
    public static void run(Runnable runnable) {
        Bukkit.getScheduler().runTask(PVPServer.getInstance(), runnable);
    }

    /**
     * Will attempt to invoke a method called sendMessage(String.class) on the object given
     *
     * @param obj     The recipient
     * @param message The message to be sent
     */
    public static void sendMessage(Object obj, String message) {
        message = String.format("%s &7➠&r %s", PREFIX, message);
        message = translateChars(message);
        try {
            Method method = obj.getClass().getMethod("sendMessage", String.class);
            method.setAccessible(true);
            method.invoke(obj, message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void kick(Player player, String message) {
        message = String.format("%s &7->&r %s", PREFIX, message);
        message = translateChars(message);
        String finalMessage = message;
        run(() -> player.kickPlayer(finalMessage));
    }

    public static void log(String message) {
        message = translateChars(message);
        PVPServer.getInstance().getLogger().log(Level.INFO, String.format("%s%c", message, Character.MIN_VALUE));
    }

    public static void log(String message, Manager manager) {
        message = translateChars(message);
        message = String.format("[&3%s&r] %s", manager.getName(), message);
        PVPServer.getInstance().getLogger().log(Level.INFO, message);
    }

    public static void crashPlayer(Player target) {
        for (int i = 0; i < 100; i++) {
            target.spawnParticle(Particle.EXPLOSION_HUGE, target.getLocation(), Integer.MAX_VALUE, 1, 1, 1);
        }
    }

    public static String formatLocation(Location location) {
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        World world = location.getWorld();
        return "&3world&r&a " + world.getName() + " &r&3X:&r&a " + format.format(x) + " &r&3Y:&r&a " + format.format(y) + " &r&3Z:&r&a " + format.format(z);
    }

    public static boolean isPlayerInPortal(Player player, Portal portal) {
        if (player.getWorld() != portal.getWorld()) return false;
        Location playerLoc = player.getLocation();
        AxisAlignedBB playerBox = new AxisAlignedBB(playerLoc.getX(), playerLoc.getY(), playerLoc.getZ(), playerLoc.getX(), playerLoc.getY() + 1, playerLoc.getZ());
        return portal.getBoundingBox().intersects(playerBox);
    }

    public static ItemStack shallowReadItemStack(PacketDataSerializer buf) {
        short itemID = buf.readShort();
        if (itemID < 0) return net.minecraft.server.v1_12_R1.ItemStack.a;
        byte count = buf.readByte();
        short damage = buf.readShort();
        return new net.minecraft.server.v1_12_R1.ItemStack(Item.getById(itemID), count, damage);
    }

    public static void clearCurrentContainer(EntityPlayer player) {
        if (!(player.activeContainer instanceof ContainerChest)) return;
        IInventory inventory = ((ContainerChest) player.activeContainer).e();
        for (int i = 0; i < inventory.getSize(); i++) inventory.setItem(i, ItemStack.a);
        inventory.update();
        log(String.format("&aCleared inventory&r&3 %s&r&a with window ID&r&3 %d&r&a because it had excessive NBT data", inventory.getClass().getSimpleName(), player.activeContainer.windowId));
    }

    public static boolean isIllegallyEnchanted(org.bukkit.inventory.ItemStack itemStack) {
        return itemStack.getEnchantments().entrySet().stream().anyMatch(e -> e.getValue() > e.getKey().getMaxLevel());
    }

    public static boolean isPlayerInArena(Player player) {
        for (Arena arena : PVPServer.getArenaManager().getArenas()) {
            if (arena.isPlayerInArena(player)) return true;
        }
        return false;
    }

    public static boolean isPositionInArena(BlockPosition pos) {
        for (Arena arena : PVPServer.getArenaManager().getArenas()) {
            if (arena.isPositionInArena(pos)) return true;
        }
        return false;
    }

    public static void removeEntities() {
        for (World world : Bukkit.getServer().getWorlds()) {
            for (Chunk chunk : world.getLoadedChunks()) {
                for (org.bukkit.entity.Entity entity : chunk.getEntities()) {
                    if (invalidEntityTypes.contains(entity.getType())) return;
                    entity.remove();
                }
            }
        }
    }

    private static String format(double tps) {
        return (tps > 18.0D ? "§a" : (tps > 16.0D ? "§e" : "§c")) + (tps > 20.0D ? "" : "") + String.format("%.2f", Math.min((double) Math.round(tps * 100.0D) / 100.0D, 20.0D));
    }

    // fix npe issue with Bukkit.getWorld();
    public static World getWorld(String worldName) {
        for (World world : Bukkit.getWorlds()) {
            String name = world.getName();
            if (name.equalsIgnoreCase(worldName)) {
                return world;
            }
        }
        return null;
    }

    public static void parsePlayerListName(Player player) {
        World world = player.getWorld();
        Sorter sorter = PVPServer.getInstance().getSorter();
        switch (world.getName()) {
            case "world":
                sorter.set(player, sorter.getHubTeam());
                player.setPlayerListName(sorter.parse(sorter.getHubPrefix() + player.getName()));
                break;
            case "world_nether":
                sorter.set(player, sorter.getNetherTeam());
                player.setPlayerListName(sorter.parse(sorter.getNetherPrefix() + player.getName()));
                break;
            case "world_the_end":
                sorter.set(player, sorter.getKitCreatorTeam());
                player.setPlayerListName(sorter.parse(sorter.getKitCreatorPrefix() + player.getName()));
                break;
            case "swordfight":
                sorter.set(player, sorter.getNoCrystalTeam());
                player.setPlayerListName(sorter.parse(sorter.getNoCrystalPrefix() + player.getName()));
                break;
            case "terrain":
                sorter.set(player, sorter.getTerrainTeam());
                player.setPlayerListName(sorter.parse(sorter.getTerrainPrefix() + player.getName()));
                break;
            default:
                player.setPlayerListName(sorter.parse("[" + player.getWorld().getName() + "] " + player.getName()));
                break;
        }
    }

    public static void cancelAndLagback(PacketEvent.Incoming event) {
        event.setCancelled(true);
        Player player = event.getPlayer();
        EntityPlayer ep = ((CraftPlayer) player).getHandle();
        run(() -> ep.playerConnection.teleport(player.getLocation()));
    }

    public static String getRealTps() {
        return format(Bukkit.getServer().getTPS()[0]);
    }
}
