import java.util.Stack;
import java.util.Collections;

public class Mazo{
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
                cartas.push(new Carta(palo, valor)); //Agregamos cada carta al mazo utilizando el metodo push de la pila
                }}
        }

    public void barajar() {
            Collections.shuffle(cartas); //Utilizamos el metodo shuffle de la clase Collections para mezclar las cartas del mazo
        }

    public Carta repartirCarta() {
        if (!cartas.isEmpty()) { //Esto sirve para comprobar que el mazo no esté vacío antes de repartir una carta
            return cartas.pop(); //Devuelve la carta que esta primera en el mazo y la elimina del mazo
        }
        return null; //Si el mazo esta vacio, devuelve null
        }

    public boolean esVacio() {
    return cartas.isEmpty(); //Devuelve true si el mazo esta vacio, de lo contrario devuelve false
        }
    }
