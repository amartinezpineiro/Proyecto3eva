package com.adrian.clases;

import com.adrian.interfaz.Marco;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Tablero {
    //declaración del objeto mimarco para interactuar con la interfaz
    private Marco mimarco;
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
            //instancia de mimarco
            mimarco = new Marco();
            mimarco.setVisible(true);
            mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        //llamada al metodo que mueve las fichas
        moverFichas(listaJugadores,jugador);

    }
    public void moverFichas(ArrayList<Jugador> listaJugadores, Jugador jugador){
        if(jugador==listaJugadores.get(0)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ1pos(790,754);
                    break;
                case 1: mimarco.setJ1pos(660,775);
                    break;
                case 2: mimarco.setJ1pos(560,775);
                    break;
                case 3: mimarco.setJ1pos(460,775);
                    break;
                case 4: mimarco.setJ1pos(360,775);
                    break;
                case 5: mimarco.setJ1pos(260,775);
                    break;
                case 6: mimarco.setJ1pos(110,754);
                    break;
                case 7: mimarco.setJ1pos(110,625);
                    break;
                case 8: mimarco.setJ1pos(110,525);
                    break;
                case 9: mimarco.setJ1pos(110,425);
                    break;
                case 10: mimarco.setJ1pos(110,325);
                    break;
                case 11: mimarco.setJ1pos(110,225);
                    break;
                case 12: mimarco.setJ1pos(110,80);
                    break;
                case 13: mimarco.setJ1pos(250,90);
                    break;
                case 14: mimarco.setJ1pos(350,90);
                    break;
                case 15: mimarco.setJ1pos(450,90);
                    break;
                case 16: mimarco.setJ1pos(550,90);
                    break;
                case 17: mimarco.setJ1pos(650,90);
                    break;
                case 18: mimarco.setJ1pos(770,80);
                    break;
                case 19: mimarco.setJ1pos(790,220);
                    break;
                case 20: mimarco.setJ1pos(800,320);
                    break;
                case 21: mimarco.setJ1pos(800,420);
                    break;
                case 22: mimarco.setJ1pos(800,520);
                    break;
                case 23: mimarco.setJ1pos(800,620);
                    break;
            }
        }
        if(jugador==listaJugadores.get(1)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ2pos(790+60,754);
                    break;
                case 1: mimarco.setJ2pos(660+50,775);
                    break;
                case 2: mimarco.setJ2pos(560+50,775);
                    break;
                case 3: mimarco.setJ2pos(460+50,775);
                    break;
                case 4: mimarco.setJ2pos(360+50,775);
                    break;
                case 5: mimarco.setJ2pos(260+50,775);
                    break;
                case 6: mimarco.setJ2pos(110+60,754);
                    break;
                case 7: mimarco.setJ2pos(110+50,625);
                    break;
                case 8: mimarco.setJ2pos(110+50,525);
                    break;
                case 9: mimarco.setJ2pos(110+50,425);
                    break;
                case 10: mimarco.setJ2pos(110+50,325);
                    break;
                case 11: mimarco.setJ2pos(110+50,225);
                    break;
                case 12: mimarco.setJ2pos(110+60,80);
                    break;
                case 13: mimarco.setJ2pos(250+50,90);
                    break;
                case 14: mimarco.setJ2pos(350+50,90);
                    break;
                case 15: mimarco.setJ2pos(450+50,90);
                    break;
                case 16: mimarco.setJ2pos(550+50,90);
                    break;
                case 17: mimarco.setJ2pos(650+50,90);
                    break;
                case 18: mimarco.setJ2pos(770+50,80);
                    break;
                case 19: mimarco.setJ2pos(790+50,220);
                    break;
                case 20: mimarco.setJ2pos(800+50,320);
                    break;
                case 21: mimarco.setJ2pos(800+50,420);
                    break;
                case 22: mimarco.setJ2pos(800+50,520);
                    break;
                case 23: mimarco.setJ2pos(800+50,620);
                    break;
            }
        }
        if(jugador==listaJugadores.get(2)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ3pos(790,754+50);
                    break;
                case 1: mimarco.setJ3pos(660,775+50);
                    break;
                case 2: mimarco.setJ3pos(560,775+50);
                    break;
                case 3: mimarco.setJ3pos(460,775+50);
                    break;
                case 4: mimarco.setJ3pos(360,775+50);
                    break;
                case 5: mimarco.setJ3pos(260,775+50);
                    break;
                case 6: mimarco.setJ3pos(110,754+50);
                    break;
                case 7: mimarco.setJ3pos(110,625+50);
                    break;
                case 8: mimarco.setJ3pos(110,525+50);
                    break;
                case 9: mimarco.setJ3pos(110,425+50);
                    break;
                case 10: mimarco.setJ3pos(110,325+50);
                    break;
                case 11: mimarco.setJ3pos(110,225+50);
                    break;
                case 12: mimarco.setJ3pos(110,80+50);
                    break;
                case 13: mimarco.setJ3pos(250,90+50);
                    break;
                case 14: mimarco.setJ3pos(350,90+50);
                    break;
                case 15: mimarco.setJ3pos(450,90+50);
                    break;
                case 16: mimarco.setJ3pos(550,90+50);
                    break;
                case 17: mimarco.setJ3pos(650,90+50);
                    break;
                case 18: mimarco.setJ3pos(770,80+50);
                    break;
                case 19: mimarco.setJ3pos(790,220+50);
                    break;
                case 20: mimarco.setJ3pos(800,320+50);
                    break;
                case 21: mimarco.setJ3pos(800,420+50);
                    break;
                case 22: mimarco.setJ3pos(800,520+50);
                    break;
                case 23: mimarco.setJ3pos(800,620+50);
                    break;
            }
        }
        if(jugador==listaJugadores.get(3)){
            switch (jugador.getPosicion()){
                case 0: mimarco.setJ4pos(790+60,754+50);
                    break;
                case 1: mimarco.setJ4pos(660+50,775+50);
                    break;
                case 2: mimarco.setJ4pos(560+50,775+50);
                    break;
                case 3: mimarco.setJ4pos(460+50,775+50);
                    break;
                case 4: mimarco.setJ4pos(360+50,775+50);
                    break;
                case 5: mimarco.setJ4pos(260+50,775+50);
                    break;
                case 6: mimarco.setJ4pos(110+60,754+50);
                    break;
                case 7: mimarco.setJ4pos(110+50,625+50);
                    break;
                case 8: mimarco.setJ4pos(110+50,525+50);
                    break;
                case 9: mimarco.setJ4pos(110+50,425+50);
                    break;
                case 10: mimarco.setJ4pos(110+50,325+50);
                    break;
                case 11: mimarco.setJ4pos(110+50,225+50);
                    break;
                case 12: mimarco.setJ4pos(110+60,80+50);
                    break;
                case 13: mimarco.setJ4pos(250+50,90+50);
                    break;
                case 14: mimarco.setJ4pos(350+50,90+50);
                    break;
                case 15: mimarco.setJ4pos(450+50,90+50);
                    break;
                case 16: mimarco.setJ4pos(550+50,90+50);
                    break;
                case 17: mimarco.setJ4pos(650+50,90+50);
                    break;
                case 18: mimarco.setJ4pos(770+50,80+50);
                    break;
                case 19: mimarco.setJ4pos(790+50,220+50);
                    break;
                case 20: mimarco.setJ4pos(800+50,320+50);
                    break;
                case 21: mimarco.setJ4pos(800+50,420+50);
                    break;
                case 22: mimarco.setJ4pos(800+50,520+50);
                    break;
                case 23: mimarco.setJ4pos(800+50,620+50);
                    break;
            }
        }
        }
    }
