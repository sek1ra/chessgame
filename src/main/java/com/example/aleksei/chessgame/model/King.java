package com.example.aleksei.chessgame.model;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {
    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Cell> getPsevdoAvailableMoves(Piece[][] pieces, int row, int col) {
        List<Cell> availMovies = new ArrayList<Cell>();

        int[] dr = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] dc = {0, 1, 1, 1, 0, -1, -1, -1};

        for (int i = 0; i < 8; i++) {
            int r = row + dr[i];
            int c = col + dc[i];

            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                Piece target = pieces[r][c];

                if (target == null || target.isWhite() != this.isWhite()) {
                    availMovies.add(new Cell(r, c));
                }
            }
        }
       
        return availMovies;
    }

    @Override
    public String getSymbol() {
        return "K";
    }

    @Override
    public String getImagePath() {
        if (isWhite()) {
            return "/com/example/aleksei/chessgame/images/w_king.png";
        } else {
            return "/com/example/aleksei/chessgame/images/b_king.png";
        }
    }

    @Override
    public List<Cell> getAttackedCells(Piece[][] pieces, int row, int col) {
        List<Cell> result = new ArrayList<>();

        int[] dirRow = {-1, -1, -1, 0, 1, 1, 1, 0};
        int[] dirCol = {-1, 0, 1, 1, 1, 0, -1, -1};

        for (int i = 0; i < 8; i++) {
            int r = row + dirRow[i];
            int c = col + dirCol[i];
            if (r >= 0 && r < 8 && c >= 0 && c < 8) {
                result.add(new Cell(r, c));
            }
        }

        return result;
    }
}
