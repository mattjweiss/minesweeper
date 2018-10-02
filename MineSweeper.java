import java.util.Scanner;
import java.util.Random;

class MineSweeper {
    public static void main(String[] args) {
        MineSweeper game = new MineSweeper();
        game.play();
    }

    void play() {

        // Initialize game
        CommandLineInterface comm = new CommandLineInterface();
        Settings settings = comm.settingsPrompt();
        Board board = new Board(settings);

        // Play turns until the user wins or loses
        boolean win = false;
        boolean lose = false;
        boolean over = false;
        while(!win && !lose) {
    
            // Display board
            printBoard(board);
    
            // Get command
            String[] command = comm.commandPrompt();
            Action action = ActionFactory.getAction(command);
    
            // Apply command
            action.apply(board);
            over = action.checkOverConditions(board);
            lose = action.checkLoseConditions(board);
            win = over && !lose;
        }
    
        if(win) {
            System.out.println();
            System.out.println("You win!");
            printSolution(board);
            System.out.println();
        }
        else if(lose) {
            System.out.println();
            System.out.println("You lose!");
            printSolution(board);
            System.out.println();
        }
        else {
            System.out.println();
            System.out.println("An error occurred.");
            System.out.println();
        }
    }
   
    void printBoard(Board board) {
        System.out.println();
        System.out.print("   ");
        for(int i=0; i<board.size; i++) {
            System.out.print(i);
            System.out.print(' ');
        }
        System.out.println();
        System.out.println();
        char[][] boardArray = board.state;
        int size = boardArray.length; 
        for(int i=0; i<size; i++){
            System.out.print(i);
            System.out.print("  ");
            for(int j=0; j<size; j++){
                System.out.print(boardArray[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }

    void printSolution(Board board) {
        System.out.println();
        System.out.print("   ");
        for(int i=0; i<board.size; i++) {
            System.out.print(i);
            System.out.print(' ');
        }
        System.out.println();
        System.out.println();
        char[][] boardArray = board.solution;
        int size = boardArray.length; 
        for(int i=0; i<size; i++){
            System.out.print(i);
            System.out.print("  ");
            for(int j=0; j<size; j++){
                System.out.print(boardArray[i][j]);
                System.out.print(' ');
            }
            System.out.println();
        }
        System.out.println();
    }
}

class ActionFactory{
    static Action getAction(String[] command) {
        String commandName = command[0];
        int x;
        int y;
        switch(commandName) {
            case "guess" :
                x = Integer.parseInt(command[1]);
                y = Integer.parseInt(command[2]);
                return new Guess(x, y);
            case "flag" :
                x = Integer.parseInt(command[1]);
                y = Integer.parseInt(command[2]);
                return new Flag(x, y);
            default :
                System.out.println("Command not recognized.");
                return new BadAction();
        }
    }
}

interface Action {
    void apply(Board board);
    boolean checkOverConditions(Board board);
    boolean checkLoseConditions(Board board);
}

class Guess implements Action {
    int x;
    int y;

    Guess(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void apply(Board board) {
        if(board.solution[x][y]=='b') { // don't use board.solution.
            board.state[x][y] = 'b';
        }
        else {
            sweepClear(board, x, y);
        }
    }

    public boolean checkOverConditions(Board board) {
        int numSpacesLeft = 0;
        for(int i=0; i<board.size; i++) {
            for(int j=0; j<board.size; j++) {
                if(board.state[i][j]=='x' || board.state[i][j]=='f'){
                    numSpacesLeft++;
                }
            }
        }
        if(numSpacesLeft == board.numMines) {
            return true;
        }
        else {
            return false;
        }
    }


    public boolean checkLoseConditions(Board board) {
        if(board.solution[x][y] == 'b') {
            return true;
        }
        else {
            return false;
        }
    }

    void sweepClear(Board board, int x, int y) {
        // if there are, count them and mark (x,y) with number
        // if there aren't, clear the space and sweepClear adjacent spaces
        
        // Count the bombs adjacent to (x,y)
        int numBombs = 0;
        for(int i=x-1; i<=x+1; i++) {
            if(i<0 || i>=board.size){continue;}
            for(int j=y-1; j<=y+1; j++) {
                if(j<0 || j>=board.size){continue;}
                if(board.solution[i][j] == 'b') {
                    numBombs++;
                }
            }
        }
        // If there aren't any, clear (x,y) and sweepClear adjacent spaces
        if(numBombs==0) {
            board.state[x][y] = ' ';
            for(int i=x-1; i<=x+1; i++) {
                if(i<0 || i>=board.size){continue;}
                for(int j=y-1; j<=y+1; j++) {
                    if(j<0 || j>=board.size){continue;}
                    if(board.state[i][j] != ' ') {
                        sweepClear(board, i, j);
                    }
                }
            }
        }
        else {
            board.state[x][y] = (char)(numBombs+48);
        }
    }
}

class Flag implements Action {
    int x;
    int y;

    Flag(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void apply(Board board) {
        if(board.state[x][y] == 'f') {
            board.state[x][y] = 'x';
        }
        if(board.state[x][y] == 'x') {
            board.state[x][y] = 'f';
        }
        else {
            System.out.println("Can only flag x spaces.");
        }
    }

    public boolean checkOverConditions(Board board) {
        return false;
    }


    public boolean checkLoseConditions(Board board) {
        return false;
    }
}

class BadAction implements Action { // Don't like this
    public void apply(Board board) {}
    public boolean checkOverConditions(Board board) {
        return false;
    }
    public boolean checkLoseConditions(Board board) {
        return false;
    }
}



class CommandLineInterface {
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

}

class Settings {
    int numMines;
    int size;

    Settings(int numMines, int size) {
        this.numMines = numMines;
        this.size = size;
    }
}

class Board {
    char[][] state;
    char[][] solution;
    int size;
    int numMines;

    Board(Settings settings) {
        size = settings.size;
        numMines = settings.numMines;

        // create an initial board of 'x's of size `size`
        state = new char[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                state[i][j] = 'x';
            }
        }

        // randomly generate a solution (mine configuration)
        solution = new char[size][size];
        for(int i=0; i<size; i++){
            for(int j=0; j<size; j++){
                solution[i][j] = ' ';
            }
        }
        Random random = new Random();
        int x;
        int y;
        for(int z=0; z<numMines; z++){
            x = random.nextInt(size);
            y = random.nextInt(size);
            solution[x][y] = 'b';
        }
    }
}
 
