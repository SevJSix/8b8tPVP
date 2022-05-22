package me.sevj6.pvp.mixin.mixins;

import me.sevj6.pvp.event.NBTUpdateEvent;
import me.sevj6.pvp.event.TileEntityCreateEvent;
import me.txmc.rtmixin.CallbackInfo;
import me.txmc.rtmixin.mixin.At;
import me.txmc.rtmixin.mixin.Inject;
import me.txmc.rtmixin.mixin.MethodInfo;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.TileEntity;
import net.minecraft.server.v1_12_R1.World;
import org.bukkit.Bukkit;

public class MixinTest {

    @Inject(info = @MethodInfo(_class = TileEntity.class, name = "create", sig = {World.class, NBTTagCompound.class}, rtype = TileEntity.class), at = @At(pos = At.Position.TAIL))
    public static void onTileEntityCreate(CallbackInfo ci) {
        World world = (World) ci.getParameters()[0];
        NBTTagCompound compound = (NBTTagCompound) ci.getParameters()[1];
        TileEntityCreateEvent createEvent = new TileEntityCreateEvent(world, compound);
        Bukkit.getServer().getPluginManager().callEvent(createEvent);
    }

    @Inject(info = @MethodInfo(_class = ItemStack.class, name = "save", sig = NBTTagCompound.class, rtype = NBTTagCompound.class), at = @At(pos = At.Position.HEAD))
    public static void onItemNbtSave(CallbackInfo ci) {
        NBTTagCompound compound = (NBTTagCompound) ci.getParameters()[0];
        NBTUpdateEvent.Item saveEvent = new NBTUpdateEvent.Item(compound);
        Bukkit.getServer().getPluginManager().callEvent(saveEvent);
        if (saveEvent.isCancelled()) ci.cancel();
    }

    @Inject(info = @MethodInfo(_class = ItemStack.class, name = "load", sig = NBTTagCompound.class, rtype = void.class), at = @At(pos = At.Position.HEAD))
    public static void onItemNbtLoad(CallbackInfo ci) {
        NBTTagCompound compound = (NBTTagCompound) ci.getParameters()[0];
        NBTUpdateEvent.Item loadEvent = new NBTUpdateEvent.Item(compound);
        Bukkit.getServer().getPluginManager().callEvent(loadEvent);
        if (loadEvent.isCancelled()) ci.cancel();
    }
}
