import java.util.Scanner;

public class Main {
	private static Scanner input = new Scanner(System.in);

	/**
	 * Menu principal del programa
	 * 
	 * @return La opcion elegida por el jugador del 0 al 1
	 */
	private static int menu() {
		int opcion = -1;

		do {
			System.out.print("[1] Nueva partida\n" + "[0] Salir\n" + "Elige una opción: ");

			opcion = input.nextInt();

			if (opcion < 0 || opcion > 1)
				System.out.println("Opcion incorrecta! Intentalo de nuevo");
		} while (opcion < 0 || opcion > 1);

		return opcion;
	}

	/**
	 * Submenu para elegir hacia donde mover los tiles del tablero
	 * 
	 * @return La opcion elegida por el jugador del 0 al 4
	 */
	private static int turno() {
		int opcion = -1;

		do {
			System.out.print("[1] Mover izquierda\n" + "[2] Mover derecha\n" + "[3] Mover arriba\n"
					+ "[4] Mover abajo\n" + "[0] Salir\n" + "Elige una opción: ");

			opcion = input.nextInt();
			System.out.println();

			if (opcion < 0 || opcion > 4)
				System.out.println("Opcion incorrecta! Intentalo de nuevo");
		} while (opcion < 0 || opcion > 4);

		return opcion;
	}

	public static void main(String[] args) {
		int opcion = -1;
		Board board = null;

		do {
			opcion = menu();

			switch (opcion) {
			case 0:
				System.out.println("Programa finalizado!");
				break;
			case 1:
				board = new Board(4, 4, 2);

				// Pide movimientos al jugador mientras la partida continue y el jugador no haya
				// salido del programa
				do {
					board.showBoard();
					opcion = turno();

					switch (opcion) {
					case 0:
						System.out.println("Programa finalizado!");
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

				} while (opcion != 0 && board.getBoardState() == BoardState.InProgress);

				// Muestra al jugador el resultado de la partida
				if (board.getBoardState() != BoardState.InProgress) {
					System.out.println((board.getBoardState() == BoardState.Blocked
							? "Te has quedado sin huecos ni movimientos, has perdido!"
							: "Has llegado a 2048, has ganado!") + "\n");
				}

				break;
			}
		} while (opcion != 0);
	}
}
