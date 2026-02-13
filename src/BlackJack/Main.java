package BlackJack;

import javax.swing.SwingUtilities;

public class Main {

    public static void main(String[] args) {
        // Si se ejecuta con argumento "-console", usar modo consola
        if (args.length > 0 && args[0].equals("-console")) {
            ejecutarConsola();
        } else {
            // Por defecto, ejecutar interfaz gráfica
            ejecutarGUI();
        }
    }
    //MODO INTERFAZ GRÁFICA:
    private static void ejecutarGUI() {
        System.out.println("Iniciando BlackJack con Interfaz Gráfica...");
        SwingUtilities.invokeLater(() -> new InterfazJuegoBlackjack());
    }

    // MODO CONSOLA:
    private static void ejecutarConsola() {
        System.out.println("Iniciando BlackJack en Modo Consola...\n");
    
        JuegoBlackjack juego = new JuegoBlackjack();
        juego.iniciarJuego();
    }
}