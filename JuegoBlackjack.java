import java.util.ArrayList;
import java.util.Scanner;
import java.util.List;

public class JuegoBlackjack {

    /* Constantes */
    private static final int PUNTOS_VICTORIA = 5;
    private static final String LINEA = "═══════════════════════════════════════════════════";
    private static final String LINEA_FINA = "───────────────────────────────────────────────────";

    /* Atributos del juego */

    private List<Jugador> jugadores; //Creamos un ArrayList de jugadores para representar a los jugadores en el juego
    private Crupier crupier; //Creamos una variable de tipo Crupier para representar al crupier en el juego
    private Mazo mazo;
    private Scanner scanner; //Creamos un objeto Scanner para leer la entrada del usuario
    private int numero_de_Ronda; //Variable para llevar el conteo de las rondas del juego

    /* Constructores del juego */
    public JuegoBlackjack() {
        jugadores = new ArrayList<>(); //Inicializamos el ArrayList de jugadores
        crupier = new Crupier("Crupier"); //Creamos un nuevo crupier
        mazo = new Mazo(); //Creamos un nuevo mazo de cartas
        scanner = new Scanner(System.in); //Inicializamos el Scanner para leer la entrada del usuario
        numero_de_Ronda = 0; //Inicializamos el conteo de rondas a 0 }
    }

    /* Metodos del juego */

    //Metodo para mostrar la bienvenida al juego y la explicacion de las reglas
    public void bienvenida_juego(){
        System.out.println("\n" + LINEA);
        System.out.println("  ♠ ♥  BIENVENIDO AL BLACKJACK  ♦ ♣");
        System.out.println("  Gana el primero en conseguir " + PUNTOS_VICTORIA + " puntos.");
        System.out.println(LINEA);

    }
    //Metodo para configurar los jugadores al inicio del juego, pipdiendo el numero de players, nombres y mostrando el total de players en la partida
    public void configurar_jugadores() {
        System.out.println("Inserte el numero de jugadores (entre 4 y 7 jugadores): "); //Pedimos el numero de jugadores a los usuarios
        int numJugadores = scanner.nextInt(); //Leemos el numer de jugadores ingresado
        scanner.nextLine(); //Limpiamos el buffer del Scanner para evitar problemas al leer el nombre de los jugadores
        while (numJugadores < 4 || numJugadores > 7) { //Validamos que el numero de jugadores sea entre 4 y 7
            System.out.println("Numero de jugadores invalido. Ingrese un numero entre 4 y 7: ");
            numJugadores = scanner.nextInt(); //Leemos el nuevo numero de jugadores ingresado por el usuario
            scanner.nextLine(); //Limpiamos el buffer del Scanner
        }
        for (int i = 0; i < numJugadores; i++) { //Bucle para agregar cada jugador al ArrayList de jugadores 
            System.out.print("Ingrese el nombre del jugador " + (i + 1) + ": "); //Pedimos al usuario que ingrese el nombre de cada jugador 
            String nombreJugador = scanner.nextLine(); //Leemos el nombre del jugador ingresado por el usuario
            jugadores.add(new Jugador(nombreJugador)); //Agregamos el jugador al ArrayList de jugadores
        }
        System.out.println("En total hay "+jugadores.size() + " jugadores en la partida."); //Mostramos el numero total de jugadores en la partida
    }

    public void iniciarJuego() {
        bienvenida_juego();
        configurar_jugadores();

        mazo = new Mazo(); //Creamos un nuevo mazo de cartas al iniciar el juego mazo.barajar(); //Barajamos el mazo de cartas al iniciar el juego

        while(!hayGanadorFinal()){// Mientras el juego no haya llegado a 5 puntos, se sigue jugando
            numero_de_Ronda++; //Incrementamos el numero de rondas al comenzar una nueva ronda
            System.out.println("\n" + LINEA_FINA); 
            System.out.println(" ♠ ♥ RONDA " + numero_de_Ronda + " ♦ ♣"); 
            System.out.println(LINEA_FINA);

            jugarRonda(); //Llamamos al metodo de jugarRonda para ejecutar una nueva ronda
            mostrar_marcador(); // Mostramos el marcador al finaliza cada ronda para q los jugadores vean sus estadisticas

            if (!hayGanadorFinal()) { //Si el juego no ha terminado, se le pide al usuario que pulse ENTER para continuar a la siguiente ronda
                System.out.print("\n  Pulsa ENTER para la siguiente ronda...");
                scanner.nextLine();
            }
        }

            define_ganador_final();
    }

