package com.adrian.clases;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tablero {
    /**
     * Método principal que llama a la creacion de las casillas y jugadores
     * y que crea el bucle de los turnos
     */
    public void iniciarPartida() {
        //instancia de la lista de casillas y de jugadores
        ArrayList<Casillas> listaCasillas = new ArrayList<>();
        ArrayList<Jugador> listaJugadores = new ArrayList<>();

        //Menu para empezar a jugar
        UIManager.put("OptionPane.background", new Color(156, 194, 120));
        UIManager.put("Panel.background", new Color(156, 194, 120));
        String[] botones = {"JUGAR"};
        int ventana = JOptionPane.showOptionDialog(null,
                "PULSA JUGAR PARA EMPEZAR",
                "MONOPOLI",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE, null,
                botones, botones[0]);
        if (ventana == 0) {
            crearCasilla(listaCasillas);
            int nJugadores = crearJugadores(listaJugadores);

            boolean partida = true;
            //bucle que dura mientras no se cumpla una condición de victoria
            while (partida) {
                for (int i = 0; i < nJugadores; i++) {
                    //llamada al método turnos
                    turnos(listaCasillas, listaJugadores, listaJugadores.get(i));
                    //falta condición de victoria
                }
            }
        }
    }

    /**
     * Método que pide datos al usuario para crear objetos de la clase Jugador
     * Y meterlos en una lista
     * @param listaJugadores recibe la lista donde guarda los jugadores
     * @return devuelve el número de jugadores para usarlo en los turnos.
     */
    public static int crearJugadores(ArrayList<Jugador> listaJugadores) {
        //variable para guardar el número de jugadores.
        int nJugadores = 0;

        //Menu para elegir el número de jugadores
        UIManager.put("OptionPane.background", new Color(156, 194, 120));
        UIManager.put("Panel.background", new Color(156, 194, 120));
        String[] botones = {"2 jugadores", "3 jugadores", "4 jugadores"};
        int ventana = JOptionPane.showOptionDialog(null,
                "Selecciona el numero de jugadores",
                "Menú de elección del Nº de jugadores",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                botones, botones[0]);

        //switch para dar valor a la variable de número de jugadores dependiendo del botón pulsado.
        switch (ventana){
            case 0: nJugadores = 2;
                break;
            case 1: nJugadores = 3;
                break;
            case 2: nJugadores = 4;
        }

        //bucle for para crear jugadores.
        for (int i = 0; i < nJugadores; i++) {
            String nombre = JOptionPane.showInputDialog(null,"Introduce nombre del jugador","",JOptionPane.PLAIN_MESSAGE);
            if(nombre.equals("")){nombre="Jugador"+(i+1);}
            listaJugadores.add(new Jugador(nombre, 1500, new ArrayList<>()));
        }

        return nJugadores;
    }

    /**
     * Método que se conecta a una base de datos para crear objetos de la clase Casilla
     * Y meterlos en una lista
     * @param listaCasillas recibe la lista donde guarda las casillas
     */
    public static void crearCasilla(ArrayList<Casillas> listaCasillas) {
        // recojo la instancia única
        Conexion miDB = Conexion.getInstance();
        // utilizo un método de la instancia
        for (int i = 1; i < 25; i++) {
            listaCasillas.add(new Casillas(miDB.getCasillaNombre(i), miDB.getCasillaPrecio(i), miDB.getCasillaCodigo(i),
                    miDB.getCasillaImpuesto(i), miDB.getCasillaPrecioCasa(i)));
        }
    }

    /**
     * Método que usas un menu para llamar al método de tirar dado
     * @param listaCasillas recibe la lista de las casillas
     * @param listaJugadores recibe la lista de los jugadores
     * @param jugador recibe el jugador del turno actual
     */
    public void turnos(ArrayList<Casillas> listaCasillas, ArrayList<Jugador> listaJugadores, Jugador jugador) {
        //Menu para tirar el dado
        UIManager.put("OptionPane.background", new Color(156, 194, 120));
        UIManager.put("Panel.background", new Color(156, 194, 120));
        String[] botones = {"Tirar dado"};
        int ventana = JOptionPane.showOptionDialog(null,
                "Pulsa para tirar el dado",
                "Dado",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE, null,
                botones, botones[0]);
        if (ventana == 0) {
            tirarDado(listaCasillas,listaJugadores, jugador);
            JOptionPane.showMessageDialog(null, "Has parado en la casilla: " + listaCasillas.get(jugador.getPosicion()).getNombre(),"", JOptionPane.PLAIN_MESSAGE);
        }
    }

    /**
     * Método que utiliza un numero random para mover al jugador x casillas
     * @param listaCasillas recibe la lista de las casillas
     * @param listaJugadores recibe la lista de los jugadores
     * @param jugador recibe el jugador que se va a mover
     */
    public void tirarDado(ArrayList<Casillas> listaCasillas,ArrayList<Jugador> listaJugadores, Jugador jugador) {
        //numero random que determina cuantas casillas se mueve el jugador
        Random rm = new Random();
        int avanzar = 1 + rm.nextInt(6);
        JOptionPane.showMessageDialog(null, "En el dado salio un: " + avanzar,"", JOptionPane.PLAIN_MESSAGE);

        //condiciones que permiten al jugador recorrer las casillas de forma cíclica
        if (jugador.getPosicion() + avanzar > (listaCasillas.size() - 1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());

        } else if (jugador.getPosicion() == (listaCasillas.size() - 1)) {
            jugador.setPosicion(jugador.getPosicion() + avanzar - listaCasillas.size());

        } else {
            jugador.setPosicion(jugador.getPosicion() + avanzar);
        }

    }
}
