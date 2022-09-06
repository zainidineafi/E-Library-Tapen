/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Auth;
import Core.Controller;
import Models.Publisher;
import Utils.InputNumber;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author ASUS
 */
public class PublisherController extends Controller implements Initializable {

    private Publisher publisher = null;
    private Publisher selectionData = null;

    /**
     * Initializes the controller class.
     */

    public PublisherController() {
        publisher = new Publisher();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        nameBox.setText(Auth.getName().toUpperCase());
        formPage.setVisible(false);
        this.loadData();
        new InputNumber().getInputNumber(nomorInput);
    }

    @FXML
    private Label nameBox;
    @FXML
    private AnchorPane formPage;
    @FXML
    private Button btnKembaliFormPage;
    @FXML
    private Button btnSubmit;
    @FXML
    private Button btnTambah;
    @FXML
    private Button btnEdit;
    @FXML
    private Button btnHapus;
    @FXML
    private TextField idInput;
    @FXML
    private TextField nameInput;
    @FXML
    private TextField nomorInput;
    @FXML
    private TextArea addressInput;
    @FXML
    private TableView tablePenerbit;
    @FXML
    private TableColumn<Publisher, Integer> idCol;
    @FXML
    private TableColumn<Publisher, String> nameCol;
    @FXML
    private TableColumn<Publisher, String> phoneCol;
    @FXML
    private TableColumn<Publisher, String> addressCol;
    @FXML
    private TableColumn<Publisher, String> createdAtCol;

    ObservableList<Publisher> PublisherList = FXCollections.observableArrayList();

    public void btnTambahHandle(ActionEvent act) {
        this.btnSubmit.setText("Tambah");
        this.resetForm();
        formPage.setVisible(true);
    }

    public void btnEditHandle(ActionEvent act) {
        if (tablePenerbit.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih penerbit terlebih dahulu");
        } else {
            this.selectionData = (Publisher) tablePenerbit.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            this.btnSubmit.setText("Simpan");
            formPage.setVisible(true);
        }
    }

    public void btnHapusHandle(ActionEvent act) throws SQLException {
        if (tablePenerbit.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih penerbit terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghapus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Publisher) tablePenerbit.getSelectionModel().getSelectedItem();
                this.publisher.delete(this.selectionData.getId());
                loadData();
            }
        }
    }

    public void btnSubmitHandle(ActionEvent act) throws SQLException {
        try {
            this.inputValidation();
            String name = nameInput.getText(),
                    phone = nomorInput.getText(),
                    address = addressInput.getText();

            if (btnSubmit.getText().toLowerCase().equals("tambah")) {
                this.publisher.store(name, phone, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data petugas berhasil ditambahkan");
            } else {
                int id = Integer.valueOf(idInput.getText());
                ResultSet currentFullData = this.publisher.getById(id);
                String checkPassword = null;

                this.publisher.update(id, name, phone, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data petugas berhasil diubah");
            }

            this.loadData();
            formPage.setVisible(false);
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }

    }

    private void inputValidation() throws Exception {
        if ((nameInput.getText() == null || nameInput.getText().equals(""))
                || (nomorInput.getText() == null || nomorInput.getText().equals(""))
                || (addressInput.getText() == null || addressInput.getText().equals(""))) {
            throw new Exception("Nama wajib diisi");
        }
    }

    public void btnKembaliFormPageHandle(ActionEvent act) {
        this.resetForm();
        formPage.setVisible(false);
    }

    private void setForm(Publisher publisher) {
        idInput.setText(String.valueOf(publisher.getId()));
        nameInput.setText(publisher.getName());
        nomorInput.setText(publisher.getPhone());
        addressInput.setText(publisher.getAddress());
    }

    private void resetForm() {
        idInput.setText(null);
        nameInput.setText(null);
        nomorInput.setText(null);
        addressInput.setText(null);
    }

    @FXML
    private TextField search;

    public void searchMethod(KeyEvent evt) {
        loadData();
    }

    public void setDataTable() {
        try {
            PublisherList.clear();
            ResultSet publishers;
            if (search.getText().equals("") || search.getText() == null) {
                publishers = this.publisher.getAll();
            } else {
                publishers = this.publisher.getBySearch(search.getText());
            }
            while (publishers.next()) {
                PublisherList.add(new Publisher(
                        publishers.getInt("id"),
                        publishers.getString("name"),
                        publishers.getString("phone"),
                        publishers.getString("address"),
                        publishers.getTimestamp("created_at").toString()
                ));
                tablePenerbit.setItems(PublisherList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
    }

}
