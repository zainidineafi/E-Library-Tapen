/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Config.BCrypt;
import Models.Admin;
import Models.Auth;
import Utils.InputNumber;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
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
 * @author Illuminate
 */
public class AdminController extends Controller implements Initializable {

    private Admin admin = null;
    private Admin selectionData = null;
    private final ObservableList<String> genderList = FXCollections.observableArrayList("Pria", "Wanita");

    public AdminController() {
        admin = new Admin();
    }

    @FXML
    private Label nameBox;

    @FXML
    private AnchorPane formPage;

    @FXML
    private TableView tablePetugas;
    @FXML
    private TableColumn<Admin, Integer> idCol;
    @FXML
    private TableColumn<Admin, String> nameCol;
    @FXML
    private TableColumn<Admin, String> usernameCol;
    @FXML
    private TableColumn<Admin, String> phoneCol;
    @FXML
    private TableColumn<Admin, String> genderCol;
    @FXML
    private TableColumn<Admin, String> addressCol;
    @FXML
    private TableColumn<Admin, String> createdAtCol;

    ObservableList<Admin> AdminList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        formPage.setVisible(false);
        this.genderInput.setItems(genderList);
        this.loadData();

        new InputNumber().getInputNumber(nomorInput);
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
        if (tablePetugas.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih petugas terlebih dahulu");
        } else {
            this.selectionData = (Admin) tablePetugas.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            this.btnSubmit.setText("Simpan");
            formPage.setVisible(true);
        }
    }

    @FXML
    private Button btnHapus;

    public void btnHapusHandle(ActionEvent act) throws SQLException {
        if (tablePetugas.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih petugas terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghpus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Admin) tablePetugas.getSelectionModel().getSelectedItem();
                this.admin.delete(this.selectionData.getId());
                loadData();
            }
        }
    }

    //    Halaman From Tambah dan Edit
    @FXML
    private Button btnKembaliFormPage;

    public void btnKembaliFormPageHandle(ActionEvent act) {
        this.resetForm();
        formPage.setVisible(false);
    }

    @FXML
    private TextField idInput;
    @FXML
    private TextField nameInput;
    @FXML
    private ComboBox genderInput;
    @FXML
    private TextField usernameInput;
    @FXML
    private TextField passwordInput;
    @FXML
    private TextField nomorInput;
    @FXML
    private TextArea addressInput;

    private void setForm(Admin admin) {
        idInput.setText(String.valueOf(admin.getId()));
        nameInput.setText(admin.getName());
        genderInput.setValue(admin.getGender());
        usernameInput.setText(admin.getUsername());
        nomorInput.setText(admin.getPhone());
        addressInput.setText(admin.getAddress());
    }

    private void resetForm() {
        idInput.setText("");
        nameInput.setText("");
        genderInput.setValue("");
        usernameInput.setText("");
        passwordInput.setText("");
        nomorInput.setText("");
        addressInput.setText("");
    }

    private void inputValidation() throws Exception {
        if ((nameInput.getText() == null || nameInput.getText().equals(""))
                || (genderInput.getValue() == null)
                || (usernameInput.getText() == null || usernameInput.getText().equals(""))
                || (passwordInput.getText() == null && btnSubmit.getText().toLowerCase().equals("tambah") || passwordInput.getText().equals("") && btnSubmit.getText().toLowerCase().equals("tambah"))
                || (nomorInput.getText() == null || nomorInput.getText().equals(""))
                || (addressInput.getText() == null || addressInput.getText().equals(""))) {
            throw new Exception("Data wajib diisi");
        }

        if (btnSubmit.getText().toLowerCase().equals("tambah")) {
            if (passwordInput.getText().length() < 6) {
                throw new Exception("Password harus lebih dari 6 karakter");
            }
        } else {
            if ((!passwordInput.getText().equals("") && passwordInput.getText().length() < 6)) {
                throw new Exception("Password harus lebih dari 6 karakter");
            }
        }
    }

    @FXML
    private Button btnSubmit;

    public void btnSubmitHandle(ActionEvent act) {
        try {
            System.out.println(passwordInput.getText() == null && btnSubmit.getText().toLowerCase().equals("tambah") || passwordInput.getText().equals("") && btnSubmit.getText().toLowerCase().equals("tambah"));
            this.inputValidation();
            String name = nameInput.getText(),
                    username = usernameInput.getText().toLowerCase(),
                    password = passwordInput.getText().equals("") ? passwordInput.getText() : BCrypt.hashpw(passwordInput.getText(), BCrypt.gensalt()),
                    phone = nomorInput.getText(),
                    gender = (String) genderInput.getValue(),
                    address = addressInput.getText();

            if (btnSubmit.getText().toLowerCase().equals("tambah")) {
                this.admin.store(name, username, password, phone, gender, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data petugas berhasil ditambahkan");
            } else {
                int id = Integer.valueOf(idInput.getText());
                ResultSet currentFullData = this.admin.getById(id);
                String checkPassword = null;
                while (currentFullData.next()) {
                    checkPassword = password.equals("") ? currentFullData.getString("password") : password;
                }
                this.admin.update(id, name, username, checkPassword, phone, gender, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data petugas berhasil diubah");
            }
            this.resetForm();
            this.loadData();
            formPage.setVisible(false);
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    @FXML
    private TextField search;

    public void searchMethod(KeyEvent evt) {
        loadData();
    }

    // Set Tableview
    public void setDataTable() {
        try {
            AdminList.clear();
            ResultSet admins;
            if (search.getText().equals("") || search.getText() == null) {
                admins = this.admin.getAll();
            } else {
                admins = this.admin.getBySearch(search.getText());
            }

            while (admins.next()) {
                if (!admins.getString("username").equals(Auth.getUsername())) {
                    AdminList.add(new Admin(
                            admins.getInt("id"),
                            admins.getString("name"),
                            admins.getString("username"),
                            admins.getString("phone"),
                            admins.getString("gender"),
                            admins.getString("address"),
                            admins.getTimestamp("created_at").toString()
                    ));
                    tablePetugas.setItems(AdminList);
                }
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
    }
}
