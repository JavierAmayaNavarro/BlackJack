package BlackJack;

import java.util.Stack;
import java.util.Collections;

public class Mazo{

    /* Atributos de la clase Mazo */
    private Stack<Carta> cartas; //Creamos una pila de cartas gracias a la clase Stack

        //Constructor de la clase Mazo
    public Mazo() {
            cartas = new Stack<>(); //Inicializamos la pila de carta
            iniciarMazo(); //Llamamos al metodo iniciarMazo para llenar el mazo con las cartas
            barajar(); //Llamamos al metodo barajar para mezclar las cartas del mazo
        }

    private void iniciarMazo() {
            String[] palos = {"♠", "♥", "♦", "♣"}; //Creamos un array con los palos de las cartas
            String[] valores = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"}; //Creamos un array con los valores de las cartas

            for (String palo : palos) { //Recorremos el array de palos
                for (String valor : valores) { //Se recorre el array de valores
                cartas.add(new Carta(palo, valor)); //Agregamos cada carta al mazo utilizando el metodo add de la lista
                }}
        }

    /* Metodo para Barajar las cartas */

    public void barajar() {
            Collections.shuffle(cartas); //Utilizamos el metodo shuffle de la clase Collections para mezclar las cartas del mazo
        }

    /* Metodo para repartir la carta del tope del mazo
    Si el mazo se agota, se reinicializa y vuelve a barajar automaticamente */
    public Carta repartirCarta() {
    // Si el mazo está vacío, reiniciamos y barajamos
    if (cartas.isEmpty()) {
        System.out.println("  ⚠ El mazo se ha agotado. Reiniciando y barajando...");
        iniciarMazo(); // Rellenamos el mazo con cartas
        barajar(); // Barajamos las cartas
    }

    // Ahora que el mazo está lleno nuevamente, repartimos la carta
        return cartas.pop(); // Usamos pop() para sacar la carta del tope de la pila
    }   

    /* Devuelve el número de cartas restantes en el mazo. */

    public int cartasRestantes() {
        return cartas.size(); // Muestra el tamaño de la lista de cartas
    }

    /* Getters */

    public Stack<Carta> getCartas() {
        return cartas; }
}