package me.sevj6.pvp.mixin;

import me.sevj6.pvp.Manager;
import me.sevj6.pvp.PVPServer;
import me.sevj6.pvp.mixin.mixins.MixinTest;
import me.txmc.rtmixin.RtMixin;
import org.bukkit.configuration.ConfigurationSection;

public class MixinManager extends Manager {

    public MixinManager() {
        super("Mixins");
    }

    @Override
    public void init(PVPServer plugin) {
        RtMixin.attachAgent().orElseThrow(NullPointerException::new);
        RtMixin.processMixins(MixinTest.class);
    }

    @Override
    public void destruct(PVPServer plugin) {

    }

    @Override
    public void reloadConfig(ConfigurationSection config) {

    }
}
