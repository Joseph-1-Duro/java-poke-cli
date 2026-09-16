package org.example.model;

public class Move {
    private final String name;
    private final int maxPP;
    private int currentPP;
    private final int basePower;
    private final Type type;

    public Move (String name, int maxPP, int basePower, Type type) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        if (maxPP <= 0 || basePower <= 0) {
            throw new IllegalArgumentException("PP or Power cannot be negative");
        }

        if (type == null) {
            throw new IllegalArgumentException("A type cannot be null");
        }

        this.name = name;
        this.maxPP = maxPP;
        this.currentPP = maxPP;
        this.basePower = basePower;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getMaxPP() {
        return maxPP;
    }

    public int getCurrentPP() {
        return currentPP;
    }

    public int getBasePower() {
        return basePower;
    }

    public Type getType() {
        return type;
    }

    // might move else where
    public void restoreMove() {
        this.currentPP = getMaxPP();
    }
}
