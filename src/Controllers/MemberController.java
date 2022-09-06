/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Auth;
import Models.Member;
import Utils.InputNumber;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class MemberController extends Controller implements Initializable {

    private Member member = null;
    private Member selectionData = null;
    private final ObservableList<String> genderList = FXCollections.observableArrayList("Pria", "Wanita");

    public MemberController() {
        member = new Member();
    }

    @FXML
    private Label nameBox;

    @FXML
    private AnchorPane formPage;

    @FXML
    private TableView tableAnggota;
    @FXML
    private TableColumn<Member, Integer> idCol;
    @FXML
    private TableColumn<Member, Integer> pointCol;
    @FXML
    private TableColumn<Member, String> nameCol;
    @FXML
    private TableColumn<Member, String> phoneCol;
    @FXML
    private TableColumn<Member, String> genderCol;
    @FXML
    private TableColumn<Member, String> addressCol;
    @FXML
    private TableColumn<Member, String> createdAtCol;

    ObservableList<Member> MemberList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
        if (tableAnggota.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih anggota terlebih dahulu");
        } else {
            this.selectionData = (Member) tableAnggota.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            this.btnSubmit.setText("Simpan");
            formPage.setVisible(true);
        }
    }

    @FXML
    private Button btnHapus;

    public void btnHapusHandle(ActionEvent act) throws SQLException {
        if (tableAnggota.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih anggota terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghapus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Member) tableAnggota.getSelectionModel().getSelectedItem();
                this.member.delete(this.selectionData.getId());
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
    private TextField nomorInput;
    @FXML
    private TextArea addressInput;

    private void setForm(Member member) {
        idInput.setText(String.valueOf(member.getId()));
        nameInput.setText(member.getName());
        genderInput.setValue(member.getGender());
        nomorInput.setText(member.getPhone());
        addressInput.setText(member.getAddress());
    }

    private void resetForm() {
        idInput.setText(null);
        nameInput.setText(null);
        genderInput.setValue(null);
        nomorInput.setText(null);
        addressInput.setText(null);
    }

    private void inputValidation() throws Exception {
        if ((nameInput.getText() == null || nameInput.getText().equals(""))
                || (genderInput.getValue() == null || genderInput.getValue().equals(""))
                || (nomorInput.getText() == null || nomorInput.getText().equals(""))
                || (addressInput.getText() == null || addressInput.getText().equals(""))) {
            throw new Exception("Data wajib diisi");
        }
    }

    @FXML
    private Button btnSubmit;

    public void btnSubmitHandle(ActionEvent act) {
        try {
            this.inputValidation();
            String name = nameInput.getText(),
                    phone = nomorInput.getText(),
                    gender = (String) genderInput.getValue(),
                    address = addressInput.getText();
            if (btnSubmit.getText().toLowerCase().equals("tambah")) {
                this.member.store(name, phone, gender, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data anggota berhasil ditambahkan");
            } else {
                int id = Integer.valueOf(idInput.getText());
                this.member.update(id, name, phone, gender, address);

                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data anggota berhasil diubah");
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

//    Set TableView
    public void setDataTable() {
        try {
            MemberList.clear();
            ResultSet members;
            if (search.getText() == null || search.getText().equals("")) {
                members = this.member.getAll();
            } else {
                members = this.member.getBySearch(search.getText());
            }

            while (members.next()) {
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
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        pointCol.setCellValueFactory(new PropertyValueFactory<>("point"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));

    }
}
