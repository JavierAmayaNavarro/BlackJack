package BlackJack;

import java.util.List;
import java.util.ArrayList;

public class Mano {
    private List<Carta> cartas; //Creamos una lista de cartas para representar la mano del jugador

    //Constructor de la clase Mano
    public Mano() {
        cartas = new ArrayList<>(); //Inicializamos el ArrayList de cartas
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta); //Agregamos una carta a la mano utilizando el metodo add del ArrayList
    }


    /* Te deja la mano vacia, cada vez que cominezas una nueva ronda. */
    public void limpiar() {
        cartas.clear();
    }


    public int obtenerValorMano() {
        int valortotal = 0;
        int numAses = 0;

        //Calculamos el valor total de la mano
        for(Carta carta : cartas) {
            valortotal += carta.getValorNumerico(); //Sumamos el valor de la carta a el valor de nuestra mano
            if(carta.getValor().equals("A")) {
                numAses++; //Si la carta es un As, incrementamos el contador de Ases
            }

        while (valortotal > 21 && numAses > 0) {//Si el valor total de la mano es mayor a 21 y tenemos ases en la mano, podemos utilizar los ases como valor de 1 en lugar de 11
            valortotal -= 10; //Restamos 10 al valor total para tratar el As como 1 en lugar de 11
            numAses--; //Decrementamos el contador de Ases
        }

    }
        return valortotal;
        
    }

    public void mostrarMano() {
        System.out.println("Cartas en la mano:");
        for (Carta carta : cartas) {
           System.out.println(carta.toString()); //Imprimimos cada carta en la mano utilizando el metodo toString de la clase Carta
        }
    
    }

    public boolean estaPasado() {
        if (obtenerValorMano() > 21) {
            return true; //Si el valor total de la mano es mayor a 21, el jugador ha pasado
        }
        return false; //Si el valor total de la mano es 21 o menor, el jugador no ha pasado
    }

    public boolean tieneBlackjack() {
        if (obtenerValorMano() == 21 && cartas.size() == 2) { //Si el valor total de la mano es 21 y el jugador tiene exactamente 2 cartas, el jugador tiene blackjack
            return true;
        }
        return false; //Si el valor total de la mano no es 21 o el jugador tiene mas de 2 cartas, el jugador no tiene blackjack
    }

    /* Getters */ 
    public List<Carta> getCartas() { //Metodo para obtener la lista de cartas en la mano 
        return cartas;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartas en la mano: ");

        for(Carta carta : cartas) {

            sb.append(carta.toString()); // Llamamos al método toString() de la clase Carta para obtener su representación legible
            sb.append(" "); // Añadimos un espacio entre las cartas

        }

        return sb.toString();
    }
}

