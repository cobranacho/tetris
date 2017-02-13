import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Created by james on 5/31/2016.
 */

public class TetrisApp extends Application implements TetrisConstants {
    private Tetris tetris;
    private Tetris competitorTetris;

    // May crash on Linux machines
    private final URL resource = getClass().getResource("song.mp3");
    private final Media media = new Media(resource.toString());
    private final MediaPlayer mediaPlayer = new MediaPlayer(media);


    @Override
    public void start(Stage primaryStage) throws Exception {
        // System.out.println(PANEL_HEIGHT);
        // System.out.println(PANEL_WIDTH);

        tetris = new Tetris();
        competitorTetris = new Tetris();
        GamePane gamePane = tetris.getTetrisGamePane();
        InfoPane infoPane = tetris.getTetrisInfoPane();
        infoPane.setIsPlayerPane();
        infoPane.displayStart();
        gamePane.setIsPlayerPane();
        gamePane.displayStartScreen();

        tetris.connectToServer();

        GamePane gamePane2 = competitorTetris.getTetrisGamePane();
        InfoPane infoPane2 = competitorTetris.getTetrisInfoPane();
        infoPane2.displayStart();
        gamePane2.displayStartScreen();


        HBox hBoxGame1 = new HBox();
        hBoxGame1.getChildren().addAll(gamePane, infoPane);
        HBox hBoxGame2 = new HBox();
        hBoxGame2.getChildren().addAll(gamePane2, infoPane2);

        HBox gameHBox = new HBox();
        gameHBox.setStyle("-fx-background-image: url('colorful.jpg');");
        gameHBox.getChildren().addAll(hBoxGame1, hBoxGame2);

        Scene scene = new Scene(gameHBox, (PANEL_WIDTH + SIDE_PANEL_WIDTH - 16) * 2, PANEL_HEIGHT - 20);
        primaryStage.setTitle("Tetris");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();


        mediaPlayer.setVolume(0.4);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);


        scene.setOnKeyReleased(event1 -> {
            switch (event1.getCode()) {

                case DOWN:
                    tetris.timerRestore();
                    break;
            }
        });


        scene.setOnKeyPressed(event -> {
            // System.out.println(event.getCode());
            switch (event.getCode()) {
                case ENTER:
                    if (tetris.getGameOver() && !tetris.getWaitingStatus()) {
                        tetris.startGame();
                        tetris.resetGame();
                        startMusic();
                    }
                    break;
                case DOWN:
                     tetris.moveDrop();
                    break;
                case LEFT:
                    tetris.moveLeft();
                    break;
                case RIGHT:
                    tetris.moveRight();
                    break;
                case Z:
                    tetris.rotateClockWise();
                    break;
                case X:
                    tetris.rotateCounterClockWise();
                    break;
            }
        });
    }

    private void startMusic() {

        Thread music = new Thread(new RunMusic());
        music.start();

    }
    private class RunMusic implements Runnable {

        @Override
        public void run() {
            try {
                while (true) {
                    mediaPlayer.play();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
