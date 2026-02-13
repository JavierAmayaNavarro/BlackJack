package BlackJack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class JuegoBlackjack {

    /* Constantes */
    private static final int PUNTOS_VICTORIA = 5;
    private static final String LINEA = "═══════════════════════════════════════════════════";
    private static final String LINEA_FINA = "───────────────────────────────────────────────────";

    /* Atributos del juego */

    private List<Jugador> jugadores; // Creamos un ArrayList de jugadores para representar a los jugadores en el juego
    private Crupier crupier; // Creamos una variable de tipo Crupier para representar al crupier en el juego
    private Mazo mazo;
    private Scanner scanner; // Creamos un objeto Scanner para leer la entrada del usuario
    private int numero_de_Ronda; // Variable para llevar el conteo de las rondas del juego
    
    /** NUEVO: Referencia a la interfaz gráfica (null si se ejecuta en consola) */
    private InterfazJuegoBlackjack interfazGrafica;

    /* Constructores del juego */
    /**
     * Constructor de JuegoBlackjack
     * @param interfaz - Referencia a la interfaz gráfica (puede ser null para consola)
     */
    public JuegoBlackjack(InterfazJuegoBlackjack interfaz) {
        this.interfazGrafica = interfaz;  // Guardar referencia a la interfaz
        jugadores = new ArrayList<>(); // Inicializamos el ArrayList de jugadores
        crupier = new Crupier("Crupier"); // Creamos un nuevo crupier
        mazo = new Mazo(); // Creamos un nuevo mazo de cartas
        scanner = new Scanner(System.in); // Inicializamos el Scanner para leer la entrada del usuario
        numero_de_Ronda = 0; // Inicializamos el conteo de rondas a 0
    }
    
    /** Constructor sin interfaz gráfica (para modo consola) */
    public JuegoBlackjack() {
        this(null);  // Llamar al constructor principal con null
    }

    /* Métodos del juego */

    // Método para mostrar la bienvenida al juego y la explicación de las reglas
    public void bienvenida_juego() {
        System.out.println("\n" + LINEA);
        System.out.println("  ♠ ♥  BIENVENIDO AL BLACKJACK  ♦ ♣");
        System.out.println("  Gana el primero en conseguir " + PUNTOS_VICTORIA + " puntos.");
        System.out.println(LINEA);
    }

    // Método para configurar los jugadores al inicio del juego
    public void configurar_jugadores() {
        System.out.println("Inserte el número de jugadores (entre 4 y 7 jugadores): ");
        int numJugadores = scanner.nextInt(); // Leemos el número de jugadores ingresado
        scanner.nextLine(); // Limpiamos el buffer del Scanner
        while (numJugadores < 4 || numJugadores > 7) { // Validamos que el número de jugadores sea entre 4 y 7
            System.out.println("Número de jugadores inválido. Ingrese un número entre 4 y 7: ");
            numJugadores = scanner.nextInt(); // Leemos el nuevo número de jugadores ingresado
            scanner.nextLine(); // Limpiamos el buffer del Scanner
        }
        for (int i = 0; i < numJugadores; i++) { // Bucle para agregar cada jugador al ArrayList de jugadores 
            System.out.print("Ingrese el nombre del jugador " + (i + 1) + ": ");
            String nombreJugador = scanner.nextLine(); // Leemos el nombre del jugador
            jugadores.add(new Jugador(nombreJugador)); // Agregamos el jugador al ArrayList de jugadores
        }
        System.out.println("En total hay " + jugadores.size() + " jugadores en la partida.");
    }

    public void iniciarJuego() {
        bienvenida_juego();
        configurar_jugadores();

        mazo = new Mazo(); // Creamos un nuevo mazo de cartas al iniciar el juego
        mazo.barajar(); // Barajamos el mazo de cartas al iniciar el juego

        while (!hayGanadorFinal()) { // Mientras el juego no haya llegado a 5 puntos, se sigue jugando
            numero_de_Ronda++; // Incrementamos el número de rondas al comenzar una nueva ronda
            System.out.println("\n" + LINEA_FINA);
            System.out.println(" P C RONDA " + numero_de_Ronda + " D T");
            System.out.println(LINEA_FINA);

            jugarRonda(); // Llamamos al método de jugarRonda para ejecutar una nueva ronda
            mostrar_marcador(); // Mostramos el marcador al finalizar cada ronda

            if (!hayGanadorFinal()) { // Si el juego no ha terminado, se le pide al usuario que pulse ENTER para continuar a la siguiente ronda
                System.out.print("\n  Pulsa ENTER para la siguiente ronda...");
                scanner.nextLine();
            }
        }

        define_ganador_final();
    }

    public void jugarRonda() {
        // 1. Preparar nueva ronda
        for (Jugador j : jugadores) j.reiniciarRonda();
        crupier.nuevaRonda();

        // 2. Reparto inicial (2 cartas cada uno; 2ª carta del crupier boca abajo)
        repartirInicial();
        mostrar_tablero(false);
        
        // ACTUALIZAR GUI INMEDIATAMENTE DESPUÉS DEL REPARTO
        if (interfazGrafica != null) {
            interfazGrafica.actualizarTablero();
            try {
                Thread.sleep(800);  // Pausa para que vea las cartas
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 3. Verificar Blackjack natural del crupier inmediatamente
        if (crupier.tieneBlackjack()) {
            System.out.println("\n  ⭐ ¡El CRUPIER tiene BLACKJACK NATURAL!");
            crupier.revelarCartaOculta();
            mostrar_tablero(true);
            if (interfazGrafica != null) {
                interfazGrafica.actualizarTablero();
            }
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
        
        // ACTUALIZAR GUI DESPUÉS DEL TURNO DEL CRUPIER
        if (interfazGrafica != null) {
            interfazGrafica.actualizarTablero();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // 7. Resolver ronda y asignar puntos
        resolverRonda();
        
        // ACTUALIZAR GUI AL FINAL DE LA RONDA
        if (interfazGrafica != null) {
            interfazGrafica.actualizarTablero();
        }
    }

    // ─── Reparto inicial ────────────────────────────────────────────────────────
    private void repartirInicial() {
        // Primera carta a todos (boca arriba)
        for (Jugador j : jugadores) j.pedirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta(), false);

        // Segunda carta a todos (crupier boca abajo)
        for (Jugador j : jugadores) j.pedirCarta(mazo.repartirCarta());
        crupier.recibirCarta(mazo.repartirCarta(), true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TURNOS DE JUGADORES
    // ══════════════════════════════════════════════════════════════════════════

    private void jugarTurnoJugador(Jugador jugador) {
        System.out.println("\n" + LINEA_FINA);
        System.out.println("  TURNO DE: " + jugador.getNombre().toUpperCase());
        System.out.println(LINEA_FINA);

        // Si hay interfaz gráfica, mostrar nombre del jugador
        if (interfazGrafica != null) {
            interfazGrafica.mostrarTurnoJugador(jugador.getNombre());
            try {
                Thread.sleep(500);  // Pequeña pausa para que vea el nombre
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Blackjack natural automático
        if (jugador.getMano().tieneBlackjack()) {
            System.out.println("  ⭐ ¡BLACKJACK NATURAL! El turno pasa automáticamente.");
            return;
        }

        // ════════════════════════════════════════════════════════════
        // TURNO MANUAL DEL JUGADOR (NO AUTOMÁTICO)
        // ════════════════════════════════════════════════════════════
        
        while (!jugador.turnoTerminado()) {
            System.out.println("  Mano: " + formatMano(jugador.getMano()) +
                               "  →  Puntuación: " + jugador.getMano().obtenerValorMano());
            System.out.println("  [1] Pedir carta (Hit)   [2] Plantarse (Stand)");
            
            String entrada;
            
            // Si hay interfaz gráfica, pedir acción desde ahí
            // Si no, usar Scanner (modo consola)
            if (interfazGrafica != null) {
                // MODO GRÁFICO: Esperar a que el usuario haga clic en un botón
                entrada = interfazGrafica.obtenerAccion();
                System.out.print("  Tu elección: " + entrada + "\n");
            } else {
                // MODO CONSOLA: Leer del teclado como antes
                System.out.print("  Tu elección: ");
                entrada = scanner.nextLine().trim();
            }

            movimiento(jugador, entrada);
            
            // Actualizar tablero en GUI si existe
            if (interfazGrafica != null) {
                interfazGrafica.actualizarTableroDesdeJuego();
                try {
                    Thread.sleep(600);  // Pausa para ver los cambios
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
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

    // Resolución cuando el crupier tiene Blackjack natural
    private void resolverRonda_crupierBlackjack() {
        System.out.println("\n  ─── RESULTADOS DE LA RONDA ───");
        StringBuilder mensajeGanadores = new StringBuilder("⭐ ¡CRUPIER TIENE BLACKJACK!\n\n");
        boolean crupierGana = false;
        
        for (Jugador j : jugadores) {
            if (j.getMano().tieneBlackjack()) {
                System.out.println("  " + j.getNombre() + ": EMPATE (ambos con Blackjack)");
                mensajeGanadores.append("= ").append(j.getNombre()).append(" - EMPATE\n");
            } else {
                System.out.println("  " + j.getNombre() + ": PIERDE (crupier tiene Blackjack)");
                mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE\n");
                crupierGana = true;
            }
        }
        
        if (crupierGana) {
            crupier.sumarPunto();
            mensajeGanadores.append("\n✔ CRUPIER GANA LA RONDA");
        }
        
        // Mostrar en GUI
        if (interfazGrafica != null) {
            interfazGrafica.mostrarResultadoRonda(mensajeGanadores.toString());
        }
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
        StringBuilder mensajeGanadores = new StringBuilder("📊 RESULTADOS DE LA RONDA\n\n");

        if (crupier.estaPasado()) {
            System.out.println("  ⭐ ¡El CRUPIER se ha PASADO! Ganan todos los jugadores activos.");
            mensajeGanadores.append("⭐ ¡CRUPIER SE PASÓ!\n\n");
            
            for (Jugador j : jugadores) {
                if (!j.isEliminado()) {
                    j.sumarPunto();
                    System.out.println("  ✔ " + j.getNombre() + " GANA (+1 punto → " + j.getPuntos() + ")");
                    mensajeGanadores.append("✔ ").append(j.getNombre()).append(" - GANA\n");
                } else {
                    System.out.println("  ✗ " + j.getNombre() + " PIERDE (se había pasado)");
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE (pasado)\n");
                }
            }
            
            // Mostrar en GUI
            if (interfazGrafica != null) {
                interfazGrafica.mostrarResultadoRonda(mensajeGanadores.toString());
            }
            return;
        }
        // Determinar el máximo entre jugadores activos
        int maxJugador = -1;
        for (Jugador j : jugadores) {
            if (!j.isEliminado()) {
                int val = j.getMano().obtenerValorMano();
                if (val > maxJugador) maxJugador = val;
            }
        }

        int puntosCrupier = crupier.getPuntuacionTotal();

        // Si el máximo jugador está por encima del crupier, solo los jugadores con ese máximo ganan
        if (maxJugador > puntosCrupier) {
            mensajeGanadores.append("★ Jugador(es) con la mejor mano ganan la ronda\n\n");
            for (Jugador j : jugadores) {
                if (!j.isEliminado() && j.getMano().obtenerValorMano() == maxJugador) {
                    j.sumarPunto();
                    System.out.println("  ✔ " + j.getNombre() + " GANA  |  "
                            + j.getMano().obtenerValorMano() + " vs " + puntosCrupier
                            + "  → Puntos: " + j.getPuntos());
                    mensajeGanadores.append("✔ ").append(j.getNombre()).append(" - GANA (")
                            .append(j.getMano().obtenerValorMano()).append(" vs ")
                            .append(puntosCrupier).append(")\n");
                } else if (!j.isEliminado()) {
                    System.out.println("  ✗ " + j.getNombre() + " PIERDE  |  "
                            + j.getMano().obtenerValorMano() + " vs " + puntosCrupier);
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE\n");
                } else {
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE (pasado)\n");
                }
            }
        } else if (maxJugador == puntosCrupier) {
            // Empate global: no puntos para jugadores ni crupier
            mensajeGanadores.append("= Empate general con el crupier. Ningún punto asignado.\n");
            for (Jugador j : jugadores) {
                if (!j.isEliminado()) {
                    mensajeGanadores.append("= ").append(j.getNombre()).append(" - EMPATE\n");
                } else {
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE (pasado)\n");
                }
            }
        } else {
            // Crupier gana la ronda
            mensajeGanadores.append("✔ CRUPIER GANA LA RONDA\n");
            crupier.sumarPunto();
            System.out.println("  ✔ CRUPIER gana la ronda → Puntos: " + crupier.getPuntos());
            for (Jugador j : jugadores) {
                if (!j.isEliminado()) {
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE\n");
                } else {
                    mensajeGanadores.append("✗ ").append(j.getNombre()).append(" - PIERDE (pasado)\n");
                }
            }
        }

        // Mostrar en GUI
        if (interfazGrafica != null) {
            interfazGrafica.mostrarResultadoRonda(mensajeGanadores.toString());
        }
    }

    // MOSTRAR TABLERO

    public void mostrar_tablero(boolean mostrarTodo) {
        System.out.println("\n" + LINEA_FINA);
        System.out.println("  TABLERO");
        System.out.println(LINEA_FINA);

        // Crupier
        if (mostrarTodo) {
            System.out.println("  Crupier  → " + formatMano(crupier.getMano())
                    + "  [" + crupier.getPuntuacionTotal() + " pts]");
        } else {
            System.out.println("  Crupier  → " + formatMano(crupier.getMano())
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
                    + "  → " + formatMano(j.getMano())
                    + "  [" + j.getMano().obtenerValorMano() + " pts]" + estado);
        }
        System.out.println(LINEA_FINA);
    }

    /* ---- Helpers para mostrar símbolos de palo ---- */
    private String formatCarta(Carta c) {
        if (c == null) return "";
        String palo = c.getPalo();
        String simbolo = (palo != null && palo.length() > 0) ? palo.substring(0, 1) : "";
        return c.getValor() + simbolo;
    }

    private String formatMano(Mano mano) {
        StringBuilder sb = new StringBuilder();
        for (Carta c : mano.getCartas()) {
            sb.append(formatCarta(c)).append(" ");
        }
        return sb.toString().trim();
    }

    // Marcador y ganador final

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
        sb.append(" (" + pts + "/" + PUNTOS_VICTORIA + ")");
        return sb.toString();
    }

    public boolean hayGanadorFinal() {
        if (crupier.getPuntos() >= PUNTOS_VICTORIA) return true;
        return jugadores.stream().anyMatch(j -> j.getPuntos() >= PUNTOS_VICTORIA);
    }

    public void define_ganador_final() {
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

    // ════════════════════════════════════════════════════════════════════════════
    // MÉTODOS NUEVOS AGREGADOS PARA LA INTERFAZ GRÁFICA
    // ════════════════════════════════════════════════════════════════════════════

    /**
     * MÉTODO NUEVO: agregarJugador()
     * Permite agregar jugadores dinámicamente desde la interfaz gráfica
     * (En la versión original, los jugadores se pedían por consola)
     * 
     * @param nombre - Nombre del jugador a agregar
     */
    public void agregarJugador(String nombre) {
        // Validar que el nombre no sea nulo ni esté vacío
        if (nombre != null && !nombre.trim().isEmpty()) {
            // Crear un nuevo jugador y agregarlo a la lista
            jugadores.add(new Jugador(nombre.trim()));
        }
    }

    /**
     * MÉTODO NUEVO: inicializarMazo()
     * Crea un nuevo mazo de cartas y lo baraja
     * 
     * En la versión original, esto se hacía en iniciarJuego().
     * Ahora está separado para que la interfaz gráfica tenga control sobre cuándo
     * inicializar el mazo.
     */
    public void inicializarMazo() {
        mazo = new Mazo();      // Crear un mazo nuevo
        mazo.barajar();         // Barajarlo
    }

    // ════════════════════════════════════════════════════════════════════════════
    // GETTERS: Métodos para acceder a los datos del juego desde otras clases
    // ════════════════════════════════════════════════════════════════════════════

    /** Obtener la lista de jugadores */
    public List<Jugador> getJugadores() { return jugadores; }
    
    /** Obtener una referencia al crupier */
    public Crupier       getCrupier()   { return crupier;   }
    
    /** Obtener una referencia al mazo */
    public Mazo          getMazo()      { return mazo;      }

    /**
     * Devuelve el ganador final de la partida si lo hay.
     * Retorna null si el crupier fue el ganador final.
     */
    public Jugador getGanadorFinal() {
        if (crupier.getPuntos() >= PUNTOS_VICTORIA) return null;
        for (Jugador j : jugadores) {
            if (j.getPuntos() >= PUNTOS_VICTORIA) return j;
        }
        return null;
    }
}