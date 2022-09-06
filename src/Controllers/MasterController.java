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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class MasterController extends Controller implements Initializable {
    public MasterController() {
        super();
    }
    
    public void btnPetugasHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Admin.fxml");
    }
    
    public void btnPengarangHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Author.fxml");
    }
    
    public void btnPenerbitHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Publisher.fxml");
    }
    
    public void btnAnggotaHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Member.fxml");
    }
    
    public void btnRakHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Shelf.fxml");
    }
    
    public void btnBukuHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Book.fxml");
    }
    
    public void btnStockHandle(MouseEvent mct) throws IOException {
        main.render("../View/Master/Stock.fxml");
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
