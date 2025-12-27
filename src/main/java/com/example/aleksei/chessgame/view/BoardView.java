package com.example.aleksei.chessgame.view;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.example.aleksei.chessgame.model.Cell;
import com.example.aleksei.chessgame.model.Piece;
import com.example.aleksei.chessgame.interfaces.IBoardView;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;

public class BoardView implements IBoardView {
    Consumer<Cell> onCellClicked;
    private Button[][] buttons = new Button[8][8];
    GridPane grid = new GridPane();
    private final Map<String, Image> PIECESIMAGES;
    private final ListView<String> moveList = new ListView<>();
    private final BorderPane root = new BorderPane();

    public BoardView() {
        grid.setPrefSize(480, 480);
        String cssPath = getClass().getResource("/com/example/aleksei/chessgame/css/styles.css").toExternalForm();
        grid.getStylesheets().add(cssPath);

        PIECESIMAGES = new HashMap<>();
        String[] piecesNames = {"bishop", "king", "knight", "pawn", "queen", "rook"};
        for (String piecesName : piecesNames) {
            Image img = new Image(getClass().getResource("/com/example/aleksei/chessgame/images/b_"+piecesName+".png").toExternalForm());
            PIECESIMAGES.put("b_" + piecesName, img);
            img = new Image(getClass().getResource("/com/example/aleksei/chessgame/images/w_"+piecesName+".png").toExternalForm());
            PIECESIMAGES.put("w_" + piecesName, img);
        }

        StackPane stack = new StackPane();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(60, 60);
                buttons[i][j].setOnAction(this::handleClick);
                buttons[i][j].setGraphic(stack);
                grid.add(buttons[i][j], j, i);
            }
        }
        moveList.setPrefHeight(120);
        root.setCenter(grid);      // шахматная доска
        root.setBottom(moveList);
    }

    @Override
    public void handleClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        int row = GridPane.getRowIndex(clickedButton);
        int col = GridPane.getColumnIndex(clickedButton);
        System.out.println("Clicked cell: [" + row + "," + col + "]");
        handleClick(row, col);
    }

    @Override
    public void draw(Piece[][] pieces) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (pieces[i][j] != null) {
                    String imageName = pieces[i][j].isWhite() ? "w_" : "b_";
                    imageName += pieces[i][j].getClass().getSimpleName().toLowerCase();
                    Image img = PIECESIMAGES.get(imageName);
                    buttons[i][j].setGraphic(getStackPane(img, false));
                } else {
                    buttons[i][j].setGraphic(getStackPane(null, false));
                }

                buttons[i][j].getStyleClass().removeAll("cellBlack", "cellWhite");
                if ((i + j) % 2 == 0) {
                    buttons[i][j].getStyleClass().add("cellBlack");
                } else {
                    buttons[i][j].getStyleClass().add("cellWhite");
                }
            }
        }
    }

    @Override
    public void setOnCellClicked(Consumer<Cell> handler) {
        onCellClicked = handler;
    }

    @Override
    public void handleClick(int row, int col) {
        if (onCellClicked != null) {
            onCellClicked.accept(new Cell(row, col));
        }
    }

    @Override
    public GridPane getGridPane() {
        return grid;
    }

    @Override
    public void clearSelection() {
        for (int k = 0; k < 8; k++) {
            for (int l = 0; l < 8; l++) {
                buttons[k][l].getStyleClass().remove("cellActive");
                buttons[k][l].getStyleClass().remove("cellAvailable");

                /*Node graphic = buttons[k][l].getGraphic();
                System.out.println(graphic.getClass());
                if (graphic instanceof Circle) {
                    buttons[k][l].setGraphic(null);
                }*/

                if (buttons[k][l].getGraphic() instanceof StackPane) {
                    StackPane pane = (StackPane) buttons[k][l].getGraphic();
                    /*for (Node child : pane.getChildren()) {
                        if (child instanceof Circle) {
                            buttons[k][l].setGraphic(null);
                            break;
                        }
                    }*/
                    pane.getChildren().removeIf(node -> node instanceof Circle);
                }
            }
        }
    }

    @Override
    public void highlightMoves(List<Cell> availMovies) {
        clearSelection();
        if (availMovies != null && availMovies.size() > 0) {
            for (Cell availMove : availMovies) {
                Node stack = buttons[availMove.row()][availMove.col()].getGraphic();
                if (stack instanceof StackPane stackPane && !stackPane.getChildren().isEmpty()) {
                    Node node = stackPane.getChildren().get(0);
                    if (node instanceof ImageView imageView) {
                        buttons[availMove.row()][availMove.col()].setGraphic(getStackPane(imageView.getImage(), true));
                    } else {
                        buttons[availMove.row()][availMove.col()].setGraphic(getStackPane(null, true));
                    }
                } else {
                    buttons[availMove.row()][availMove.col()].setGraphic(getStackPane(null, true));
                }
            }
        }
    }

    private StackPane getStackPane(Image img, boolean isCircle) {
        StackPane stack = new StackPane();
        ImageView view = new ImageView();

        if (img != null) {
            view = new ImageView(img);
            view.setFitWidth(30);
            view.setFitHeight(30);
            view.setPreserveRatio(true);
            stack.getChildren().add(view);
        }

        Circle dot = null;
        if (isCircle) {
            dot = new Circle(8);
            dot.setFill(Color.BLACK);
            dot.setOpacity(0.3);
            stack.getChildren().add(dot);
        }

        return stack;
    }

    public ListView<String> getMoveList() {
        return moveList;
    }

    public void addMove(String moveText) {
        moveList.getItems().add(moveText);
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BorderPane getRoot() {
        return root;
    }
}
