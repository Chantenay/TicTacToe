import java.util.ArrayList;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class TicTacToe extends Application {
    //game board
    static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, { " ", " ", " "}};

    Rec[][] rectangles = {{null, null, null}, {null, null, null}, {null, null, null}};

    Rec highlighted = null;

    Group root = new Group();

    int turns = 0;

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

    static int[] snapToGrid(Rec rec) {
        int x;
        int y;

        int boardPlacementX = -1;;
        int boardPlacementY = -1;;

        if (rec.getLayoutX() > 0 && rec.getLayoutX() < 200) {
            x = 25;
            boardPlacementX = 0;
        } else if (rec.getLayoutX() > 200 && rec.getLayoutX() < 400) {
            x = 225;
            boardPlacementX = 1;
        } else if (rec.getLayoutX() > 400 && rec.getLayoutX() < 600) {
            x = 425;
            boardPlacementX = 2;
        } else {
            x = -1;
        }

        if (rec.getLayoutY() > 0 && rec.getLayoutY() < 200) {
            y = 25;
            boardPlacementY = 0;
        } else if (rec.getLayoutY() > 200 && rec.getLayoutY() < 400) {
            y = 225;
            boardPlacementY = 1;
        } else if (rec.getLayoutY() > 400 && rec.getLayoutY() < 600) {
            y = 425;
            boardPlacementY = 2;
        } else {
            y = -1;
        }

        if (!addToBoard(boardPlacementX, boardPlacementY, rec.type)) {
            return new int[]{-1, -1};
        }

        return new int[]{x, y};
    }

    static boolean addToBoard(int x, int y, String type) {
        if (x != -1 && y != -1) {
            if (board[x][y].equals(" ")) {
                board[x][y] = type;
                return true;
            }
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Board");
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);

        for(int i = 0; i < 600; i = i + 200) {
            for(int j = 0; j < 600; j = j + 200) {
                Rectangle r = new Rectangle(199, 199);
                r.setLayoutX(i);
                r.setLayoutY(j);
                r.setFill(Color.LIGHTGRAY);

                root.getChildren().add(r);
            }
        }


        primaryStage.show();

        Rec x = new Rec(150, 150, "X");
        root.getChildren().add(x);

        boolean finished = false;

        int turnCounter = 0;

       /* while (!finished) {

            printBoard();

            //x 's turn

            System.out.println("X's turn: Enter placement as xy");

            Rec x = new Rec(150, 150, "X");
            root.getChildren().add(x);
            turnCounter++;
            printBoard();

            //check if won

            if (checkWinner()) {
                System.out.println("X WON!");
                break;
            }

            //check draw
            if (turnCounter == 9) {
                System.out.println("DRAW");
                break;
            }

            //o's turn

            Rec o = new Rec(150, 150, "X");
            root.getChildren().add(o);
            turnCounter++;
            printBoard();

            //check if won

            if (checkWinner()) {
                System.out.println("O WON!");
                break;
            }

            turnCounter++;
        }*/


    }

    class Rec extends Rectangle {
        String type;

        double mouseX;
        double mouseY;

        Rec (double x, double y, String type){
            super(x, y);

            this.type = type;

            this.setLayoutX(625);
            this.setLayoutY(225);

            if (type.equals("X")) {
                 this.setFill(Color.RED);
            } else {
                this.setFill(Color.BLUE);
            }

            this.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();
                this.toFront();
            });

            this.setOnMouseDragged(event -> {
                this.setLayoutX(event.getSceneX() - 75);
                this.setLayoutY(event.getSceneY() - 75);

            });

            this.setOnMouseReleased(event -> {
                int[] array = snapToGrid(this);

                //check valid placement
                if(array[0] != -1 && array[1] != -1) {
                    this.setLayoutX(array[0]);
                    this.setLayoutY(array[1]);

                    Rectangle rectangle = new Rectangle(150, 150);
                    rectangle.setFill(this.fillProperty().getValue());
                    rectangle.setLayoutX(array[0]);
                    rectangle.setLayoutY(array[1]);

                    root.getChildren().add(rectangle);
                    root.getChildren().remove(this);

                    turns++;
                    //check if won
                    if (checkWinner()) {
                        //end the game
                    }
                    //check if draw
                    else if (turns == 9) {
                        //end the game
                    }
                    //start next turn
                    else {
                        String newType = "X";
                        if (this.type.equals("X")) {
                            newType = "O";
                        }
                        Rec newRec = new Rec(150, 150, newType);
                        root.getChildren().add(newRec);
                    }
                } else {
                    Rec newRec = new Rec(150, 150, this.type);
                    root.getChildren().add(newRec);
                    root.getChildren().remove(this);
                }

            });

        }

    }
}
