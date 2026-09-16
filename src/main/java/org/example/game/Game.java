package org.example.game;

import org.example.model.Pokemon;

import java.util.Scanner;

public class Game {
    private final Scanner scanner;
    private GameState gameState = GameState.MENU;
    // create a player object then ask question of number of pokemon, name, double/ single in the future
    private Pokemon playerPokemon;

    public Game (Scanner scanner) {
        this.scanner = scanner;
        playerPokemon = null;
    }

     public void startGame () {
         IO.println("""
                        ███████████           █████                                                  
                       ░░███░░░░░███         ░░███                                                   
                        ░███    ░███  ██████  ░███ █████  ██████  █████████████    ██████  ████████  
                        ░██████████  ███░░███ ░███░░███  ███░░███░░███░░███░░███  ███░░███░░███░░███ 
                        ░███░░░░░░  ░███ ░███ ░██████░  ░███████  ░███ ░███ ░███ ░███ ░███ ░███ ░███ 
                        ░███        ░███ ░███ ░███░░███ ░███░░░   ░███ ░███ ░███ ░███ ░███ ░███ ░███ 
                        █████       ░░██████  ████ █████░░██████  █████░███ █████░░██████  ████ █████
                       ░░░░░         ░░░░░░  ░░░░ ░░░░░  ░░░░░░  ░░░░░ ░░░ ░░░░░  ░░░░░░  ░░░░ ░░░░░ 
                                                                                                     
                                                                                                     
                                                                                                     
                """);

         while(gameState != GameState.EXIT) {
             switch (gameState) {
                 case MENU -> displayMenu();
                 case SELECT_MON -> selectPokemon();
                 case BATTLE -> beginBattle();
                 case PREVIEW_MON -> previewPokemon();
                 default -> IO.println("""
                         
                         Your selected option doesn't exist
                         
                         """);
             }
         }
    }

    void selectPokemon() {
        // select pokemon of your choice
    }

    void beginBattle() {
        if (playerPokemon == null) {
            gameState = GameState.MENU;
            IO.println("You can't battle without your partner, pick one by selecting 1!!");
            IO.println();
            return;
        }
    }

    void previewPokemon() {
        // preview your selected Pokemons
        return;
    }

    private void displayMenu() {
        IO.println("""
                Menu
                ===
                (choose btw 1-4)
                
                1. Choose your Pokemon
                2. Begin the battle
                3. Check your Pokemon
                4. Close the game
                """);

        handleMenuOption();
    }

    private int readMenuChoice() {
        while (true) {
            IO.print(":- ");

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                if (choice >= 1 && choice <= 4) return  choice;
            } else {
                scanner.next();
            }

            IO.println("Choose the appropriate option btw 1-4!!");
        }
    }

    private void handleMenuOption() {
        switch (readMenuChoice()) {
            case 1 -> gameState = GameState.SELECT_MON;
            case 2 -> gameState = GameState.BATTLE;
            case 3 -> gameState = GameState.PREVIEW_MON;
            case 4 -> gameState = GameState.EXIT;
        }
    }
}
