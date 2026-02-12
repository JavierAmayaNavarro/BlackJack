import java.util.ArrayList;
import java.util.Scanner;

public class JuegoBlackjack {

    /* Constantes */

    // ─── Constantes ────────────────────────────────────────────────────────────
    private static final int PUNTOS_VICTORIA = 5;
    private static final String LINEA = "═══════════════════════════════════════════════════";
    private static final String LINEA_FINA = "───────────────────────────────────────────────────";

    /* Atributos del juego */

    private ArrayList<Jugador> jugadores; //Creamos un ArrayList de jugadores para representar a los jugadores en el juego
    private Crupier crupier; //Creamos una variable de tipo Crupier para representar al crupier en el juego
    private Mazo mazo;
    private Scanner scanner; //Creamos un objeto Scanner para leer la entrada del usuario
    private int numero_de_Ronda; //Variable para llevar el conteo de las rondas del juego

    /* Constructores del juego */
    public JuegoBlackjack() {
        jugadores = new ArrayList<>(); //Inicializamos el ArrayList de jugadores
        crupier = new Crupier(); //Creamos un nuevo crupier
        mazo = new Mazo(); //Creamos un nuevo mazo de cartas
        scanner = new Scanner(System.in); //Inicializamos el Scanner para leer la entrada del usuario
        numero_de_Ronda = 0; //Inicializamos el conteo de rondas a 0 }
    }

    /* Metodos del juego */

    public void iniciarJuego() {

        System.out.println("Bienvenido al juego de Blackjack!"); //Mensaje de bienvenida al juego
        System.out.print("Ingrese el numero de jugadores (4-7): "); //Pedimos al usuario que ingrese el numero de jugadores
        int numJugadores = scanner.nextInt(); //Leemos el numero de jugadores ingresado por el usuario
        while (numJugadores < 4 || numJugadores > 7) { //Validamos que el numero de jugadores este entre 4 y 7
            System.out.print("Numero de jugadores invalido. Ingrese un numero entre 4 y 7: "); //Si el numero de jugadores es invalido, pedimos al usuario que ingrese un numero valido
            numJugadores = scanner.nextInt(); //Leemos el nuevo numero de jugadores ingresado por el usuario
        }
        scanner.nextLine(); //Limpiamos el buffer del Scanner para evitar problemas al leer el nombre de los jugadores
        for (int i = 0; i<numJugadores; i++) { 
            System.out.print("Ingrese el nombre del jugador " + (i+1)+ ": "); //Pedimos al usuario que ingrese el nombre de cada jugador
            String nombreJugador = scanner.nextLine(); //Leemos el nombre del jugador ingresado por el usuario
            jugadores.add(new Jugador(nombreJugador)); //Agregamos el jugador al ArrayList de jugadores
        }

        // Bucle hasta que alguien llegue a 5 puntos 
        boolean finjuego = false; //Variable para controlar el fin del juego

        while(!finjuego){// Mientras el juego no haya llegado a 5 puntos, se sigue jugando
            mazo = new Mazo(); //Creamos un nuevo mazo de cartas al comenzar cada ronda
            mazo.barajar(); //Barajamos el mazo de cartas al comenzar cada ronda

            // Reiniciamos las manos de cada jugador y del crupier cada vez que la ronda comienza de nuevo 
            for (Jugador jugador : jugadores) {
                jugador.reiniciarRonda(); //Reiniciamos la mano y el estado de plantado de cada jugador al comenzar cada ronda
            }
            crupier.reiniciarRonda(); //Reiniciamos la mano del crupier al comenzar cada ronda

            // Repartimos dos cartas a cada jugador y al crupier al comenzar cada ronda , ( El juego se empieza con 2 cartas para cada jugador y el crupier) ##PRUEBA##
            for (Jugador jugador : jugadores) {
                jugador.pedirCarta(mazo); //Repartimos una carta al jugador utilizando el metodo pedirCarta
                jugador.pedirCarta(mazo); //Repartimos una segunda carta al jugador utilizando el metodo pedirCarta
            }
            crupier.pedirCarta(mazo); //Repartimos una carta al crupier utilizando el metodo pedirCarta
            crupier.pedirCarta(mazo); //Repartimos una segunda carta al crupier utilizando el metodo pedirCarta

            crupier.jugarTurno(mazo); //El crupier juega su turno utilizando el metodo jugarTurno, que hace que el crupier siga pidiendo cartas hasta que su mano tenga un valor de al menos 17

            // Determinamos el jugador

            ganador();

            //Mostramos las puntuaciones de cada jugador al finalizar la ronda

            for (Jugador jugador : jugadores) {
                if (jugador.obtenerPuntos() == 5) {
                    System.out.println("El ganador es: " + jugador.getNombre() + " con " + jugador.obtenerPuntos() + " puntos!"); //Si un jugador llega a 5 puntos, se declara como ganador y se muestra su nombre y puntos
                    finjuego = true; //Se establece finjuego como true para terminar el bucle del juego
                }
            }

            if (crupier.obtenerPuntos() == 5) {
                System.out.println("El crupier ha ganado con " + crupier.obtenerPuntos() + " puntos!"); //Si el crupier llega a 5 puntos, se declara como ganador y se muestra su puntuacion
                finjuego = true; //Se establece finjuego como true para terminar el bucle del juego
            }
        }
    }

    private void ganador() {
    int valorCrupier = crupier.getMano().obtenerValorMano();

    // Recorremos solo los jugadores, no al crupier
    for (Jugador j : jugadores) {
        int valorJugador = j.getMano().obtenerValorMano();

        // Si el jugador se pasa, lo saltamos
        if (j.getMano().estaPasado()) {
            continue;
        }

        // Comparamos las puntuaciones del jugador y el crupier
        if (crupier.getMano().estaPasado() || valorJugador > valorCrupier) {
            j.sumarPunto(); // El jugador gana
            System.out.println(j.getNombre() + " gana la ronda!");
        } else if (valorJugador == valorCrupier) {
            System.out.println("Empate entre " + j.getNombre() + " y el crupier.");
        } else {
            crupier.sumarPunto(); // El crupier gana
            System.out.println("El crupier gana contra " + j.getNombre());
        }
    }
    }
}
