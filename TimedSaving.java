/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
/**
 *
 * @author giuseppe
 */
public class TimedSaving implements Runnable{
    private ObservableList<Evento> list;

    public TimedSaving(ObservableList<Evento> list) {
        this.list = list;
    }
    
    @Override
    public void run() {
        while(true){
            try(ObjectOutputStream o = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("ListaEventi.class")))){
            List<Evento> lista = new LinkedList<>(list); 
            o.writeObject(lista);
        }   catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException ex) {
            }
        }
    }   
}