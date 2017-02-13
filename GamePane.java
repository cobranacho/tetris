


import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.scene.image.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;


public class GamePane extends Pane implements TetrisConstants {

	private Tetris tetris;
	private Tetrimino[][] tetriminos;
	private Text gameStartText;
	private Text infoText;
	private boolean isPlayerPane;

	Animation animation;

	public GamePane(Tetris tetris) {
		this.tetris = tetris;
		tetriminos = new Tetrimino[ROW_COUNT][COL_COUNT];

		setPrefSize(PANEL_WIDTH, PANEL_HEIGHT);
		setStyle("-fx-background-color: rgba(0, 40, 160, 0.4); -fx-border-color: steelblue; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, darkslategray, 20, 0.4, 0, 0);");



	}

	public void displayStartScreen() {

		Image image = new Image("tetris.png");

		StackPane imagePane = new StackPane(new ImageView(image));
		StackPane textPane = new StackPane();
		gameStartText = new Text(" ");
		gameStartText.setLineSpacing(36);
		gameStartText.setTextAlignment(TextAlignment.CENTER);
		gameStartText.setFont(LARGE_FONT);
		gameStartText.setFill(FONT_COLOR);
		textPane.getChildren().add(gameStartText);
		textPane.setPadding(new javafx.geometry.Insets(180, 0, 0, 0));

		VBox gameInfo = new VBox(imagePane, textPane);
		gameInfo.setLayoutX(PANEL_WIDTH / 2 - image.getWidth() / 2);
		gameInfo.setLayoutY(140);

		animation = new Timeline(new KeyFrame(Duration.millis(500), event -> flashText("Press Enter Key to Start")));
		animation.setCycleCount(Timeline.INDEFINITE);


		if (!tetris.getGameStatus() && !tetris.getWaitingStatus() && isPlayerPane == true) {
			animation.play();
			getChildren().add(gameInfo);
		} else {
			ImageView imageView = new ImageView(new javafx.scene.image.Image("moscow.jpg"));
			imageView.setStyle("-fx-opacity: 0.65;");
			getChildren().add(imageView);
		}

	}



	public void clear() {

		for(int i = 0; i < ROW_COUNT; i++) {
			for(int j = 0; j < COL_COUNT; j++) {
				tetriminos[i][j] = null;
			}
		}
	}

	public boolean isValidAndEmpty(Tetrimino type, int x, int y, int rotation) {

		// Ensure the piece is in a valid column.
		if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) > COL_COUNT) {
			return false;
		}
		
		// Ensure the piece is in a valid row.
		if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) > ROW_COUNT) {
			return false;
		}
		

		// Loop through every tile in the piece and see if it conflicts with an existing tile.
		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
					return false;
				}
			}
		}
		return true;
	}
	

	public void addPiece(Tetrimino type, int x, int y, int rotation) {

		for(int col = 0; col < type.getDimension(); col++) {
			for(int row = 0; row < type.getDimension(); row++) {
				if(type.isTile(col, row, rotation)) {
					setTetrimino(col + x, row + y, type);
				}
			}
		}
	}
	

	public int checkLines() {
		int completedLines = 0;
		

		for(int row = 0; row < ROW_COUNT; row++) {
			if(checkLine(row)) {
				completedLines++;
			}
		}
		return completedLines;
	}
			

	private boolean checkLine(int line) {

		for(int col = 0; col < COL_COUNT; col++) {
			if(!isOccupied(col, line)) {
				return false;
			}
		}

		// remove line and shift everything down
		for(int row = line - 1; row >= 0; row--) {
			for(int col = 0; col < COL_COUNT; col++) {
				setTetrimino(col, row + 1, getTetrimino(col, row));
			}
		}
		return true;
	}
	

	private boolean isOccupied(int x, int y) {
		return tetriminos[y][x] != null;
	}
	

	// Setter a tetrimino in column and row
	private void setTetrimino(int  x, int y, Tetrimino tetrimino) {
		tetriminos[y][x] = tetrimino;
	}
		


	// getter a tile in column and row
	private Tetrimino getTetrimino(int x, int y) {
		return tetriminos[y][x];
	}

	public void paintTetriminos() {

		// Clear all the children first
		getChildren().clear();

		if(tetris.isPaused()) {

			StackPane pausePane = new StackPane();
			infoText = new Text("Game Paused");
			infoText.setLineSpacing(36);
			infoText.setTextAlignment(TextAlignment.CENTER);
			infoText.setFont(LARGE_FONT);
			infoText.setFill(FONT_COLOR);
			getChildren().add(pausePane);

		} else if(tetris.isGameStatus() || tetris.isGameOver()) {
			displayStartScreen();

		} else {

			for(int x = 0; x < COL_COUNT; x++) {
				for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
					Tetrimino tile = getTetrimino(x, y);
					if(tile != null) {
						drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE);
					}

				}
			}

			Tetrimino type = tetris.getPieceType();
			int pieceCol = tetris.getPieceCol();
			int pieceRow = tetris.getPieceRow();
			int rotation = tetris.getPieceRotation();
			
			// Draw the tetrimino onto the board.
			for(int col = 0; col < type.getDimension(); col++) {
				for(int row = 0; row < type.getDimension(); row++) {
				if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
						drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE);
					}
				}
			}
		}

	}

	private void drawTile(Tetrimino tetrimino, int x, int y) {
		drawTile(tetrimino.getBaseColor(), x, y);
	}
	

	private void drawTile(Color base, int x, int y) {

		Rectangle rect = new Rectangle(x, y, TILE_SIZE, TILE_SIZE );
		rect.setFill(base);

		Rectangle rectShading = new Rectangle(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		Rectangle rectShading1 = new Rectangle(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		rectShading.setFill(base.darker());
		rectShading1.setFill(base.darker());

		Rectangle rectHighlight = new Rectangle(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
		Rectangle rectHighlight1 = new Rectangle(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);

		getChildren().addAll(rect, rectShading, rectShading1, rectHighlight, rectHighlight1);


		for(int i = 0; i < SHADE_WIDTH; i++) {
			Line highlight = new Line(x, y + i, x + TILE_SIZE - i - 1, y + i);
			Line highlight1 = new Line(x + i, y, x + i, y + TILE_SIZE - i - 1);
			highlight.setStroke(base.brighter());
			highlight.setStrokeWidth(1);
			highlight1.setStroke(base.brighter());
			highlight1.setStrokeWidth(1);

			getChildren().addAll(highlight, highlight1);
		}

	}

	public void setIsPlayerPane() {
		this.isPlayerPane = true;
	}

	private void flashText(String text) {
		String temp = gameStartText.getText();
		if (temp == text) {
			gameStartText.setText(" ");
		} else {
			gameStartText.setText(text);
		}
	}
}
