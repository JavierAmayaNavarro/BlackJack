public class Carta {

    /*ATRIBUTOS DE LA VARIABLE CARTA */

    public String palo; //(♠ ♥ ♦ ♣)
    public String valor; //(2–10, J, Q, K, A)

    /* Contructores */

    public Carta (String cPalo, String cValor) {

        this.palo = cPalo; //Ponemos el this para referirnos al de arriba
        this.valor = cValor; 
    }

    /* Getters */

    public String getPalo(){
        return this.palo; //El this se refiere a los atributos de la clase carta, fuera del constructor
    }

    public String getValor(){
        return this.valor; //Hace referencia al valor de la carta fuera del constructor
    }

    // Los setters no los vamos a cambiar ya que la carta no va a cambiar su valor ni su palo, solamente la vamos a obtener sin llegar a modificarla 
    


    /* Metodos */

    //Metodo para ver el valor de la carta

    public int getValorNumerico() {
        if (this.valor.equals("J")) || this.valor.equals("Q") || this.valor.equals("K")

    }

}