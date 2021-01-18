import java.util.Scanner;

public class TicTacToe {
    //game board
    static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, { " ", " ", " "}};

    //checks if there is a winner
    static boolean checkWinner() {

        //check diagonal
        //top - bottom
        if(board[2][0].equals(board[1][1]) && board[2][0].equals(board[0][2]) && !board[2][0].equals(" ")) {
            return true;
        }
        //bottom - top
        if(board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals(" ")) {
            return true;
        }

        //check rows
        for(int r = 0; r < 3; r++) {
            if(board[r][0].equals(board[r][1]) && board[r][0].equals(board[r][2]) && !board[r][0].equals(" ")) {
                return true;
            }
        }

        //check columns
        for(int c = 0; c < 3; c++) {
            if(board[0][c].equals(board[1][c]) && board[0][c].equals(board[2][c]) && !board[0][c].equals(" ")) {
                return true;
            }
        }

        return false;
    }

    //Checks input and if valid adds to the board
    static boolean checkValidInput(int input, String tile) {

        int y = input / 10;

        int x = input % 10;

        if(x > 3 || x < 1 || y > 3 || y < 1) {
            return true;
        }

        if(!board[x - 1][y - 1].equals(" ")) {
            return true;
        }

        board[x - 1][y - 1] = tile;
        return false;
    }

    static void printBoard() {

        System.out.println("   1   2   3");

        for(int i = 0; i < 3; i++) {
            System.out.println(i + 1 + "  " + board[i][0] + " | " + board[i][1] +" | " + board[i][2]);
        }
    }

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);

        boolean finished = false;

        int turnCounter = 0;

        while (!finished) {

            printBoard();

            //x 's turn

            System.out.println("X's turn: Enter placement as xy");

            int input = in.nextInt();

            while(checkValidInput(input, "X")) {
                System.out.println("Invalid Input, Try Again: ");
                input = in.nextInt();
            }

            turnCounter++;

            printBoard();

            //check if won

            if (checkWinner()) {
                System.out.println("X WON!");
                break;
            }

            //check draw
            if(turnCounter == 9) {
                System.out.println("DRAW");
                break;
            }

            //o's turn

            System.out.println("O's turn: Enter placement as xy");

            int oInput = in.nextInt();

            while(checkValidInput(oInput, "O")){
                System.out.println("Invalid Input, Try Again: ");
                oInput = in.nextInt();
            }

            //check if won

            if (checkWinner()) {
                System.out.println("O WON!");
                break;
            }

            turnCounter++;
        }
    }

}
