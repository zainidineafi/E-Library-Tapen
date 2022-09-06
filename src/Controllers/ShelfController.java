/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Shelf;
import Models.Auth;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author acer
 */
public class ShelfController extends Controller implements Initializable {

    private Shelf shelf = null;
    private Shelf selectionData = null;

    public ShelfController() {
        shelf = new Shelf();
    }

    @FXML
    private Label nameBox;

    @FXML
    private AnchorPane formPage;

    @FXML
    private TableView tableRak;
    @FXML
    private TableColumn<Shelf, Integer> idCol;
    @FXML
    private TableColumn<Shelf, String> codeCol;
    @FXML
    private TableColumn<Shelf, String> nameCol;
    @FXML
    private TableColumn<Shelf, String> createdAtCol;

    ObservableList<Shelf> ShelfList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        formPage.setVisible(false);
        this.loadData();
    }

    @FXML
    private Button btnTambah;

    public void btnTambahHandle(ActionEvent act) {
        this.btnSubmit.setText("Tambah");
        this.resetForm();
        formPage.setVisible(true);
    }

    @FXML
    private Button btnEdit;

    public void btnEditHandle(ActionEvent act) {
        if (tableRak.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih rak terlebih dahulu");
        } else {
            this.selectionData = (Shelf) tableRak.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            this.btnSubmit.setText("Simpan");
            formPage.setVisible(true);
        }
    }

    @FXML
    private Button btnHapus;

    public void btnHapusHandle(ActionEvent act) throws SQLException {
        if (tableRak.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih rak terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghapus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Shelf) tableRak.getSelectionModel().getSelectedItem();
                this.shelf.delete(this.selectionData.getId());
                loadData();
            }
        }
    }

    //Halaman form tambah dan edit
    @FXML
    private Button btnKembaliFormPage;

    public void btnKembaliFormPageHandle(ActionEvent act) {
        this.resetForm();
        formPage.setVisible(false);
    }

    @FXML
    private TextField idInput;
    @FXML
    private TextField codeInput;
    @FXML
    private TextField nameInput;

    private void setForm(Shelf shelf) {
        idInput.setText(String.valueOf(shelf.getId()));
        codeInput.setText(shelf.getCode());
        nameInput.setText(shelf.getName());
    }

    private void resetForm() {
        idInput.setText(null);
        codeInput.setText(null);
        nameInput.setText(null);

    }

    private void inputValidation() throws Exception {
        if ((codeInput.getText() == null || codeInput.getText().equals(""))
                || (nameInput.getText() == null || nameInput.getText().equals(""))) {
            throw new Exception("Data wajib diisi");
        }

    }

    @FXML
    private Button btnSubmit;

    public void btnSubmitHandle(ActionEvent act) {

        try {
            this.inputValidation();
            String code = codeInput.getText(),
                    name = nameInput.getText();
            if (btnSubmit.getText().toLowerCase().equals("tambah")) {
                this.shelf.store(code, name);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data rak berhasil ditambahkan");
            } else {
                int id = Integer.valueOf(idInput.getText());
                this.shelf.update(id, code, name);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data rak berhasil diubah");
            }
            loadData();
            formPage.setVisible(false);
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Upss...", "", e.getMessage());
        }
    }

    @FXML
    private TextField search;

    public void searchMethod(KeyEvent evt) {
        loadData();
    }

//       Set Tableview
    public void setDataTable() {
        try {
            ShelfList.clear();
            ResultSet shelves;
            if (search.getText() == null || search.getText().equals("")) {
                shelves = this.shelf.getAll();
            } else {
                shelves = this.shelf.getBySearch(search.getText());
            }

            while (shelves.next()) {
                ShelfList.add(new Shelf(
                        shelves.getInt("id"),
                        shelves.getString("code"),
                        shelves.getString("name"),
                        shelves.getTimestamp("created_at").toString()
                ));
                tableRak.setItems(ShelfList);
            }

        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
    }

}
