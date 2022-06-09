package com.adrian.clases;

import java.util.ArrayList;

public class Jugador {
    private String nombre;
    private int dinero;
    private int posicion = 0;
    private ArrayList<Casillas> propiedades;
    private int pierdeTurno = 0;

    public Jugador(String nombre, int dinero, ArrayList<Casillas> propiedades) {
        this.nombre = nombre;
        this.dinero = dinero;
        this.propiedades = propiedades;
    }

    //getters de los atributos necesarios
    public int getPierdeTurno() {
        return pierdeTurno;
    }


    public String getNombre() {
        return nombre;
    }

    public int getPosicion() {
        return posicion;
    }

    public int getDinero() {
        return dinero;
    }


    public ArrayList<Casillas> getPropiedades() {
        return propiedades;
    }

    //setters de los atributos necesarios
    public void setDinero(int dinero) {
        this.dinero = dinero;
    }

    public void setPierdeTurno(int pierdeTurno) {
        this.pierdeTurno = pierdeTurno;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
}
