package org.example.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PokemonTest {
    private Pokemon zigzagoon;
    private Pokemon raichu;
    private Pokemon heatran;
    private Pokemon unown;

    @BeforeEach
    void generateTestMon (){
        zigzagoon = new Pokemon("Zigzagoon", Type.NORMAL, 40, 23, 28, 70);
        raichu = new Pokemon("Raichu", Type.NORMAL, Type.ELECTRIC, 89, 20, 25, 80);
        heatran = new Pokemon("Heatran", Type.STEEL, Type.FIRE, 200, 28, 70, 30);
        unown = new Pokemon("Unown", Type.PSYCHIC, Type.PSYCHIC, 20, 20, 20, 20);
    }

    @Test
    @DisplayName("Monotype should return only 1 type")
    void testMonotypeCreation() {
        assertEquals(1, zigzagoon.getType().size());
    }

    @Test
    @DisplayName("DualType should return 2 types")
    void testDualTypeCreation() {
        assertEquals(2, heatran.getType().size());
    }

    @Test
    @DisplayName("Duplicate type in constructor")
    void duplicateTypeHandling() {
        assertEquals(1, unown.getType().size());
        assertTrue(unown.getType().contains(Type.PSYCHIC));
    }

    @Test
    @DisplayName("Hp goes minimum to zero")
    void testHpToZero() {
        raichu.takeDamage(150);

        int currentHp = raichu.getCurrentHp();
        assertEquals(0, currentHp);
    }

    @Test
    @DisplayName("Pokemon has fainted")
    void testFaintedPokemon() {
        heatran.takeDamage(300);

        assertTrue(heatran.isFainted());
    }

    @Test
    @DisplayName("Invalid name argument error")
    void throwErrorForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pokemon(null, Type.NORMAL, 2, 3, 4, 88);
        }, "Name is invalid");

        assertThrows(IllegalArgumentException.class, () -> {
            new Pokemon(" ", Type.STEEL, 4, 5, 6, 7);
        }, "Name cannot be blank");
    }

    @Test
    @DisplayName("Invalidate negative or zero stats")
    void throwErrorForInvalidStat() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Pokemon("Rowlet", Type.FLYING, -5, 4, 5, 6);
        }, "Stat cannot be negative");

        assertThrows(IllegalArgumentException.class, () -> {
            new Pokemon("Capskid", Type.GRASS, 50, 23, 1, 0);
        }, "Stat cannot be zero");
    }

    @Test
    @DisplayName("Primary type invalidation")
    void throwErrorForInvalidPrimaryType() {
        new Pokemon("Zubat", null, Type.POISON, 40, 12, 12, 44);
    }
}