import java.util.Scanner;
import java.util.Random;

public class MineSweeperCLI {

    public static void main(String[] args) {
        MineSweeperCLI mscli = new MineSweeperCLI();
        mscli.play();
    }

    private void play() {

        // Initialize game
        CommandLineInterface comm = new CommandLineInterface();
        Settings settings = comm.settingsPrompt();
        MineSweeper game = new MineSweeper(settings.numMines, settings.size);

        int x;
        int y;

        // Play turns until the user wins or loses
        while(!game.userWon() && !game.userLost()) {

            // Display board
            comm.printBoard(game);

            // Get command
            String[] command = comm.commandPrompt();
            x = Integer.parseInt(command[1]);
            y = Integer.parseInt(command[2]);
    
            // Apply command
            // TODO replace with OO code
            if(command[0].equals("guess")) {
                game.guess(x, y);
            }
            else if(command[0].equals("flag")) {
                game.flag(x, y);
            }
            else {
                System.out.println("Invalid command.");
            }
        }
        if(game.userWon()) {
            //comm.printWinMessage();
            System.out.println("You win!");
        }
        else if(game.userLost()) {
            //comm.printLoseMessage();
            System.out.println("You lose!");
        }
        game.displaySolution();
        comm.printBoard(game);
    }

    private class CommandLineInterface {
        Scanner inputScanner;
        CommandLineInterface() {
            inputScanner = new Scanner(System.in).useDelimiter("\n");
        }
        Settings settingsPrompt() {
            System.out.println("Enter a difficulty level (number of mines).");
            String input = inputScanner.next();
            int numMines = Integer.parseInt(input);
            System.out.println("Enter a size for the board.");
            input = inputScanner.next();
            int size = Integer.parseInt(input);
    
            Settings settings = new Settings(numMines, size);
            return settings;
        }
        String[] commandPrompt() {
            System.out.println("Enter a command.");
            String input = inputScanner.next();
            String[] strs = input.split(" ");
            return strs;
        }

        void printBoard(MineSweeper game) {
            System.out.println();
            System.out.print("   ");
            for(int i=0; i<game.boardSize; i++) {
                System.out.print(i);
                System.out.print(' ');
            }
            System.out.println();
            System.out.println();
            for(int i=0; i<game.boardSize; i++){
                System.out.print(i);
                System.out.print("  ");
                for(int j=0; j<game.boardSize; j++){
                    System.out.print(game.getBoardValue(i,j));
                    System.out.print(' ');
                }
                System.out.println();
            }
            System.out.println();
        }
    }

    class Settings {
        int numMines;
        int size;
    
        Settings(int numMines, int size) {
            this.numMines = numMines;
            this.size = size;
        }
    }

}



