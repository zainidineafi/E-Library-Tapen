/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Main.Main;
import Config.BCrypt;
import Models.Auth;
import Core.Controller;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class LoginController extends Controller implements Initializable {
    protected Auth authModel;
    protected Main main;
    
    public LoginController() {
        authModel = new Auth();
        main = new Main();
    }
        
    @FXML
    private Label info;
    
    @FXML
    private TextField usernameInput;
    
    @FXML
    private TextField passwordInput;
    
    @FXML
    private Button login;
    
    @FXML
    private void loginAction(ActionEvent act)
    {   
        String username = usernameInput.getText(), 
               password = passwordInput.getText();
        
        try {
            ResultSet authUser = this.authModel.getByUsername(username);
            if(username.equals("") || password.equals("")) {
                throw new Exception("Username dan password wajib terisi");
            } else {
                if(authUser.next() && BCrypt.checkpw(password, authUser.getString("password"))) {
                    this.authModel.setId(authUser.getInt("id"));
                    this.authModel.setUsername(authUser.getString("username"));
                    this.authModel.setName(authUser.getString("name"));

                    main.render("../View/Pages/Dashboard.fxml");
                } else {
                    throw new Exception("Kredensial tidak ditemukan dalam database kami");
                }
            }
        } catch(Exception e) {
            String message = e.getMessage();
            info.setTextFill(Color.RED);
            info.setText("Error : " + message);
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameInput.setText("admin");
        passwordInput.setText("admin");
        
        passwordInput.setOnAction(e -> {
            loginAction(e);
        });
    }    
    
}
