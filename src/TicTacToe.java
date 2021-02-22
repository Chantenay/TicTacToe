import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TicTacToe extends Application {
    //game board
    static String[][] board = {{" ", " ", " "}, {" ", " ", " "}, { " ", " ", " "}};

    //rectangles representing game board
    Rectangle[][] rectangles = {{null, null, null}, {null, null, null}, {null, null, null}};

    Rectangle highlighted = null;

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

    static int[] snapToGrid(double initX, double initY, Rec rec) {
        int x;
        int y;

        int boardPlacementX = -1;;
        int boardPlacementY = -1;;

        if (initX > 0 && initX < 200) {
            x = 25;
            boardPlacementX = 0;
        } else if (initX > 200 && initX < 400) {
            x = 225;
            boardPlacementX = 1;
        } else if (initX > 400 && initX < 600) {
            x = 425;
            boardPlacementX = 2;
        } else {
            x = -1;
        }

        if (initY > 0 && initY < 200) {
            y = 25;
            boardPlacementY = 0;
        } else if (initY > 200 && initY < 400) {
            y = 225;
            boardPlacementY = 1;
        } else if (initY > 400 && initY < 600) {
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

    void highlightNearest(double x, double y) {
        if (highlighted != null) {
            highlighted.setFill(Color.LIGHTGREY);
        }

        int xLoc = 0;
        int yLoc = 0;

        if (x > 0 && x < 200) {
            xLoc = 0;
        } else if (x > 200 && x < 400) {
            xLoc = 1;
        } else {
            xLoc = 2;
        }

        if (y > 0 && y < 200) {
            yLoc = 0;
        } else if (y > 200 && y < 400) {
            yLoc = 1;
        } else {
            yLoc = 2;
        }

        highlighted = rectangles[xLoc][yLoc];
        if(board[xLoc][yLoc].equals(" ")) {
            highlighted.setFill(Color.GREEN);
        } else {
            highlighted.setFill(Color.RED);
        }
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

                rectangles[i/200][j/200] = r;

                r.setLayoutX(i);
                r.setLayoutY(j);
                r.setFill(Color.LIGHTGRAY);

                root.getChildren().add(r);
            }
        }

        primaryStage.show();

        //Start game with x token
        Rec x = new Rec(150, 150, "X");
        root.getChildren().add(x);
    }

    class Rec extends Rectangle {
        String type;

        double mouseX;
        double mouseY;

        private double x, y;

        Rec (double x, double y, String type){
            super(x, y);

            this.x = x;
            this.y = y;

            this.type = type;

            this.setLayoutX(625);
            this.setLayoutY(225);

            if (type.equals("X")) {
                 this.setFill(Color.HOTPINK);
            } else {
                this.setFill(Color.BLUE);
            }

            this.setOnMousePressed(event -> {
                mouseX = event.getSceneX();
                mouseY = event.getSceneY();

                this.x = super.getLayoutX();
                this.y = super.getLayoutY();

                this.toFront();
            });

            this.setOnMouseDragged(event -> {
                this.setLayoutX(this.x + event.getSceneX() - mouseX);
                this.setLayoutY(this.y +  event.getSceneY() - mouseY);

                highlightNearest(event.getSceneX(), event.getSceneY());
            });

            this.setOnMouseReleased(event -> {
                int[] array = snapToGrid(event.getSceneX(), event.getSceneY(), this);

                if ( highlighted != null) {
                    highlighted.setFill(Color.LIGHTGREY);
                    highlighted = null;
                }

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
                        String winTextFill = "" + this.type + " WINS!";
                        Text winText = new Text(150, 350, winTextFill);
                        winText.setFill(Color.BLACK);
                        winText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 120));
                        root.getChildren().add(winText);
                    }
                    //check if draw
                    else if (turns == 9) {
                        //end the game
                        String winTextFill = "DRAW!";
                        Text winText = new Text(190, 340, winTextFill);
                        winText.setFill(Color.BLACK);
                        winText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 120));
                        root.getChildren().add(winText);
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
