package me.sevj6.pvp.arena.boiler;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Farter extends AbstractFart {

    public Farter(Player farter, int length, int volume, FartPungency pungency) {
        super(farter, length, volume, pungency);
    }

    public Farter(Player farter) {
        this(farter, 5, 10, FartPungency.NORMAL);
    }

    @Override
    public void fart() {
        getFarter().getLocation().createExplosion(3.0F);
        getFarter().getLocation().getNearbyPlayers(6).stream().filter(player -> !player.equals(getFarter())).forEach(player -> player.addPotionEffect(
                new PotionEffect(PotionEffectType.CONFUSION, 1000, 10)
        ));
    }
}
