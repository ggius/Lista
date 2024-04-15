/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package giuseppevitolo;

//import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
//import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
//import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
//import java.util.TreeMap;
//import java.util.logging.Level;
//import java.util.logging.Logger;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
//import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
/**
 *
 * @author giuseppe
 */
public class FXMLDocumentController implements Initializable {
    private ObservableList<Evento> list;
    private SimpleListProperty<Evento> listasimple;

    @FXML
    private Button agg;
    @FXML
    private DatePicker date;
    @FXML
    private TextField descr_tf;
    @FXML
    private TableView<Evento> tab;
    @FXML
    private TableColumn<Evento, LocalDate> data_col;
    @FXML
    private TableColumn<Evento, String> event_col;
    @FXML
    private MenuItem del_menu;
    @FXML
    private MenuItem imp_menu;
    @FXML
    private MenuItem exp_menu;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        list=FXCollections.observableArrayList();
        listasimple= new SimpleListProperty();
        
        
        date.setValue(LocalDate.now());
        
        try {
            resume();
        } catch (ClassNotFoundException ex) {
        }
        agg.disableProperty().bind(descr_tf.textProperty().isEmpty());
        exp_menu.disableProperty().bind(listasimple.emptyProperty());
        del_menu.disableProperty().bind(listasimple.emptyProperty());
        imp_menu.disableProperty().bind(listasimple.emptyProperty().not());
        
        data_col.setCellValueFactory(new PropertyValueFactory("data"));
        event_col.setCellValueFactory(new PropertyValueFactory("descrizione"));
        
        event_col.setCellFactory(TextFieldTableCell.forTableColumn());
        
        tab.setItems(list);
        listasimple.setValue(list);
        Thread t = new Thread(new TimedSaving(list));
        t.start();
        
    }    
    
    @FXML
    private void aggiungiEvento(ActionEvent event) {
        Evento a =  new Evento(date.getValue(), descr_tf.getText());
        if(list.isEmpty()){
            list.add(a);
        }else{
        for(int i =0; i<list.size();i++){
            if(a.compareTo(list.get(i))<0){
                list.add(i, a);
                break;
            }else if (i==list.size()-1){
                list.add(a);
                break;
            }
        }
        }
        
        descr_tf.clear();
    }
    
    @FXML
    private void delete_event(ActionEvent event) {
        list.remove(tab.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void Import(ActionEvent event) {
         FileChooser fileChooser = new FileChooser();
        
        fileChooser.setTitle("Seleziona un file");

        File ListaEventi = fileChooser.showOpenDialog(null);
        try(Scanner o = new Scanner(new BufferedReader(new FileReader(ListaEventi)))){
            while(o.hasNext()){
                String e1 = o.nextLine();
                String [] e2 = e1.split(";");
                list.add(new Evento(LocalDate.parse(e2[0]), e2[1]));
            }
        } catch (FileNotFoundException ex) {}
        
    }

    @FXML
    private void export(ActionEvent event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ListaEventi.csv"))) {
            for (Evento evento : list) {
                String data = evento.getData().toString();
                String descrizione = evento.getDescrizione().replace(';', '|');
                String line = data + ";" + descrizione + "\n";
                writer.write(line);
            }
        } catch (IOException ex) {
        }
        
    }
    @FXML
    private void updateCell(TableColumn.CellEditEvent<Evento,String> event){
        Evento ev = tab.getSelectionModel().getSelectedItem();
        ev.setDescrizione(event.getNewValue());
    
    }
    private void resume() throws ClassNotFoundException{
    try(ObjectInputStream o = new ObjectInputStream(new BufferedInputStream(new FileInputStream("ListaEventi.class")))){
            List<Evento> salvataggio = (List<Evento>) o.readObject();
            list = FXCollections.observableArrayList(salvataggio);
        }   catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
            
        }
    
    
}