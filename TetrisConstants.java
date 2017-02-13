
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public interface TetrisConstants {

    Color I_Color = Color.rgb(36, 220, 220);
    Color J_Color = Color.rgb(36, 36, 220);
    Color L_Color = Color.rgb(220, 128, 36);
    Color O_Color = Color.rgb(220, 220, 36);
    Color S_Color = Color.rgb(36, 220, 36);
    Color T_Color = Color.rgb(128, 35, 128);
    Color Z_Color = Color.rgb(220, 36, 36);

    int BORDER_WIDTH = 4;
    int COL_COUNT = 12;
    int VISIBLE_ROW_COUNT = 24;
    int HIDDEN_ROW_COUNT = 2;
    int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
    int TILE_SIZE = 24;
    int SHADE_WIDTH = 4;

    int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    int SIDE_PANEL_WIDTH = 200;
    int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;

    int PREVIEW_TILE_SIZE = 18;
    int PREVIEW_SHADE_WIDTH = SHADE_WIDTH >> 1;

    int PLAYER1 = 1;
    int PLAYER2 = 2;

    int SQUARE_CENTER_X = 100;
    int SQUARE_CENTER_Y = 65;


    Font LARGE_FONT = Font.font("SansSerif", FontWeight.EXTRA_BOLD, 18);
    Font MEDIUM_FONT = Font.font("SansSerif", FontWeight.BOLD, 14);
    Font SMALL_FONT = Font.font("SansSerif", FontWeight.MEDIUM, 12);

    Color FONT_COLOR = Color.color(0.95, 0.85, 0.8, 0.8);

}
