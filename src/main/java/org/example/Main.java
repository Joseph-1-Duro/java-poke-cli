package org.example;

import org.example.game.Game;

import java.util.Scanner;

public class Main {
    static void main () {
        try (Scanner scanner = new Scanner(System.in)){
            new Game(scanner).startGame();
        } // close when scanner closes
    }
}