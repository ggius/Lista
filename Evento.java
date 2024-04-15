/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

import java.io.Serializable;
import java.time.LocalDate;
//import java.util.Date;

/**
 *
 * @author giuseppe
 */


public class Evento implements Comparable<Evento>, Serializable {
    private LocalDate data;
    private String descrizione;
    private static int nextID = 1;
    private int ID;

    public Evento(LocalDate data, String descrizione) {
        this.data = data;
        this.descrizione = descrizione;
        this.ID = nextID++;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public LocalDate getData() {
        return data;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public int getID() {
        return ID;
    }

    @Override
    public int compareTo(Evento altroEvento) {
        if (this.data.equals(altroEvento.getData())) {
            return Integer.compare(this.ID, altroEvento.getID());
        } else {
            return this.data.compareTo(altroEvento.getData());
        }
    }
}