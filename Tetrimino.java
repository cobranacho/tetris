
import javafx.scene.paint.Color;

public enum Tetrimino implements TetrisConstants {


	I(I_Color, 4, 4, 1, new boolean[][] {
		{
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false,
			false,	false,	false,	false
		},
		{
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false,
			false,	false,	true,	false
		},
		{
			false,	false,	false,	false,
			false,	false,	false,	false,
			true,	true,	true,	true,
			false,	false,	false,	false
		},
		{
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false,
			false,	true,	false,	false
		}
	}),
	

	J(J_Color, 3, 3, 2, new boolean[][] {
		{
			true,	false,	false,
			true,	true,	true,
			false,	false,	false
		},
		{
			false,	true,	true,
			false,	true,	false,
			false,	true,	false
		},
		{
			false,	false,	false,
			true,	true,	true,
			false,	false,	true
		},
		{
			false,	true,	false,
			false,	true,	false,
			true,	true,	false
		}
	}),
	

	L(L_Color, 3, 3, 2, new boolean[][] {
		{
			false,	false,	true,
			true,	true,	true,
			false,	false,	false
		},
		{
			false,	true,	false,
			false,	true,	false,
			false,	true,	true
		},
		{
			false,	false,	false,
			true,	true,	true,
			true,	false,	false
		},
		{
			true,	true,	false,
			false,	true,	false,
			false,	true,	false
		}
	}),
	

	O(O_Color, 2, 2, 2, new boolean[][] {
		{true,	true, true,	true},
		{true,	true, true,	true},
		{true,	true, true,	true},
		{true,	true, true,	true}
	}),
	

	S(S_Color, 3, 3, 2, new boolean[][] {
		{
			false,	true,	true,
			true,	true,	false,
			false,	false,	false
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	false,	true
		},
		{
			false,	false,	false,
			false,	true,	true,
			true,	true,	false
		},
		{
			true,	false,	false,
			true,	true,	false,
			false,	true,	false
		}
	}),
	

	T(T_Color, 3, 3, 2, new boolean[][] {
		{
			false,	false,	false,
			true,	true,	true,
			false,	true,	false
		},
		{
			false,	true,	false,
			false,	true,	true,
			false,	true,	false
		},
		{
			false,	true,	false,
			true,	true,	true,
			false,	false,	false
		},
		{
			false,	true,	false,
			true,	true,	false,
			false,	true,	false
		}
	}),
	

	Z(Z_Color, 3, 3, 2, new boolean[][] {
		{
			true,	true,	false,
			false,	true,	true,
			false,	false,	false
		},
		{
			false,	false,	true,
			false,	true,	true,
			false,	true,	false
		},
		{
			false,	false,	false,
			true,	true,	false,
			false,	true,	true
		},
		{
			false,	true,	false,
			true,	true,	false,
			true,	false,	false
		}
	});
		

	private Color baseColor;
	private int spawnCol;
	private int spawnRow;

	private int dimension;
	private int rows;
	private int cols;

	private boolean[][] tetriminos;
	

	Tetrimino(Color color, int dimension, int cols, int rows, boolean[][] tetriminos) {
		this.baseColor = color;
		this.dimension = dimension;
		this.tetriminos = tetriminos;
		this.cols = cols;
		this.rows = rows;

		this.spawnCol = 5 - (dimension >> 1);
		this.spawnRow = getTopInset(0);
	}
	

	public Color getBaseColor() {
		return baseColor;
	}

	public int getDimension() {
		return dimension;
	}

	public int getSpawnColumn() {
		return spawnCol;
	}

	public int getSpawnRow() {
		return spawnRow;
	}

	public int getRows() {
		return rows;
	}

	public int getCols() {
		return cols;
	}

	public boolean isTile(int x, int y, int rotation) {
		return tetriminos[rotation][y * dimension + x];
	}

	public int getLeftInset(int rotation) {

		for(int x = 0; x < dimension; x++) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return x;
				}
			}
		}
		return -1;
	}

	public int getRightInset(int rotation) {

		for(int x = dimension - 1; x >= 0; x--) {
			for(int y = 0; y < dimension; y++) {
				if(isTile(x, y, rotation)) {
					return dimension - x -1;
				}
			}
		}
		return -1;
	}

	public int getTopInset(int rotation) {

		for(int y = 0; y < dimension; y++) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return y;
				}
			}
		}
		return -1;
	}
	

	public int getBottomInset(int rotation) {

		for(int y = dimension - 1; y >= 0; y--) {
			for(int x = 0; x < dimension; x++) {
				if(isTile(x, y, rotation)) {
					return dimension - y -1;
				}
			}
		}
		return -1;
	}
	
}
