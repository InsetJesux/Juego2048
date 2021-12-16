public class Board {
	private int[][] board;
	private int horizontalLength;
	private int verticalLength;
	private BoardState boardState = BoardState.InProgress;
	private int score = 0;

	/**
	 * Constructor de la clase
	 * 
	 * @param horizontalLength Longitud horizontal del tablero
	 * @param verticalLength   Longitud vertical del tablero
	 * @param maxInitialValue  Indica la potencia maxima a la que se elevara la base
	 *                         2 para generar el numero
	 */
	public Board(int horizontalLength, int verticalLength, int maxInitialValue) {
		this.horizontalLength = horizontalLength;
		this.verticalLength = verticalLength;

		// Iniciar tablero
		this.createBoard(maxInitialValue);
	}
	
	/***
	 * Getter de score
	 * 
	 * @return Devuelve la puntuacion del tablero
	 */
	public int getScore() {
		return score;
	}
	
	/***
	 * Getter de board
	 * 
	 * @return Devuelve el tablero
	 */
	public int[][] getBoard() {
		return board;
	}
	
	/**
	 * Getter de boardState
	 * 
	 * @return Devuelve el estado del tablero
	 */
	public BoardState getBoardState() {
		return boardState;
	}

	/**
	 * Crea el estado inicial del tablero
	 * 
	 * @param maxInitialValue Indica la potencia maxima a la que se elevara la base
	 *                        2 para generar el numero, minimo 0, maximo 10
	 */
	private void createBoard(int maxInitialValue) {
		// Inicializamos el tablero
		board = new int[verticalLength][horizontalLength];

		// Comprobamos que el valor no pueda sobrepasar 2048 y que sea positivo
		maxInitialValue = Math.abs(maxInitialValue);
		maxInitialValue = maxInitialValue > 10 ? 10 : maxInitialValue;

		// Calcula cuantos numeros iniciales deberia tener el tablero
		int numeroTilesIniciales = (int) ((2.0 * (verticalLength * horizontalLength)) / 16.0);

		// Generamos los numeros aleatorios de base 2 iniciales
		for (int i = 0; i < numeroTilesIniciales; i++) {
			generateNewNumber(maxInitialValue);
		}
	}

	/**
	 * Muestra el tablero por consola
	 */
	public void showBoard() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int position = board[i][j];
				System.out.printf("%-4s", position == 0 ? "" : position);
				System.out.print(" ");
			}
			System.out.println("");
		}
	}

	/**
	 * Mueve los tiles sumando los que son iguales en la direccion indicada y si hay movimiento genera el numero aleatorio
	 * 
	 * @param direction Direccion en la que mover los tiles
	 */
	public void moveTiles(Direction direction) {
		boolean hasMoved = false;
		
		switch (direction) {
		case Up:
			hasMoved = moveTilesUp();
			break;
		case Down:
			hasMoved = moveTilesDown();
			break;
		case Right:
			hasMoved = moveTilesRight();
			break;
		case Left:
			hasMoved = moveTilesLeft();
			break;
		}

		// Actualizamos el estado del tablero
		boardState = checkBoardState();

		// Si el juego ha terminado no continuamos
		if (boardState != BoardState.InProgress) {
			return;
		}

		// Comprobamos si queda algun hueco vacio para generar un numero nuevo
		boolean zeroLeft = false;

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 0)
					zeroLeft = true;
			}
		}

		if (zeroLeft && hasMoved)
			generateNewNumber(1);
	}

	/***
	 * Mueve los tiles a la izquierda y suma los que son iguales
	 * 
	 * @return Devuelve true si algun tile se ha movido
	 */
	private boolean moveTilesLeft() {
		boolean hasMoved = false;
		
		// Elimina los ceros a la izquierda y entre medio de los tiles
		for (int i = 0; i < board.length; i++) {
			int[] filaTemporal = new int[board[i].length];
			int indice = 0;

			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] != 0)
					filaTemporal[indice++] = board[i][j];
			}
			
			// Comprobar si el array original y el temporal han cambiado
			for (int j = 0; j < filaTemporal.length; j++) {
				if (filaTemporal[j] != board[i][j]) hasMoved = true;
			}

			System.arraycopy(filaTemporal, 0, board[i], 0, board[i].length);
		}

		// Si coinciden 2 valores seguidos, multiplicamos el de la izquierda por 2 y
		// movemos el resto un hueco a la izquierda sobreescribiendo el 2º valor
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length - 1; j++) {
				if (board[i][j] == board[i][j + 1]) {
					board[i][j] *= 2;

					// Al combinarse dos baldosas hay movimiento
					if (board[i][j] != 0) hasMoved = true;
					
					// Sumamos la puntuacion al jugador
					score += board[i][j];
					
					// Mueve el resto hacia la izquierda, sobreescribiendo el valor que ya se ha
					// añadido al primero
					for (int k = j + 1; k < board[i].length - 1; k++) {
						board[i][k] = board[i][k + 1];
					}

					// Eliminamos el valor del ultimo tile
					board[i][horizontalLength - 1] = 0;
				}
			}
		}
		
		return hasMoved;
	}

	
	/***
	 * Mueve los tiles a la derecha y suma los que son iguales
	 * 
	 * @return Devuelve true si algun tile se ha movido
	 */
	private boolean moveTilesRight() {
		boolean hasMoved = false;
		
		// Elimina los ceros a la derecha y entre medio de los tiles
		for (int i = 0; i < board.length; i++) {
			int[] filaTemporal = new int[board[i].length];
			int indice = board[i].length - 1;

			for (int j = board[i].length - 1; j >= 0; j--) {
				if (board[i][j] != 0)
					filaTemporal[indice--] = board[i][j];
			}
			
			// Comprobar si el array original y el temporal han cambiado
			for (int j = 0; j < filaTemporal.length; j++) {
				if (filaTemporal[j] != board[i][j]) hasMoved = true;
			}

			System.arraycopy(filaTemporal, 0, board[i], 0, board[i].length);
		}

		// Si coinciden 2 valores seguidos, multiplicamos el de la derecha por 2 y
		// movemos el resto un hueco a la derecha sobreescribiendo el 2º valor
		for (int i = 0; i < board.length; i++) {
			for (int j = board[i].length - 1; j > 0; j--) {
				if (board[i][j] == board[i][j - 1]) {
					board[i][j] *= 2;

					// Al combinarse dos baldosas hay movimiento
					if (board[i][j] != 0) hasMoved = true;
					
					// Sumamos la puntuacion al jugador
					score += board[i][j];

					// Mueve el resto hacia la derecha, sobreescribiendo el valor que ya se ha
					// añadido al primero
					for (int k = j - 1; k > 0; k--) {
						board[i][k] = board[i][k - 1];
					}

					// Eliminamos el valor del ultimo tile
					board[i][0] = 0;
				}
			}
		}
		
		return hasMoved;
	}

	/***
	 * Mueve los tiles hacia arriba y suma los que son iguales
	 * 
	 * @return Devuelve true si algun tile se ha movido
	 */
	private boolean moveTilesUp() {
		boolean hasMoved = false;
		
		// Elimina los ceros arriba y entre medio de los tiles
		for (int i = 0; i < horizontalLength; i++) {
			int[] columnaTemporal = new int[verticalLength];
			int indice = 0;

			for (int j = 0; j < verticalLength; j++) {
				if (board[j][i] != 0) {
					columnaTemporal[indice++] = board[j][i];
				}
			}

			for (int j = 0; j < columnaTemporal.length; j++) {
				// Comprobar si el array original y el temporal han cambiado
				if (columnaTemporal[j] != board[j][i]) hasMoved = true;
				
				board[j][i] = columnaTemporal[j];
			}
		}

		// Si coinciden 2 valores seguidos, multiplicamos el de arriba por 2 y
		// movemos el resto un hueco hacia arriba sobreescribiendo el 2º valor
		for (int i = 0; i < horizontalLength; i++) {
			for (int j = 0; j < verticalLength - 1; j++) {
				if (board[j][i] == board[j + 1][i]) {
					board[j][i] *= 2;

					// Al combinarse dos baldosas hay movimiento
					if (board[j][i] != 0) hasMoved = true;
					
					// Sumamos la puntuacion al jugador
					score += board[j][i];

					// Mueve el resto hacia arriba, sobreescribiendo el valor que ya se ha añadido
					// al primero
					for (int k = j + 1; k < verticalLength - 1; k++) {
						board[k][i] = board[k + 1][i];
					}

					// Eliminamos el valor del ultimo tile
					board[verticalLength - 1][i] = 0;
				}
			}
		}
		
		return hasMoved;
	}

	/***
	 * Mueve los tiles hacia abajo y suma los que son iguales
	 * 
	 * @return Devuelve true si algun tile se ha movido
	 */
	private boolean moveTilesDown() {
		boolean hasMoved = false;
		
		// Elimina los ceros abajo y entre medio de los tiles
		for (int i = 0; i < horizontalLength; i++) {
			int[] columnaTemporal = new int[verticalLength];
			int indice = verticalLength - 1;

			for (int j = verticalLength - 1; j >= 0; j--) {
				if (board[j][i] != 0) {
					columnaTemporal[indice--] = board[j][i];
				}
			}

			for (int j = 0; j < columnaTemporal.length; j++) {
				// Comprobar si el array original y el temporal han cambiado
				if (columnaTemporal[j] != board[j][i]) hasMoved = true;

				board[j][i] = columnaTemporal[j];
			}
		}

		// Si coinciden 2 valores seguidos, multiplicamos el de abajo por 2 y
		// movemos el resto un hueco hacia abajo sobreescribiendo el 2º valor
		for (int i = 0; i < horizontalLength; i++) {
			for (int j = verticalLength - 1; j > 0; j--) {
				if (board[j][i] == board[j - 1][i]) {
					board[j][i] *= 2;

					// Al combinarse dos baldosas hay movimiento
					if (board[j][i] != 0) hasMoved = true;
					
					// Sumamos la puntuacion al jugador
					score += board[j][i];

					// Mueve el resto hacia la abajo, sobreescribiendo el valor que ya se ha añadido
					// al primero
					for (int k = j - 1; k > 0; k--) {
						board[k][i] = board[k - 1][i];
					}

					// Eliminamos el valor del ultimo tile
					board[0][i] = 0;
				}
			}
		}
		
		return hasMoved;
	}

	/**
	 * Genera un nuevo tile en una posicion aleatoria del tablero que este vacia, el
	 * nuevo numero sera una potencia de 2
	 * 
	 * @param maxInitialValue Indica la potencia maxima a la que se elevara la base
	 *                        2 para generar el numero
	 */
	private void generateNewNumber(int maxInitialValue) {
		int fila, columna;

		// Comprueba filas y columnas aleatorias hasta que encuentra un hueco vacio
		do {
			fila = (int) (Math.random() * board.length);
			columna = (int) (Math.random() * board[0].length);
		} while (board[fila][columna] != 0);

		// Genera un numero aleatorio de base 2, con el exponente indicado
		board[fila][columna] = (int) (Math.pow(2, (int) (1 + Math.random() * maxInitialValue)));
	}

	/**
	 * Comprueba el estado del tablero
	 * 
	 * @return Devuelve el estado del tablero
	 */
	private BoardState checkBoardState() {
		// Comprueba si alguna casilla ha llegado al 2048 (ha ganado el juegador) o si
		// quedan huecos vacios (continua el juego)
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 2048)
					return BoardState.Completed;
				else if (board[i][j] == 0)
					return BoardState.InProgress;
			}
		}

		// Comprueba si los tiles adyacentes por los lados son iguales, si quedan
		// movimientos continua el juego
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length - 1; j++) {
				if (board[i][j] == board[i][j + 1])
					return BoardState.InProgress;
			}
		}

		// Comprueba si los tiles adyacentes por arriba y abajo son iguales, si quedan
		// movimientos continua el juego
		for (int i = 0; i < board.length - 1; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == board[i + 1][j])
					return BoardState.InProgress;
			}
		}

		// Si no se cumple ninguna condicion anterior el tablero ha quedado bloqueado
		return BoardState.Blocked;
	}
}