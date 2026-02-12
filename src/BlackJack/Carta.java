package BlackJack;

public class Carta {

    /* Constantes */

    public static final String[] PALOS = {"♠ Picas", "♥ Corazones", "♦ Diamantes", "♣ Tréboles"};
    public static final String[] VALORES = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};


    /*ATRIBUTOS DE LA VARIABLE CARTA */

    private String palo; //(♠ ♥ ♦ ♣) //Se pone privado ya que no queremos que nadie de afuera de la clase pueda modificar el palo de la carta, solo queremos que lo obtengan mediante los getters
    private String valor; //(2–10, J, Q, K, A)
    private int puntos; //Valor numérico de la carta, se asigna al crear la carta a partir del valor de la carta, esto se hace para que no tengamos que calcular el valor numerico cada vez que queramos obtener los puntos de la carta, sino que lo hacemos una sola vez al crear la carta y luego simplemente obtenemos el valor de los puntos
    private boolean bocaAbajo; //Indica si la carta está boca abajo o no, se inicializa como true ya que al crear una carta, esta estará boca abajo

    /* Contructores */

    public Carta (String cPalo, String cValor) {

        this.palo = cPalo; //Ponemos el this para referirnos al de arriba
        this.valor = cValor; 
        this.puntos = getValorNumerico(); //Asignamos el valor numerico de la carta a los puntos, esto se hace para que no tengamos que calcular el valor numerico cada vez que queramos obtener los puntos de la carta, sino que lo hacemos una sola vez al crear la carta y luego simplemente obtenemos el valor de los puntos
        this.bocaAbajo = false;// Inicializamos la carta boca abajo 
    }

    /* Getters */

    public String getPalo(){
        return this.palo; //El this se refiere a los atributos de la clase carta, fuera del constructor
    }

    public String getValor(){
        return this.valor; //Hace referencia al valor de la carta fuera del constructor
    }

    public int getPuntos() {
        return this.puntos; //Hace referencia a los puntos de la carta fuera del constructor
    }
    public boolean isBocaAbajo()  { 
        return bocaAbajo;  //Hace referencia a si la carta esta boca abajo o no fuera del constructor 
    }
    
    public void setBocaAbajo(boolean bocaAbajo) {
        this.bocaAbajo = bocaAbajo; 
    }

    public boolean esAs() {
         return valor.equals("A"); 
    }
    
    // Los setters no los vamos a cambiar ya que la carta no va a cambiar su valor ni su palo, solamente la vamos a obtener sin llegar a modificarla 

    /* Metodos */

    //Metodo para ver el valor de la carta

    public int getValorNumerico() {
        if (this.valor.equals("J") || this.valor.equals("Q") || this.valor.equals("K")){ // En caso de la carta sea J,Q o K, el valor nunerico es 10
            return 10;
        }

        if (this.valor.equals("A")){ //En caso de que la carta sea una A, el valor numerico es 11
            return 11;
        }
        
        return Integer.parseInt(this.valor); //Convierte el valor de la carta a un numero entero a partir de 2 - 10 

    }

       
    @Override
    public String toString() {
        return this.valor + " de " + this.palo; //Devuelve el valor de la carta y su palo
    }

}