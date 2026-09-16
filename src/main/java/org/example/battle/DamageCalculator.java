package org.example.battle;

import org.example.model.Move;
import org.example.model.Pokemon;

public class DamageCalculator {
    public static double getSTAB(Pokemon attacker, Move move) {
        return attacker.getType().contains(move.getType()) ? 1.5 : 1.0;
    }

    public static int calculateDamage(Pokemon attacker, Pokemon defender, Move move) {
        int attStat = attacker.getAtt();
        int defStat = defender.getDef();
        int power = move.getBasePower();

        double stab = getSTAB(attacker, move);

        double rawDamage = ((power * attStat / (double) defStat) / 50.0 + 2) * stab;

        return (int) Math.floor(rawDamage);
    }
}
