

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;


public class InfoPane extends Pane implements TetrisConstants{

	private Tetris tetris;
	private VBox vBox;
	private boolean isPlayerPane;

	private Text gameScoreLabel = new Text("SCORE");
	private Text gameScoreText = new Text("0");
	private Text gameLevelLabel = new Text("LEVEL");
	private Text gameLevelText = new Text("1");
	private Text gameInstructionText = new Text();

	public InfoPane(Tetris tetris) {
		this.tetris = tetris;

		setPrefSize(SIDE_PANEL_WIDTH, PANEL_HEIGHT);
		setStyle("-fx-background-color: rgba(0, 40, 130, 0.4);" + "-fx-background-radius: 10; -fx-border-color: slateblue;");

	}


	public void displayStart() {

		if (isPlayerPane) {
			gameInstructionText.setText("USE ARROW KEYS\nTO MOVE\n\nUSE Z AND X TO ROTATE");
		} else {
			gameInstructionText.setText("  WAITING FOR PLAYER  ");
		}
			gameInstructionText.setTextAlignment(TextAlignment.CENTER);
			gameInstructionText.setFont(SMALL_FONT);
			gameInstructionText.setFill(FONT_COLOR);
			gameInstructionText.setLineSpacing(16);


			vBox = new VBox(20);
			vBox.setAlignment(Pos.CENTER);
			vBox.setPadding(new Insets(10, 10, 10, 10));

			vBox.getChildren().add(gameInstructionText);

			vBox.setLayoutX(15);
			vBox.setLayoutY(260);

			vBox.setStyle("-fx-background-color: rgba(0, 60, 120, 0.4); -fx-border-color: steelblue; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, darkslategray, 20, 0.4, 0, 0);");
			getChildren().add(vBox);

	}

	public void displayPreview() {

		getChildren().clear();


		gameScoreText.setText(Integer.toString(tetris.getScore()));

		gameLevelText.setText(Integer.toString(tetris.getLevel()));

		gameScoreLabel.setTextAlignment(TextAlignment.CENTER);
		gameScoreLabel.setFont(LARGE_FONT);
		gameScoreLabel.setFill(FONT_COLOR);
		gameScoreText.setTextAlignment(TextAlignment.CENTER);
		gameScoreText.setFont(LARGE_FONT);
		gameScoreText.setFill(FONT_COLOR);
		gameLevelLabel.setTextAlignment(TextAlignment.CENTER);
		gameLevelLabel.setFont(LARGE_FONT);
		gameLevelLabel.setFill(FONT_COLOR);
		gameLevelText.setTextAlignment(TextAlignment.CENTER);
		gameLevelText.setFont(LARGE_FONT);
		gameLevelText.setFill(FONT_COLOR);

		vBox = new VBox(30);
		vBox.setAlignment(Pos.CENTER);
		vBox.setPadding(new Insets(20, 20, 20, 20));

		vBox.getChildren().addAll(gameLevelLabel, gameLevelText, gameScoreLabel,gameScoreText);

		vBox.setLayoutX(45);
		vBox.setLayoutY(320);


		vBox.setStyle("-fx-background-color: rgba(0, 60, 120, 0.4); -fx-border-color: steelblue; -fx-border-width: 1; -fx-effect: dropshadow(three-pass-box, darkslategray, 20, 0.4, 0, 0);");
		getChildren().add(vBox);


		Tetrimino type = tetris.getNextPieceType();
		if(!tetris.isGameOver() && type != null) {

			int cols = type.getCols();
			int rows = type.getRows();
			int dimension = type.getDimension();
		

			int startX = (SQUARE_CENTER_X - (cols * PREVIEW_TILE_SIZE / 2));
			int startY = (SQUARE_CENTER_Y - (rows * PREVIEW_TILE_SIZE / 2));
		

			int top = type.getTopInset(0);
			int left = type.getLeftInset(0);
		

			for(int row = 0; row < dimension; row++) {
				for(int col = 0; col < dimension; col++) {
					if(type.isTile(col, row, 0)) {
						drawTile(type, startX + ((col - left) * PREVIEW_TILE_SIZE), startY + ((row - top) * PREVIEW_TILE_SIZE));
					}
				}
			}
		}
	}

	private void drawTile(Tetrimino type, int x, int y) {

		Rectangle rect = new Rectangle(x, y, PREVIEW_TILE_SIZE, PREVIEW_TILE_SIZE);
		rect.setFill(type.getBaseColor());

		Rectangle rectShading = new Rectangle(x, y + PREVIEW_TILE_SIZE - PREVIEW_SHADE_WIDTH, PREVIEW_TILE_SIZE, PREVIEW_SHADE_WIDTH);
		Rectangle rectShading1 = new Rectangle(x + PREVIEW_TILE_SIZE - PREVIEW_SHADE_WIDTH, y, PREVIEW_SHADE_WIDTH, PREVIEW_TILE_SIZE);
		rectShading.setFill(type.getBaseColor().darker());
		rectShading1.setFill(type.getBaseColor().darker());

		Rectangle rectHighlight = new Rectangle(x, y + PREVIEW_TILE_SIZE - PREVIEW_SHADE_WIDTH, PREVIEW_TILE_SIZE, PREVIEW_SHADE_WIDTH);
		Rectangle rectHighlight1 = new Rectangle(x + PREVIEW_TILE_SIZE - PREVIEW_SHADE_WIDTH, y, PREVIEW_SHADE_WIDTH, PREVIEW_TILE_SIZE);

		getChildren().addAll(rect, rectShading, rectShading1, rectHighlight, rectHighlight1);


		for(int i = 0; i < PREVIEW_SHADE_WIDTH; i++) {
			Line highlight = new Line(x, y + i, x + PREVIEW_TILE_SIZE - i - 1, y + i);
			Line highlight1 = new Line(x + i, y, x + i, y + PREVIEW_TILE_SIZE - i - 1);
			highlight.setStroke(type.getBaseColor().brighter());
			highlight.setStrokeWidth(0.5);
			highlight1.setStroke(type.getBaseColor().brighter());
			highlight1.setStrokeWidth(0.5);

			getChildren().addAll(highlight, highlight1);
		}


	}

	public void setIsPlayerPane() {
		isPlayerPane = true;
	}
}
