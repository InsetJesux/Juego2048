/**
 * Juego del 2048
 * 
 * Consiste en mover las celdas en una direccion arriba, abajo, izquierda o
 * derecha, los numeros se moveran agrupados a la direccion deseada y si son
 * iguales se juntaran en una misma celda, el objetivo del juego es llegar a
 * 2048.
 * 
 * @author InsetJesux
 * @version 1.0
 * 
 */

import java.util.Scanner;

public class Main {
	private static Scanner input = new Scanner(System.in);
	private static Board board = null;
	private static int bestScore = 0;
	
	/**
	 * Menu principal del programa
	 * 
	 * @return La opcion elegida por el jugador del 0 al 1
	 */
	private static int menu() {
		int option = -1;

		do {
			System.out.println("Mejor Puntuación: " + bestScore + "\n");
			System.out.print("[1] Nueva partida\n" + "[0] Salir del programa\n" + "Elige una opción: ");

			option = input.nextInt();
			System.out.println();

			if (option < 0 || option > 1)
				System.out.println("Opcion incorrecta! Intentalo de nuevo\n");
		} while (option < 0 || option > 1);

		return option;
	}

	/**
	 * Submenu para elegir hacia donde mover los tiles del tablero
	 * 
	 * @return La opcion elegida por el jugador del 0 al 4
	 */
	private static int playerMove() {
		int opcion = -1;

		do {
			System.out.print("\n[1] Mover izquierda\n" + "[2] Mover derecha\n" + "[3] Mover arriba\n"
					+ "[4] Mover abajo\n" + "[0] Salir de la partida\n" + "Elige una opción: ");

			opcion = input.nextInt();
			System.out.println();

			if (opcion < 0 || opcion > 4)
				System.out.println("Opcion incorrecta! Intentalo de nuevo");
		} while (opcion < 0 || opcion > 4);

		return opcion;
	}
	
	/***
	 * Inicia una nueva partida
	 */
	private static void newGame() {
		int playerMove = -1;
		
		board = new Board(4, 4, 2);

		// Pide movimientos al jugador mientras la partida continue y el jugador no haya
		// salido del programa
		do {
			System.out.println("Puntuación actual: " + board.getScore() + "\tMejor puntuación: " + bestScore + "\n");
			board.showBoard();
			playerMove = playerMove();

			switch (playerMove) {
			case 0:
				System.out.println("Has finalizado la partida.\n");
				break;
			case 1:
				board.moveTiles(Direction.Left);
				break;
			case 2:
				board.moveTiles(Direction.Right);
				break;
			case 3:
				board.moveTiles(Direction.Up);
				break;
			case 4:
				board.moveTiles(Direction.Down);
				break;
			}
			
			// Actualizar la mejor puntuacion
			if (board.getScore() > bestScore) bestScore = board.getScore();

		} while (playerMove != 0 && board.getBoardState() == BoardState.InProgress);

		// Muestra al jugador el resultado de la partida
		if (board.getBoardState() != BoardState.InProgress) {
			System.out.println((board.getBoardState() == BoardState.Blocked
					? "Te has quedado sin huecos ni movimientos, has perdido!"
					: "Has llegado a 2048, has ganado!") + "\n");
		}

	}

	public static void main(String[] args) {
		int option = -1;

		do {
			option = menu();

			switch (option) {
			case 0:
				System.out.println("Programa finalizado!");
				break;
			case 1:
				newGame();
				break;
			}
		} while (option != 0);
	}
}
