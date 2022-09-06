/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Core;

import Main.Main;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

/**
 *
 * @author Illuminate
 */
public abstract class Controller {
    protected Main main;
    
    public Controller() {
        main = new Main();
    }
    
    @FXML
    private Button btnDashboard;
    
    public void btnDashboardHandle(ActionEvent act) throws IOException {
        main.render("../View/Pages/Dashboard.fxml");
    }
    
    @FXML
    private Button btnTransaksi;
    
    public void btnTransaksiHandle(ActionEvent act) throws IOException {
        main.render("../View/Pages/Transaction.fxml");
    }
    
    @FXML
    private Button btnMaster;
    
    public void btnMasterHandle(ActionEvent act) throws IOException {
        main.render("../View/Pages/Master.fxml");
    }
    
    @FXML
    private Button btnLaporan;
    
    public void btnLaporanHandle(ActionEvent act) throws IOException {
        main.render("../View/Pages/Report.fxml");
    }
    
    @FXML
    private Button btnLogout;
    
    public void btnLogoutHandle(ActionEvent act) throws IOException {
        if (showConfirm("", "Anda yakin akan keluar?").get() == ButtonType.OK) {
            main.render("../View/Auth/Login.fxml");
        }
    }
    
    public void showAlert(Alert.AlertType type, String title, String header, String body) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        if(header.equals("")) {
            alert.setHeaderText(null);
        } else {
            alert.setHeaderText(header);
        }
        alert.setContentText(body);

        alert.showAndWait();
    }
    
    public Optional showConfirm(String header, String body) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Perhatian");
        if(header.equals("")) {
            alert.setHeaderText(null);
        } else {
            alert.setHeaderText(header);
        }
        alert.setContentText(body);

        Optional result = alert.showAndWait();
        return result;
    }
}
