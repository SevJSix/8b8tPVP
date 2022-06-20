package me.sevj6.pvp.listener.tablist;

import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.util.Utils;
import me.txmc.protocolapi.reflection.ClassProcessor;
import me.txmc.protocolapi.reflection.GetField;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.IChatBaseComponent;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Tablist8b8t implements AbstractTablist {

    private final List<String> headerOption = PVPServer.getInstance().getConfig().getStringList("TabList.Header");
    private final List<String> footerOption = PVPServer.getInstance().getConfig().getStringList("TabList.Footer");
    @GetField(clazz = PacketPlayOutPlayerListHeaderFooter.class, name = "a")
    private Field headerField;
    @GetField(clazz = PacketPlayOutPlayerListHeaderFooter.class, name = "b")
    private Field footerField;

    public Tablist8b8t(PVPServer plugin) {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::sendHeaderAndFooterList, 20L, 20L);
    }

    @Override
    public void sendHeaderAndFooterList() {
        if (headerField == null || footerField == null) ClassProcessor.process(this);
        if (getOnlinePlayers().isEmpty()) return;
        getOnlinePlayers().forEach(player -> {
            try {
                String headerStr = String.join("\n", headerOption);
                String footerStr = String.join("\n", footerOption);
                IChatBaseComponent header = new ChatComponentText(parseText(headerStr, player));
                IChatBaseComponent footer = new ChatComponentText(parseText(footerStr, player));
                PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
                headerField.set(packet, header);
                footerField.set(packet, footer);
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public String parseText(String text, Player player) {
        int ping = getPlayerPing(player);
        String tps = Utils.getRealTps();
        return ChatColor.translateAlternateColorCodes('&', text
                .replace("%tps%", tps)
                .replace("%players%", String.valueOf(getOnlinePlayers().size()))
                .replace("%ping%", String.valueOf(ping))
                .replace("%uptime%", Utils.getFormattedInterval(System.currentTimeMillis() - PVPServer.START_TIME))
        );
    }

    @Override
    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }

    @Override
    public int getPlayerPing(Player player) {
        return ((CraftPlayer) player).getHandle().ping;
    }
}