    private void jugarRonda() {
        // 1. Preparar nueva ronda
        for (Jugador j : jugadores) j.reiniciarRonda();
        crupier.nuevaRonda();

        // 2. Reparto inicial (2 cartas cada uno; 2ª carta del crupier boca abajo)
        repartirInicial();
        mostrar_tablero(false);

        // 3. Verificar Blackjack natural del crupier inmediatamente
        if (crupier.tieneBlackjack()) {
            System.out.println("\n  ⭐ ¡El CRUPIER tiene BLACKJACK NATURAL!");
            crupier.revelarCartaOculta();
            mostrar_tablero(true);
            resolverRonda_crupierBlackjack();
            return;
        }

        // 4. Turnos de los jugadores
        for (Jugador jugador : jugadores) {
            jugarTurnoJugador(jugador);
        }

        // 5. Verificar si todos los jugadores se han pasado (crupier no juega)
        boolean todosEliminados = jugadores.stream().allMatch(Jugador::isEliminado);
        if (todosEliminados) {
            System.out.println("\n  Todos los jugadores se han pasado. El crupier gana la ronda.");
            crupier.sumarPunto();
            return;
        }

        // 6. Turno del crupier
        System.out.println("\n" + LINEA_FINA);
        System.out.println("  TURNO DEL CRUPIER");
        System.out.println(LINEA_FINA);
        crupier.jugarTurno(mazo);
        mostrar_tablero(true);

        // 7. Resolver ronda y asignar puntos
        resolverRonda();
    }

    // ─── Reparto inicial ────────────────────────────────────────────────────────
    private void repartirInicial() {
        // Primera carta a todos (boca arriba)
        for (Jugador j : jugadores) j.pedirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta(), false);

        // Segunda carta a todos (crupier boca abajo)
        for (Jugador j : jugadores) j.obtenerCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta(), true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TURNOS DE JUGADORES
    // ══════════════════════════════════════════════════════════════════════════

    private void jugarTurnoJugador(Jugador jugador) {
        System.out.println("\n" + LINEA_FINA);
        System.out.println("  TURNO DE: " + jugador.getNombre().toUpperCase());
        System.out.println(LINEA_FINA);

        // Blackjack natural automático
        if (jugador.getMano().tieneBlackjack()) {
            System.out.println("  ⭐ ¡BLACKJACK NATURAL! El turno pasa automáticamente.");
            return;
        }

        // Bucle de acciones del jugador
        while (!jugador.turnoTerminado()) {
            System.out.println("  Mano: " + jugador.getMano() +
                               "  →  Puntuación: " + jugador.getMano().obtenerValorMano());
            System.out.println("  [1] Pedir carta (Hit)   [2] Plantarse (Stand)");
            System.out.print("  Tu elección: ");

            String entrada = scanner.nextLine().trim();
            movimiento(jugador, entrada);
        }
    }

    public boolean esValido(Jugador jugador, String accion) {
        if (jugador.isEliminado() || jugador.isPlantado() || jugador.getMano().tieneBlackjack()) {
            return false;
        }
        return accion.equals("1") || accion.equals("2");
    }

