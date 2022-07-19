package me.sevj6.pvp.mixin.mixins;

import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.v1_12_R1.EntityEnderCrystal;
import net.minecraft.server.v1_12_R1.World;

/**
 * @author 254n_m
 * @since 7/3/22/ 10:01 PM
 * This file was created as a part of 8b8tPVP
 */
public class MixinEnderCrystal {
    @Inject(info = @MethodInfo(_class = EntityEnderCrystal.class, name = "<init>", sig = {World.class, double.class, double.class, double.class}, rtype = EntityEnderCrystal.class), at = @At(pos = At.Position.TAIL))
    public static void onEnderCrystalCreate(CallbackInfo ci) {
        EntityEnderCrystal entityEnderCrystal = (EntityEnderCrystal) ci.getSelf();
        World world = (World) ci.getParameters()[0];
        System.out.println(world);
    }
}
