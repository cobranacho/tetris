
import javafx.application.Platform;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class Tetris implements TetrisConstants {

    private boolean waitingForCompetitor;
    private boolean sessionToken;

    private int currentPlayer;
    private int gameLevel;
    private int gameScore;
    private int completeLines;
    private long gameSpeed;
    private Timer timer;


    private ObjectInputStream fromServer;
    private ObjectOutputStream toServer;

    private static final int TYPE_COUNT = 7;
    private GamePane gamePane;
    private InfoPane infoPane;
    private boolean isPaused;
    private boolean gameStatus;
    private boolean gameOver = true;
    private int level;
    private int score;

    private Random random;
    private Tetrimino currentType;
    private Tetrimino nextType;

    private int currentCol;
    private int currentRow;
    private int currentRotation;

    private Thread gameThread;
;

    public Tetris() {
        this.gamePane = new GamePane(this);
        this.infoPane = new InfoPane(this);

    }


    public void startGame() {

        random = new Random();

        gameStatus = true;
        gameSpeed = 500;


        timer = new Timer(gameSpeed);


        gameThread = new Thread(timer);
        gameThread.start();



        renderGame();

    }


    public class Timer implements Runnable {
        private long speed;
        private boolean gameContinue = true;

        public Timer(long speed) {
            this.speed = speed;
        }

        public void stopTime() {
            gameContinue = false;
        }

        public void setSpeed(long newSpeed) {
            this.speed = newSpeed;
        }

        public void resetTimer() {
            this.speed = gameSpeed;
        }

        @Override
        public void run() {
            try {
                while (gameContinue) {
                    Thread.sleep(speed);
                     updateGame();
                     renderGame();
                }
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void updateGame() {

        if (gamePane.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
            //Increment the current row if the move is valid
            currentRow++;
        } else {

            // Add a new tetrimino
            gamePane.addPiece(currentType, currentCol, currentRow, currentRotation);

            // check for completed lines and tabulate new score
            int cleared = gamePane.checkLines();
            if (cleared > 0) {
                score += 50 << cleared;
                gameSpeed -= cleared * 10;
                timer.setSpeed(gameSpeed);
                completeLines += cleared;

                if (completeLines % 20 == 0) {
                    gameLevel++;
                }
            }
            generateTetrimino();
        }
    }


    private void  renderGame() {

         Platform.runLater(() -> gamePane.paintTetriminos());
        Platform.runLater(() -> infoPane.displayPreview());

        gamePane.requestLayout();
        infoPane.requestLayout();
    }

    public void resetGame() {
        level = 1;
        score = 0;
        nextType = Tetrimino.values()[random.nextInt(TYPE_COUNT)];
        gameStatus = false;
        gameOver = false;
        gamePane.clear();

        generateTetrimino();
    }

    private void generateTetrimino() {

        try {
            gameThread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        this.currentType = nextType;
        this.currentCol = currentType.getSpawnColumn();
        this.currentRow = currentType.getSpawnRow();
        this.currentRotation = 0;
        this.nextType = Tetrimino.values()[random.nextInt(TYPE_COUNT)];

		// if not valid, it means game if over
        if (!gamePane.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
            gameOver = true;
        }
    }

    private void rotatePiece(int newRotation) {

        int newColumn = currentCol;
        int newRow = currentRow;

        int left = currentType.getLeftInset(newRotation);
        int right = currentType.getRightInset(newRotation);
        int top = currentType.getTopInset(newRotation);
        int bottom = currentType.getBottomInset(newRotation);

        if (currentCol < -left) {
            newColumn -= currentCol - left;
        } else if (currentCol + currentType.getDimension() - right >= GamePane.COL_COUNT) {
            newColumn -= (currentCol + currentType.getDimension() - right) - GamePane.COL_COUNT + 1;
        }

        if (currentRow < -top) {
            newRow -= currentRow - top;
        } else if (currentRow + currentType.getDimension() - bottom >= GamePane.ROW_COUNT) {
            newRow -= (currentRow + currentType.getDimension() - bottom) - GamePane.ROW_COUNT + 1;
        }

        if (gamePane.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
            currentRotation = newRotation;
            currentRow = newRow;
            currentCol = newColumn;
        }
    }


    public void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 8000);
            System.out.println("Local port: " + socket.getLocalPort());

            fromServer = new ObjectInputStream(socket.getInputStream());
            toServer = new ObjectOutputStream(socket.getOutputStream());

        } catch (IOException ex) {
            System.out.println("Server Connection Failure");
        }

        new Thread(() -> {
            try {
                int player = fromServer.readInt();

                if (player == PLAYER1) {
                    toServer.write(0);
                }


            } catch (IOException ex) {
                System.out.println("Network Communication Error");
            }

        }).start();

    }

    public boolean isPaused() {
        return isPaused;
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public boolean isGameStatus() {
        return gameStatus;
    }

    public int getScore() {
        return score;
    }

    public int getLevel() {
        return level;
    }

    public Tetrimino getPieceType() {
        return currentType;
    }

    public Tetrimino getNextPieceType() {
        return nextType;
    }

    public int getPieceCol() {
        return currentCol;
    }

    public int getPieceRow() {
        return currentRow;
    }

    public int getPieceRotation() {
        return currentRotation;
    }


    public GamePane getTetrisGamePane() {
        return gamePane;
    }

    public InfoPane getTetrisInfoPane() {
        return infoPane;
    }


    public boolean getGameStatus() {
        return gameStatus;
    }


    public boolean getWaitingStatus() {
        return waitingForCompetitor;
    }

    public void moveDrop() {
        timer.setSpeed(35);

    }

    public void moveLeft() {
        if (!isPaused && gamePane.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
            currentCol--;
        }
    }

    public void moveRight() {
        if (!isPaused && gamePane.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
            currentCol++;
        }
    }

    public void rotateCounterClockWise() {

        if (!isPaused) {
            rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
        }
    }

    public void rotateClockWise() {

        if (!isPaused) {
            rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
        }
    }


    public boolean getGameOver() {
        return gameOver;
    }

    public long getCurrentGameSpeed() {
        return gameSpeed;
    }

    public void timerRestore() {
        timer.setSpeed(getCurrentGameSpeed());
    }

    public void pauseGame() {
        if (!gameOver && !gameStatus) {
            isPaused = !isPaused;
           // timer.stopTime();
        }
    }

    public void restart() {
        if (gameOver || gameStatus) {
            startGame();
            resetGame();
        }
    }

}