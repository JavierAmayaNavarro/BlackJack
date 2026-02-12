# Juego de Blackjack en Java

Este es un proyecto de consola que implementa un juego de **Blackjack** en Java. El objetivo es crear una experiencia interactiva donde los jugadores pueden competir contra un crupier hasta que un jugador alcance 5 puntos, ganando en base a las reglas tradicionales del Blackjack.

## Características

- **Jugadores**: El juego soporta entre 4 y 7 jugadores.
- **Crupier**: El crupier tiene su propio conjunto de reglas para jugar y también puede obtener un Blackjack natural.
- **Rondas**: El juego se juega en rondas, y el marcador se actualiza al final de cada ronda.
- **Reglas**:
  - Los jugadores pueden **pedir carta** (Hit) o **plantarse** (Stand).
  - Si un jugador obtiene un Blackjack natural (21 puntos con 2 cartas), gana automáticamente la ronda.
  - El crupier juega su turno después de todos los jugadores.
  - El primer jugador (o crupier) en alcanzar 5 puntos gana la partida.

## Requisitos

- **Java 11** o superior.
- Un entorno de desarrollo de Java configurado (por ejemplo, [IntelliJ IDEA](https://www.jetbrains.com/idea/), [Eclipse](https://www.eclipse.org/)).

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/tu-usuario/juego-blackjack-java.git
    ```

2. Navega a la carpeta del proyecto:

    ```bash
    cd juego-blackjack-java
    ```

3. Abre el proyecto con tu IDE preferido o compílalo utilizando el siguiente comando de Java:

    ```bash
    javac JuegoBlackjack.java
    ```

## Uso

Para ejecutar el juego, asegúrate de tener el archivo `.java` compilado y luego ejecuta el siguiente comando en la terminal:

```bash
java JuegoBlackjack