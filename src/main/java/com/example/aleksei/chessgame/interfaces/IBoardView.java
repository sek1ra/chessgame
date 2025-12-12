package com.example.aleksei.chessgame.interfaces;

import java.util.List;
import java.util.function.Consumer;

import com.example.aleksei.chessgame.model.Cell;
import com.example.aleksei.chessgame.model.Piece;

import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;

public interface IBoardView {
    void handleClick(ActionEvent event);
    void draw(Piece[][] pieces);
    void setOnCellClicked(Consumer<Cell> handler);
    void handleClick(int row, int col);
    GridPane getGridPane();
    void clearSelection();
    void highlightMoves(List<Cell> availMovies);
}
