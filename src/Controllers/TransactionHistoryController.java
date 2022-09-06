/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Core.Controller;
import Models.Auth;
import Models.Borrowing;
import Models.Reversion;
import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class TransactionHistoryController extends Controller implements Initializable {

    private final Borrowing borrowing;
    private final Reversion reversion;

    public TransactionHistoryController() {
        borrowing = new Borrowing();
        reversion = new Reversion();
    }

    @FXML
    private Label nameBox;

    @FXML
    private TableView tableRiwayat;
    @FXML
    private TableColumn<Borrowing, Integer> idCol;
    @FXML
    private TableColumn<Borrowing, String> memberCol;
    @FXML
    private TableColumn<Borrowing, String> adminCol;
    @FXML
    private TableColumn<Borrowing, String> borrowDateCol;
    @FXML
    private TableColumn<Borrowing, String> returnDateCol;

    private final ObservableList<String> statusList = FXCollections.observableArrayList("Semua", "Dikembalikan", "Belum Dikembalikan");

    ObservableList<Borrowing> BorrowingList = FXCollections.observableArrayList();

    @FXML
    private DatePicker startDateInput;
    @FXML
    private DatePicker endDateInput;
    @FXML
    private ComboBox statusInput;

    private void inputValidation() throws Exception {
        if (startDateInput.getValue() == null) {
            throw new Exception("Tanggal mulai tidak boleh kosong");
        }

        if (endDateInput.getValue() == null) {
            throw new Exception("Tanggal akhir tidak boleh kosong");
        }
    }

    @FXML
    public void btnCariHandle(ActionEvent act) {
        try {
            inputValidation();
            setDataTable();
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    @FXML
    public void btnResetHandle(ActionEvent act) {
        this.startDateInput.setValue(null);
        this.endDateInput.setValue(null);
        this.statusInput.getSelectionModel().select(0);
        setDataTable();
    }

    public void loadData() {
        try {
            BorrowingList.clear();
            ResultSet borrowings;

            LocalDate startDate = startDateInput.getValue();
            LocalDate endDate = endDateInput.getValue();
            int selectedStatus = statusInput.getSelectionModel().getSelectedIndex();

            if (startDate == null && endDate == null) {
                borrowings = this.borrowing.getAll();
            } else {
                switch (selectedStatus) {
                    case 1:
                        borrowings = this.borrowing.getWasReturn(startDate, endDate);
                        break;
                    case 2:
                        borrowings = this.borrowing.getNotReturn(startDate, endDate);
                        break;
                    default:
                        borrowings = this.borrowing.getAll(startDate, endDate);
                        break;
                }
            }

            while (borrowings.next()) {
                BorrowingList.add(new Borrowing(
                        borrowings.getInt("id"),
                        borrowings.getString("member_name"),
                        borrowings.getString("admin_name"),
                        borrowings.getTimestamp("borrow_date").toString(),
                        borrowings.getTimestamp("return_date").toString()
                ));
            }
            tableRiwayat.setItems(BorrowingList);
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void setDataTable() {
        loadData();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        memberCol.setCellValueFactory(new PropertyValueFactory<>("member_name"));
        adminCol.setCellValueFactory(new PropertyValueFactory<>("admin_name"));
        borrowDateCol.setCellValueFactory(new PropertyValueFactory<>("borrow_date"));
        returnDateCol.setCellValueFactory(new PropertyValueFactory<>("return_date"));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        statusInput.setItems(statusList);
        statusInput.getSelectionModel().select(0);
        setDataTable();
    }

}
