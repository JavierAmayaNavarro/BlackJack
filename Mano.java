import java.util.ArrayList;

public class Mano {
    private ArrayList<Carta> cartas; //Creamos un ArrayList de cartas para representar la mano del jugador

    //Constructor de la clase Mano
    public Mano() {
        cartas = new ArrayList<>(); //Inicializamos el ArrayList de cartas
    }

    public void agregarCarta(Carta carta) {
        cartas.add(carta); //Agregamos una carta a la mano utilizando el metodo add del ArrayList
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
            return valortotal;

        }
        public void mostrarMano() {
           System.out.println("Cartas en la mano:");
           for (Carta carta : cartas) {
            System.out.println(carta.toString()); //Imprimimos cada carta en la mano utilizando el metodo toString de la clase Carta
             }
           }
         }
    }

