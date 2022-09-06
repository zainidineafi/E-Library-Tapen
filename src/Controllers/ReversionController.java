/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Config.Database;
import Core.Controller;
import Models.Auth;
import Models.Book;
import Models.Borrowing;
import Models.BorrowingDetail;
import Models.Member;
import Models.Reversion;
import Models.ReversionDetail;
import Utils.Time;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class ReversionController extends Controller implements Initializable {

    private final ObservableList<String> statusList = FXCollections.observableArrayList("Dikembalikan", "Hilang");
    private final Borrowing borrowings;
    private final BorrowingDetail borrowing_details;
    private final Reversion reversions;
    private final ReversionDetail reversion_details;
    private final Book books;
    private final Member members;
    private Borrowing popupBorrowingData;
    private ReversionDetail selectionData = null;

    public ReversionController() {
        borrowings = new Borrowing();
        borrowing_details = new BorrowingDetail();
        reversions = new Reversion();
        reversion_details = new ReversionDetail();
        books = new Book();
        members = new Member();
    }

    @FXML
    private Label nameBox;

    @FXML
    private AnchorPane formPage;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        statusInput.setItems(statusList);
        formPage.setVisible(false);
    }

    @FXML
    private TextField idInput, nameInput, dendaTunaiInput, dendaPointInput, titleInput;

    @FXML
    private ComboBox statusInput;

    public void btnEditHandle(ActionEvent act) {
        if (tableBook.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih buku terlebih dahulu");
        } else {
            this.selectionData = (ReversionDetail) tableBook.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            formPage.setVisible(true);
        }
    }

    public void setForm(ReversionDetail model) {
        titleInput.setText(model.getName());
        statusInput.setValue(model.getStatus());
    }

    public void btnKembaliFormPageHandle(ActionEvent act) {
        formPage.setVisible(false);
    }

    public void btnSimpanHandle(ActionEvent act) {
        String status = statusInput.getValue().toString();
        this.selectionData.setStatus(status);
        int index = reversionDetailList.indexOf(this.selectionData);

        reversionDetailList.set(index, this.selectionData);
        // Denda hilang
        if (status.equals(statusList.get(1))) {
            int dendaTunai = Integer.valueOf(dendaTunaiInput.getText());
            dendaTunaiInput.setText(String.valueOf(dendaTunai + this.selectionData.getPrice()));
        }

        // Mengurangi denda karena tidak jadi hilang
        if (status.equals(statusList.get(0))) {
            int dendaTunai = Integer.valueOf(dendaTunaiInput.getText());
            dendaTunaiInput.setText(String.valueOf(dendaTunai - this.selectionData.getPrice()));
        }

        formPage.setVisible(false);
    }

    public void btnSubmitHandle(ActionEvent act) {
        if (showConfirm("", "Anda yakin aksi ini akan diproses?").get() == ButtonType.OK) {
            try {
                Connection conn = Database.GetConnection();
                PreparedStatement ps;
                String query;
                int transactionKey = 0;
                try {
                    conn.setAutoCommit(false);
                    query = "INSERT INTO " + this.reversions.getTable() + " (id, borrowing_id, cash_penalty, point_penalty, created_at, updated_at) VALUES (NULL, " + this.popupBorrowingData.getId() + ", " + this.dendaTunaiInput.getText() + ", " + this.dendaPointInput.getText() + ", NOW(), NOW())";
                    ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.execute();
                    ResultSet res = ps.getGeneratedKeys();

                    if (res.next()) {
                        transactionKey = res.getInt(1);
                        query = "INSERT INTO " + this.reversion_details.getTable() + " (id, reversion_id, borrowing_detail_id, status, created_at, updated_at) VALUES ";
                        for (int i = 0; i < this.reversionDetailList.size(); i++) {
                            if (i != 0) {
                                query += ", ";
                            }
                            query += "(NULL, " + transactionKey + ", " + this.reversionDetailList.get(i).getBorrowing_detail_id() + ", '" + this.reversionDetailList.get(i).getStatus() + "', NOW(), NOW())";
                        }

                        ps = conn.prepareStatement(query);
                        ps.execute();

                        for (int i = 0; i < this.reversionDetailList.size(); i++) {
                            if (!this.reversionDetailList.get(i).getStatus().equals(this.statusList.get(1))) {
                                query = "UPDATE " + this.books.getTable() + " SET stock = stock + 1 WHERE id = " + this.reversionDetailList.get(i).getBook_id();
                                ps = conn.prepareStatement(query);
                                ps.execute();
                            }
                        }
                    }

                    query = "UPDATE " + this.members.getTable() + " SET point = point + 5 WHERE id = " + this.popupBorrowingData.getMember_id();
                    ps = conn.prepareStatement(query);
                    ps.execute();

                    query = "UPDATE " + this.members.getTable() + " SET point = point - " + dendaPointInput.getText() + " WHERE id = " + this.popupBorrowingData.getMember_id();
                    ps = conn.prepareStatement(query);
                    ps.execute();

                    conn.commit();
                    resetAll();
                    this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Pengembalian berhasil");
                } catch (Exception e) {
                    conn.rollback();
                    throw new Exception(e.getMessage());
                }
            } catch (Exception e) {
                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", e.getMessage());
            }
        }
    }

    public void searchBorrowingHandle(ActionEvent act) {
        try {
            Stage stg = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Popup/PopupBorrowing.fxml"));
            Parent root = loader.load();
            Scene pupupBook = new Scene(root);
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setScene(pupupBook);
            PopupBorrowingController popupBorrowingController = loader.getController();
            stg.setTitle("Peminjam");
            stg.setResizable(false);
            stg.showAndWait();

            this.popupBorrowingData = popupBorrowingController.getSelectedData();
            Timestamp returnDate = Timestamp.valueOf(this.popupBorrowingData.getReturn_date());
            resetForm();
            if (System.currentTimeMillis() > returnDate.getTime()) {
                long diff = System.currentTimeMillis() - returnDate.getTime();
                long days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);

                long dendaPoint = 2 * days;
                dendaPointInput.setText(String.valueOf(dendaPoint));
            }
            setBorrowingForm();
            loadBookData();
        } catch (Exception e) {
        }
    }

    private void resetForm() {
        idInput.setText("");
        nameInput.setText("");
        dendaTunaiInput.setText("0");
        dendaPointInput.setText("0");
    }

    private void resetAll() {
        resetForm();
        this.popupBorrowingData = null;
        this.reversionDetailList.clear();
    }

    private void setBorrowingForm() {
        idInput.setText(String.valueOf(this.popupBorrowingData.getId()));
        nameInput.setText(this.popupBorrowingData.getMember_name());
    }

    @FXML
    private TableView tableBook;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> statusCol;

    ObservableList<ReversionDetail> reversionDetailList = FXCollections.observableArrayList();

    public void setDataTable() {
        try {
            reversionDetailList.clear();
            ResultSet borrowing_details = this.borrowing_details.getDetailByParent(this.popupBorrowingData.getId());

            while (borrowing_details.next()) {
                reversionDetailList.add(new ReversionDetail(
                        borrowing_details.getInt("id"),
                        borrowing_details.getInt("book_id"),
                        borrowing_details.getString("title"),
                        borrowing_details.getInt("price"),
                        statusList.get(0)
                ));
                tableBook.setItems(reversionDetailList);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void loadBookData() {
        setDataTable();

        titleCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
}
