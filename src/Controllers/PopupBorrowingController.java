/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Book;
import Models.Borrowing;
import Utils.InputNumber;
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
import javafx.scene.control.Button;
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
public class PopupBorrowingController extends Controller implements Initializable {

    private Borrowing borrowings, selectedData;
    ObservableList<Borrowing> BorrowingList = FXCollections.observableArrayList();

    public PopupBorrowingController() {
        borrowings = new Borrowing();
    }

    public Borrowing getSelectedData() {
        return this.selectedData;
    }

    @FXML
    private TextField search;
    @FXML
    private TableView tablePeminjaman;
    @FXML
    private TableColumn<Borrowing, Integer> idCol;
    @FXML
    private TableColumn<Borrowing, Integer> totalCol;
    @FXML
    private TableColumn<Borrowing, String> nameCol;

    @FXML
    private Button btnTambah;

    @FXML
    public void btnTambahHandle(ActionEvent act) {
        if (tablePeminjaman.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih peminjaman terlebih dahulu");
        } else {
            this.selectedData = (Borrowing) tablePeminjaman.getSelectionModel().getSelectedItem();
            ((Node) (act.getSource())).getScene().getWindow().hide();
        }
    }

    @FXML
    public void searchHandle(KeyEvent event) {
        loadData();
    }

    // datatable
    public void setDataTable() {
        try {
            BorrowingList.clear();
            ResultSet borrowings;

            if (search.getText().equals("") || search.getText() == null) {
                borrowings = this.borrowings.getNotReturned();
            } else {
                borrowings = this.borrowings.getNotReturned(Integer.valueOf(search.getText()));
            }

            while (borrowings.next()) {
                BorrowingList.add(new Borrowing(
                        borrowings.getInt("id"),
                        borrowings.getInt("member_id"),
                        borrowings.getInt("total"),
                        borrowings.getString("name"),
                        borrowings.getTimestamp("return_date").toString()
                ));
                tablePeminjaman.setItems(BorrowingList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("member_name"));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();

        new InputNumber().getInputNumber(search);
    }

}
