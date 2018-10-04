import java.util.Scanner;
import java.util.Random;

/**
 * @author Matt Weiss <mattjweiss1@gmail.com>
 */

public class MineSweeper {

    public int boardSize;

    private Board board;

    MineSweeper(int numMines, int size) {
        this.board = new Board(numMines, size);
        this.boardSize = size;
    }

    /** Returns the value of the game board at location (x, y). */
    public char getBoardValue(int x, int y) {
        return this.board.state[x][y];
    }

    /** Guess that location (x, y) is NOT a mine. */
    public void guess(int x, int y) {
        if(board.solution[x][y]=='b') { // TODO replace with OO code
            board.state[x][y] = 'b';
        }
        else {
            sweepClear(board, x, y);
        }
    }

    /** Place a flag symbol on location (x, y). */
    public void flag(int x, int y) {
        if(board.state[x][y] == 'f') {
            board.state[x][y] = 'x';
        }
        if(board.state[x][y] == 'x') {
            board.state[x][y] = 'f';
        }
        else {
            // TODO MineSweeper.flag shouldn't print anything.
            System.out.println("Can only flag x spaces.");
        }
    }

    /** Check if the user has won. */
    public boolean userWon() {
        if(isOver() && !userLost()) {
            return true;
        }
        else {
            return false;
        }
    }

    /** Check if the user has lost. */
    public boolean userLost() {
        boolean trippedMine = false;
        for(int i=0; i<board.size; i++) {
            for(int j=0; j<board.size; j++) {
                if(board.state[i][j]=='b'){
                    trippedMine = true;
                }
            }
        }
        return trippedMine;
    }

    public void displaySolution() {
        for(int i=0; i<board.size; i++) {
            for(int j=0; j<board.size; j++) {
                board.state[i][j] = board.solution[i][j];
            }
        }
    }

    private boolean isOver() {
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

    private void sweepClear(Board board, int x, int y) {
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

    private class Board {
        char[][] state;
        char[][] solution;
        int size;
        int numMines;
    
        Board(int numMines, int size) {
            this.size = size;
            this.numMines = numMines;
    
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

}


