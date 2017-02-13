import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Created by james on 6/1/2016.
 */
public class TetrisServer extends Application implements TetrisConstants {
    private int sessionNo = 1;
    private int endOfGame = 0;

    private boolean continueToPlay = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        TextArea taLog = new TextArea();

        Scene scene = new Scene(new ScrollPane(taLog), 480, 240);
        primaryStage.setTitle("Tetris Server");
        primaryStage.setScene(scene);
        primaryStage.show();

        new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> taLog.appendText(new Date() + ": Server started at socket 8000\n"));

                while (true) {
                    Platform.runLater(() -> taLog.appendText(new Date() + ": Wait for players to join session" + sessionNo + '\n'));

                    Socket player1 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() + ": Player 1 joined session " + sessionNo + '\n');
                        taLog.appendText("Player 1's IP address" + player1.getInetAddress().getHostAddress() + '\n');
                    });

                    new ObjectOutputStream(player1.getOutputStream()).writeInt(PLAYER1);

                    Socket player2 = serverSocket.accept();

                    Platform.runLater(() -> {
                        taLog.appendText(new Date() + ": Player 2 joined session " + sessionNo + '\n');
                        taLog.appendText("Player 2's IP address" + player1.getInetAddress().getHostAddress() + '\n');
                    });

                    new ObjectOutputStream(player2.getOutputStream()).writeInt(PLAYER2);

                    Platform.runLater(() -> taLog.appendText(new Date() + ": Start a thread for session " + sessionNo++ + '\n'));

                    if (!continueToPlay) {
                        if (endOfGame == 1) {
                            Platform.runLater(() -> taLog.appendText("Game Over for Player 1"));
                        } else {
                            Platform.runLater(() -> taLog.appendText("Game Over for Player 2"));
                        }
                    }

                    new Thread(new HandleASession(player1, player2)).start();
                }

            } catch (IOException ex) {
                System.out.println("Client Connection Closed");
            }
        }).start();

    }

    class HandleASession implements Runnable, TetrisConstants {
        private Socket player1;
        private Socket player2;


        private ObjectInputStream fromPlayer1;
        private ObjectOutputStream toPlayer1;

        private ObjectInputStream fromPlayer2;
        private ObjectOutputStream toPlayer2;


        private int player1Score;
        private int player2Score;

        public HandleASession(Socket player1, Socket player2) {
            this.player1 = player1;
            this.player2 = player2;

            System.out.println("Running new server thread to handle session");

        }
        @Override
        public void run() {
            try {
                // Create data input and output streams
                fromPlayer1 = new ObjectInputStream(player1.getInputStream());
                toPlayer1 = new ObjectOutputStream(player1.getOutputStream());

                fromPlayer2 = new ObjectInputStream(player2.getInputStream());
                toPlayer2 = new ObjectOutputStream(player2.getOutputStream());


                while (continueToPlay) {

                    // Check for player1 end of game status
                    endOfGame = fromPlayer1.readInt();

                    // get player1Score
                    player1Score = fromPlayer1.readInt();
                    // Receive the Tetrimino array from player 1
                    Object player1Object = fromPlayer1.readObject();

                    toPlayer2.writeObject(player1Object);
                    toPlayer2.writeInt(player1Score);

                    Thread.sleep(200);

                    // Check for player2 end of game status
                    endOfGame = fromPlayer2.readInt();
                    // get player2Score
                    player2Score = fromPlayer2.readInt();
                    // Receive the Tetrimino array from player 2
                    Object player2Object = fromPlayer2.readObject();

                    toPlayer1.writeObject(player2Object);
                    toPlayer1.writeInt(player2Score);


                    if (endOfGame > 0) {
                        continueToPlay = false;
                    }
                }
            } catch (IOException ex) {
                System.out.println("Client Connection Closed");
            } catch (ClassNotFoundException ex) {
                System.out.println("Network Communication Error");
            } catch (InterruptedException ex) {
                System.out.println("Waiting for Network");
            }
        }
    }
}
