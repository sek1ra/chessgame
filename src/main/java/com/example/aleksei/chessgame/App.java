package com.example.aleksei.chessgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.aleksei.chessgame.controller.GameController;
import com.example.aleksei.chessgame.model.Game;
import com.example.aleksei.chessgame.view.BoardView;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;


/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Game game = new Game();
        BoardView view = new BoardView();
        new GameController(game, view);

        ListView<String> movesList = new ListView<>();
        movesList.setPrefHeight(140);
        movesList.setFocusTraversable(false);

        BorderPane root = new BorderPane();
        root.setCenter(view.getGridPane());
        root.setBottom(movesList);

        Scene scene = new Scene(root, 480, 620); // высота больше из-за полоски
        stage.setScene(scene);

        stage.setTitle("Chess Board");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}