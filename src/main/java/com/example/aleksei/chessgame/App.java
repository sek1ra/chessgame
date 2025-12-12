package com.example.aleksei.chessgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.example.aleksei.chessgame.controller.GameController;
import com.example.aleksei.chessgame.model.Game;
import com.example.aleksei.chessgame.view.BoardView;

/**
 * JavaFX App
 */
public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Game game = new Game();
        BoardView view = new BoardView();
        new GameController(game, view);

        Scene scene = new Scene(view.getGridPane(), 480, 480);
        stage.setScene(scene);
        stage.setTitle("Chess Board");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}