/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Auth;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class TransactionController extends Controller implements Initializable {

    public void btnPeminjamanHandle(MouseEvent mct) throws IOException {
        main.render("../View/Transaction/Borrowing.fxml");
    }
    
    public void btnPengembalianHandle(MouseEvent mct) throws IOException {
        main.render("../View/Transaction/Reversion.fxml");
    }
    
    @FXML
    private Label nameBox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
    }    
    
}