    public void movimiento(Jugador jugador, String accion) {
        if (!esValido(jugador, accion)) {
            System.out.println("  ✗ Movimiento no válido. Elige 1 (Hit) o 2 (Stand).");
            return;
        }

        if (accion.equals("1")) {
            // Hit: pedir carta
            Carta nuevaCarta = mazo.repartirCarta();
            jugador.pedirCarta(nuevaCarta);
            jugador.actualizarEstado();
            System.out.println("  Carta recibida: " + nuevaCarta);

            if (jugador.getMano().estaPasado()) {
                System.out.println("  ✗ ¡Te has pasado de 21! Puntuación: "
                        + jugador.getMano().obtenerValorMano());
            } else if (jugador.getMano().tieneBlackjack()) {
                System.out.println("  ⭐ ¡BLACKJACK! Puntuación: 21");
            } else {
                System.out.println("  Puntuación actual: " + jugador.getMano().obtenerValorMano());
            }
        } else {
            // Stand: plantarse
            jugador.plantarse();
            System.out.println("  " + jugador.getNombre() + " se planta con "
                    + jugador.getMano().obtenerValorMano() + " puntos.");
        }
    }

    //Resolucion cuando el crupier tiene Blackjack natural
    private void resolverRonda_crupierBlackjack() {
        System.out.println("\n  ─── RESULTADOS DE LA RONDA ───");
        boolean crupierGana = false;
        for (Jugador j : jugadores) {
            if (j.getMano().tieneBlackjack()) {
                System.out.println("  " + j.getNombre() + ": EMPATE (ambos con Blackjack)");
            } else {
                System.out.println("  " + j.getNombre() + ": PIERDE (crupier tiene Blackjack)");
                crupierGana = true;
            }
        }
        if (crupierGana) crupier.sumarPunto();
    }


    public int ganador(Jugador jugador) {
        int puntosJugador = jugador.getMano().obtenerValorMano();
        int puntosCrupier = crupier.getPuntuacionTotal();

        // Jugador eliminado (se pasó)
        if (jugador.isEliminado()) return -1;

        // Crupier se pasó → jugador activo gana
        if (crupier.estaPasado()) return 1;

        // Blackjack natural del jugador vs. no-blackjack del crupier
        if (jugador.getMano().tieneBlackjack() && !crupier.tieneBlackjack()) return 1;

        // Ambos tienen Blackjack → empate
        if (jugador.getMano().tieneBlackjack() && crupier.tieneBlackjack()) return 0;

        // Comparación de puntuación
        if (puntosJugador > puntosCrupier) return  1;
        if (puntosJugador < puntosCrupier) return -1;
        return 0;  // Empate (push)
    }


    private void resolverRonda() {
        System.out.println("\n  ─── RESULTADOS DE LA RONDA ───");

        if (crupier.estaPasado()) {
            System.out.println("  ⭐ ¡El CRUPIER se ha PASADO! Ganan todos los jugadores activos.");
            for (Jugador j : jugadores) {
                if (!j.isEliminado()) {
                    j.sumarPunto();
                    System.out.println("  ✔ " + j.getNombre() + " GANA (+1 punto → " + j.getPuntos() + ")");
                } else {
                    System.out.println("  ✗ " + j.getNombre() + " PIERDE (se había pasado)");
                }
            }
            return;
        }

        // Comparación individual
        boolean crupierGanaRonda = false;
        for (Jugador j : jugadores) {
            int resultado = ganador(j);
            if (resultado == 1) {
                j.sumarPunto();
                System.out.println("  ✔ " + j.getNombre() + " GANA  |  "
                        + j.getMano().obtenerValorMano() + " vs " + crupier.getPuntuacionTotal()
                        + "  → Puntos: " + j.getPuntos());
            } else if (resultado == 0) {
                System.out.println("  = " + j.getNombre() + " EMPATA (push)  |  "
                        + j.getMano().obtenerValorMano() + " vs " + crupier.getPuntuacionTotal());
            } else {
                crupierGanaRonda = true;
                System.out.println("  ✗ " + j.getNombre() + " PIERDE  |  "
                        + (j.isEliminado() ? "SE PASÓ" : j.getMano().obtenerValorMano())
                        + " vs " + crupier.getPuntuacionTotal());
            }
        }

        // El crupier suma punto si ganó a TODOS los jugadores activos
        long jugadoresActivos = jugadores.stream().filter(j -> !j.isEliminado()).count();
        long jugadoresCrupierGana = jugadores.stream()
                .filter(j -> ganador(j) == -1 && !j.isEliminado()).count();

        if (crupierGanaRonda && jugadoresCrupierGana == jugadoresActivos) {
            crupier.sumarPunto();
            System.out.println("  ✔ CRUPIER gana la ronda → Puntos: " + crupier.getPuntos());
        }
    }

