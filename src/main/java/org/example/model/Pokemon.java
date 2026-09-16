package org.example.model;

// all class must follow OOP

import java.util.List;

public class Pokemon {
    private final String name;
    private final List<Type> type;
    private final int maxHp;
    private int currentHp;
    private final int att;
    private final int def;
    private final int spd;
    // add moves later

    // MonoType Pokemon constructor
    public Pokemon(String name, Type type, int maxHp, int att, int def, int spd) {
        this(name, type, null, maxHp, att, def, spd);
    }

    // DualType Pokemon constructor
    // applied fail first principle very important
    public Pokemon(String name, Type type1, Type type2, int maxHp, int att, int def, int spd) {
        // fail first on bad name
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Pokemon name cannot be null or empty");
        }

        // fail first on bad primary type
        if (type1 == null) {
            throw new IllegalArgumentException("Primary type cannot be null");
        }

        // fail on bad stats
        if (maxHp <= 0 || att <= 0 || def <= 0 || spd <= 0) {
            throw new IllegalArgumentException("Stats can't be zero or negative");
        }

        this.name = name;

        if (type2 == null || type1 == type2) {
            this.type = List.of(type1);
        } else {
            this.type = List.of(type1, type2);
        }


        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.att = att;
        this.def = def;
        this.spd = spd;
    }

    public String getName() {
        return name;
    }

    public List<Type> getType() {
        return type;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public int getAtt() {
        return att;
    }

    public int getDef() {
        return def;
    }

    public int getSpd() {
        return spd;
    }

    // in-Battle characteristics (might move to the battle class)
    public boolean isFainted() {
        return this.currentHp == 0;
    }

    public void takeDamage(int damageReceived) {
        this.currentHp = Math.max(0, this.currentHp - damageReceived);
    }

    public void fullRestore() {
        this.currentHp = getMaxHp();
    }
}
