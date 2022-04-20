package me.sevj6.pvp.arena.boiler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractFart {

    private Player farter;
    private int length;
    private int volume;
    private FartPungency pungency;

    public abstract void fart();

    public enum FartPungency {
        NO_SMELL,
        MILD,
        NORMAL,
        BAD,
        AWFUL,
        UNBEARABLE,
        DANGEROUS,
        FATAL
    }
}
