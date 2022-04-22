package me.sevj6.pvp.arena;

import lombok.AllArgsConstructor;
import me.sevj6.pvp.arena.boiler.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author 254n_m
 * @since 4/21/22/ 10:38 PM
 * This file was created as a part of 8b8tPVP
 */
@AllArgsConstructor
public class TestCommand implements CommandExecutor {

    private final Arena arena;
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        System.out.println(arena.isPlayerInArena((Player) commandSender));
        return true;
    }
}
