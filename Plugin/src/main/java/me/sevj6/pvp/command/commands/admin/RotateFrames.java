package me.sevj6.pvp.command.commands.admin;

import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;

public class RotateFrames implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            World world = player.getWorld();
            world.getEntities().stream().filter(entity -> entity instanceof ItemFrame).forEach(entity -> ((ItemFrame) entity).setRotation(Rotation.NONE));
        }
        return true;
    }
}
