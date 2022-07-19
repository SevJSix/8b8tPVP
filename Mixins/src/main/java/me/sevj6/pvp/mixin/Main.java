package me.sevj6.pvp.mixin;

import me.sevj6.pvp.mixin.mixins.MixinEnderCrystal;
import me.txmc.rtmixin.RtMixin;
import me.txmc.rtmixin.jagent.AgentMain;

public class Main {
    public static void init() {
        System.out.println("Loading mixins");
        System.out.println();
        System.out.println(AgentMain.getInst());
        if (AgentMain.getInst() == null) {
            System.out.println("Attaching agent.");
            RtMixin.attachAgent();
            System.out.println("Attached agent.");
            System.out.println(AgentMain.getInst());
        }
        RtMixin.processMixins(MixinEnderCrystal.class);
    }
}