    //MOSTRAR TABLERO 

    public void mostrar_tablero(boolean mostrarTodo) {
        System.out.println("\n" + LINEA_FINA);
        System.out.println("  TABLERO");
        System.out.println(LINEA_FINA);

        // Crupier
        if (mostrarTodo) {
            System.out.println("  Crupier  → " + crupier.getMano()
                    + "  [" + crupier.getPuntuacionTotal() + " pts]");
        } else {
            System.out.println("  Crupier  → " + crupier.getMano()
                    + "  [" + crupier.getPuntuacionVisible() + " pts visibles]");
        }

        System.out.println(LINEA_FINA);

        // Jugadores
        for (Jugador j : jugadores) {
            String estado = "";
            if (j.getMano().tieneBlackjack()) estado = "  ⭐ BLACKJACK";
            else if (j.isEliminado())      estado = "  ✗ PASADO";
            else if (j.isPlantado())       estado = "  ✔ PLANTADO";

            System.out.println("  " + j.getNombre()
                    + "  → " + j.getMano()
                    + "  [" + j.getMano().obtenerValorMano() + " pts]" + estado);
        }
        System.out.println(LINEA_FINA);
    }

    //Marcador y ganador final 

    private void mostrar_marcador() {
        System.out.println("\n  ─── MARCADOR ───");
        System.out.printf("  %-20s %s%n", "Crupier", puntuacionBarra(crupier.getPuntos()));
        for (Jugador j : jugadores) {
            System.out.printf("  %-20s %s%n", j.getNombre(), puntuacionBarra(j.getPuntos()));
        }
    }

    private String puntuacionBarra(int pts) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < PUNTOS_VICTORIA; i++) {
            sb.append(i < pts ? "★ " : "☆ ");
        }
        sb.append(" (").append(pts).append("/").append(PUNTOS_VICTORIA).append(")");
        return sb.toString();
    }

    private boolean hayGanadorFinal() {
        if (crupier.getPuntos() >= PUNTOS_VICTORIA) return true;
        return jugadores.stream().anyMatch(j -> j.getPuntos() >= PUNTOS_VICTORIA);
    }

    private void define_ganador_final() {
        System.out.println("\n" + LINEA);
        System.out.println("  ♠ ♥  FIN DE LA PARTIDA  ♦ ♣");
        System.out.println(LINEA);

        if (crupier.getPuntos() >= PUNTOS_VICTORIA) {
            System.out.println("  ¡LA BANCA GANA! El crupier ha conseguido " + PUNTOS_VICTORIA + " puntos.");
        } else {
            for (Jugador j : jugadores) {
                if (j.getPuntos() >= PUNTOS_VICTORIA) {
                    System.out.println("  🏆 ¡¡¡" + j.getNombre().toUpperCase() + " GANA LA PARTIDA!!!");
                    System.out.println("  Ha conseguido " + PUNTOS_VICTORIA + " puntos en " + numero_de_Ronda + " rondas.");
                }
            }
        }
        System.out.println(LINEA);
    }

    //Getters para acceder a los atributos del juego desde otras clases si es necesario

    public List<Jugador> getJugadores() { return jugadores; }
    public Crupier       getCrupier()   { return crupier;   }
    public Mazo          getMazo()      { return mazo;      }
}
