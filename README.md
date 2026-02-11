enunciado 

Realiza un programa en Java para jugar al Blackjack entre varios jugadores y el
crupier, utilizando Programación Orientada a Objetos.
El programa deberá implementar, como mínimo, las siguientes clases:
- Carta
- Mazo
- Mano
- Jugador
- Crupier
- JuegoBlackjack
Cada clase debe tener sus atributos privados, constructores, getters/setters y
los métodos necesarios para su funcionamiento conforme a los principios de
POO.
Además, el programa deberá contar, como mínimo, con los siguientes métodos
clave dentro del flujo del juego:
- ganador(); // Determina quién gana comparando las puntuaciones del
jugador y del crupier (21 exacto, blackjack, pasarse, o valor más alto
válido).
- esValido(); // Comprueba si el movimiento del jugador es válido (por
ejemplo, pedir carta solo si no se ha pasado y no tiene blackjack).
- movimiento(); // Ejecuta la acción del jugador: pedir carta (hit) o
plantarse (stand). El crupier también actuará según las reglas básicas
(pedir hasta 17).
- mostrar_tablero(); Muestra el estado de la partida: cartas visibles de los
jugadores y del crupier, y la puntuación conocida.
- inicio_juego(); Controla todo el flujo del juego: creación del mazo, reparto
inicial, turnos, decisiones de los jugadores, turno del crupier y resultado
final.
El ganador o los ganadores de cada ronda sumarán un punto. El primero en
conseguir 5 gana el juego. Si el crupier llega antes a los 5 puntos, gana la
banca. Los puntos de los jugadores son independientes entre sí.
El jugador debe poder decidir cada turno si quiere:
- Pedir carta (Hit)
-Plantarse (Stand)
El crupier debe jugar siguiendo la norma oficial:
- Pedir cartas hasta llegar al menos a 17.
Deben contemplarse las siguientes situaciones:
- Blackjack natural (21 con dos cartas)
- Jugador o crupier se pasan (más de 21), si el crupier se pasa, ganan los
jugadores automáticamente la ronda.
- Empate (push)
- Ganador por puntuación

