/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Auth;
import Models.Shelf;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class PercentageBookController extends Controller implements Initializable {

    @FXML
    private PieChart pieChart;
    
    @FXML
    private Label percentLabel;

    @FXML
    private Label nameBox;

    private void setPieChart() {
        
        try {
            Shelf shelf = new Shelf();
            ResultSet counterShelf = shelf.getBookCount();

            while (counterShelf.next()) {
                PieChart.Data pieData = new PieChart.Data(counterShelf.getString("name"), counterShelf.getInt("book_count"));
                pieChart.getData().add(pieData);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
        
        percentLabel.setTextFill(Color.BLACK);

        pieChart.getData().forEach((data) -> {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
                percentLabel.setText("Persentase : " + String.valueOf(data.getPieValue()) + "%");
            });
        });
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        setPieChart();
    }
}
