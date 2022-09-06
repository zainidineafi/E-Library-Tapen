/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Member;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class PopupMemberController extends Controller implements Initializable {
    private Member member, selectedData;
    ObservableList<Member> MemberList = FXCollections.observableArrayList();
    
    public Member getSelectedData() {
        return this.selectedData;
    }
    
    public PopupMemberController() {
        this.member = new Member();
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }    
    
    @FXML
    private TableView tableAnggota;
    @FXML
    private TableColumn <Member, Integer> idCol;
    @FXML
    private TableColumn <Member, String> nameCol;
    @FXML
    private TableColumn <Member, Integer> pointCol;
    @FXML
    private TableColumn <Member, String> phoneCol;
    
    public void btnTambahHandle(ActionEvent act) {
        if(tableAnggota.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih anggota terlebih dahulu");
        } else {
            this.selectedData = (Member) tableAnggota.getSelectionModel().getSelectedItem();
            ((Node)(act.getSource())).getScene().getWindow().hide();
        }
    }
    
    @FXML
    private TextField search;
    
    public void searchHandle(KeyEvent kev) {
        loadData();
    }
    
    // Set Datatable
    public void setDataTable() {
        try {
            MemberList.clear();
            ResultSet members;
            if(search.getText().equals("") || search.getText() == null) {
                members = this.member.getAll();
            } else {
                members = this.member.getBySearch(search.getText());
            }
            
            while(members.next()) {
                MemberList.add(new Member(
                    members.getInt("id"),
                    members.getInt("point"),
                    members.getString("name"),
                    members.getString("phone"),
                    members.getString("gender"),
                    members.getString("address"),
                    members.getTimestamp("created_at").toString()
                ));
                tableAnggota.setItems(MemberList);
            }
        } catch(Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }
    
    public void loadData() {
        this.setDataTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        pointCol.setCellValueFactory(new PropertyValueFactory<>("point"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
    }
}
