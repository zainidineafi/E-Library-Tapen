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
import Models.Member;
import Models.Borrowing;
import Models.BorrowingDetail;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class BorrowingController extends Controller implements Initializable {

    private Borrowing borrowing;
    private BorrowingDetail borrowing_detail;
    private Book book;
    private Book popupBookData;
    private Member popupMemberData;

    public BorrowingController() {
        borrowing = new Borrowing();
        borrowing_detail = new BorrowingDetail();
        book = new Book();
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

    @FXML
    private TableView tableCart;
    @FXML
    private TableColumn<Book, Integer> idCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;

    ObservableList<Book> bookList = FXCollections.observableArrayList();
    ArrayList<Integer> idBookData = new ArrayList<>();

    public void searchBookHandle(ActionEvent act) {
        if (this.popupMemberData == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Harap pilih anggota terlebih dahulu");
        } else {
            try {
                Stage stg = new Stage();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Popup/PopupBook.fxml"));
                Parent root = loader.load();
                Scene pupupBook = new Scene(root);
                stg.initModality(Modality.APPLICATION_MODAL);
                stg.setScene(pupupBook);
                PopupBookController popupBookController = loader.getController();
                stg.setTitle("Buku");
                stg.setResizable(false);
                stg.showAndWait();

                this.popupBookData = popupBookController.getSelectedData();
                setBookForm();
            } catch (Exception e) {
            }
        }
    }

    public void bookRelease(KeyEvent kev) {
        if (this.popupMemberData == null) {
            resetBookForm();
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Harap pilih anggota terlebih dahulu");
        } else {
            try {
                String isbn = idBookInput.getText();
                if (!isbn.equals("")) {
                    ResultSet book = this.book.getByISBN(isbn);

                    while (book.next()) {
                        titleBookInput.setText(book.getString("title"));
                        this.popupBookData = new Book(book.getInt("id"),
                                book.getInt("stock"),
                                book.getInt("price"),
                                book.getInt("publish_year"),
                                book.getInt("author_id"),
                                book.getInt("publisher_id"),
                                book.getInt("shelf_id"),
                                book.getString("isbn"),
                                book.getString("title"),
                                book.getString("name"),
                                book.getString("description"),
                                book.getString("image")
                        );
                    }
                } else {
                    resetBookForm();
                }
            } catch (Exception e) {
                this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
            }
        }
    }

    public void searchMemberHandle(ActionEvent act) {
        try {
            Stage stg = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../View/Popup/PopupMember.fxml"));
            Parent root = loader.load();
            Scene pupupBook = new Scene(root);
            stg.initModality(Modality.APPLICATION_MODAL);
            stg.setScene(pupupBook);
            PopupMemberController popupMemberController = loader.getController();
            stg.setTitle("Anggota");
            stg.setResizable(false);
            stg.showAndWait();

            this.popupMemberData = popupMemberController.getSelectedData();

            int result = this.borrowing.getNotReturnedMember(this.popupMemberData.getId());
            if (result < 1) {
                setMemberForm();
                this.idBookData.clear();
                this.bookList.clear();
                resetBookForm();
            } else {
                this.popupMemberData = null;
                this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Kamu masih belum mengembalikan buku sebelumnya!");
            }
        } catch (Exception e) {
        }
    }

    public void btnTambahHandle(ActionEvent act) {
        loadData();
        resetBookForm();
    }

    public void btnResetHandle(ActionEvent act) {
        resetAll();
    }

    public void btnSubmitHandle(ActionEvent act) {
        if (this.popupMemberData == null || bookList.size() == 0) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Keranjang dan member tidak boleh kosong!");
        } else {
            if (showConfirm("", "Anda yakin transaksi ini akan diproses?").get() == ButtonType.OK) {
                try {
                    int idMember = Integer.valueOf(idMemberInput.getText());
                    int idAdmin = Auth.getId();
                    int return_date = 7;
                    Connection conn = Database.GetConnection();
                    PreparedStatement ps;
                    String query;
                    int transactionKey = 0;
                    try {
                        conn.setAutoCommit(false);
                        query = "INSERT INTO " + this.borrowing.getTable() + " (id, borrow_date, return_date, member_id, admin_id, created_at, updated_at) VALUES (NULL, NOW(), DATE_ADD(NOW(), INTERVAL " + return_date + " DAY), '" + idMember + "', '" + idAdmin + "', NOW(), NOW())";
                        ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                        ps.execute();
                        ResultSet res = ps.getGeneratedKeys();

                        if (res.next()) {
                            transactionKey = res.getInt(1);
                            query = "INSERT INTO " + this.borrowing_detail.getTable() + " (id, borrowing_id, book_id, created_at, updated_at) VALUES ";
                            for (int i = 0; i < bookList.size(); i++) {
                                if (i != 0) {
                                    query += ", ";
                                }
                                query += "(NULL, '" + transactionKey + "', '" + bookList.get(i).getId() + "', NOW(), NOW())";
                            }
                            ps = conn.prepareStatement(query);
                            ps.execute();

                            for (int i = 0; i < bookList.size(); i++) {
                                query = "UPDATE " + this.book.getTable() + " SET stock = IF(stock > 0, stock - 1, 0) WHERE id = " + bookList.get(i).getId();
                                ps = conn.prepareStatement(query);
                                ps.execute();
                            }
                        }

                        conn.commit();
                        resetAll();
                        this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Transaksi berhasil");
                        if (showConfirm("", "Apakah anda akan mencetak struk?").get() == ButtonType.OK) {
                            String report = "src/Report/bills.jasper";
                            HashMap hash = new HashMap();
                            hash.put("transaction", transactionKey);
                            JasperPrint JPrint = JasperFillManager.fillReport(report, hash, conn);
                            JasperViewer.viewReport(JPrint, false);
                        }
                    } catch (Exception e) {
                        conn.rollback();
                        throw new Exception(e.getMessage());
                    }
                } catch (Exception e) {
                    this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
                }
            }
        }
    }

    @FXML
    private TextField idMemberInput;

    @FXML
    private TextField nameMemberInput;

    public void setMemberForm() {
        idMemberInput.setText(String.valueOf(this.popupMemberData.getId()));
        nameMemberInput.setText(this.popupMemberData.getName());
    }

    @FXML
    private TextField idBookInput;

    @FXML
    private TextField titleBookInput;

    public void setBookForm() {
        idBookInput.setText(String.valueOf(this.popupBookData.getIsbn()));
        titleBookInput.setText(this.popupBookData.getTitle());
    }

    public void resetAll() {
        resetBookForm();
        resetMemberForm();
        this.popupBookData = null;
        this.popupMemberData = null;
        this.bookList.clear();
        this.idBookData.clear();
    }

    public void resetMemberForm() {
        idMemberInput.setText(null);
        nameMemberInput.setText(null);
    }

    public void resetBookForm() {
        idBookInput.setText(null);
        titleBookInput.setText(null);
        this.popupBookData = null;
    }

    public void setDataTable() {
        if (this.popupBookData == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Harap pilih buku terlebih dahulu");
        } else {
            if (this.popupMemberData.getPoint() <= 25 && this.bookList.size() < 1) {
                addToCart();
            } else if (this.popupMemberData.getPoint() > 25 && this.popupMemberData.getPoint() <= 50 && this.bookList.size() < 2) {
                addToCart();
            } else if (this.popupMemberData.getPoint() > 50 && this.popupMemberData.getPoint() <= 65 && this.bookList.size() < 3) {
                addToCart();
            } else if (this.popupMemberData.getPoint() > 65 && this.popupMemberData.getPoint() <= 80 && this.bookList.size() < 4) {
                addToCart();
            } else if (this.popupMemberData.getPoint() > 80 && this.popupMemberData.getPoint() <= 100 && this.bookList.size() < 5) {
                addToCart();
            } else {
                this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Poinmu tidak mencukupi");
            }
        }

    }

    public void addToCart() {
        boolean hasBookId = idBookData.contains(this.popupBookData.getId());
        if (this.popupBookData.getStock() > 0) {
            if (hasBookId == false) {
                idBookData.add(this.popupBookData.getId());
                bookList.add(this.popupBookData);
                tableCart.setItems(bookList);
            } else {
                this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Buku ini sudah ada dikeranjang");
            }
        } else {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Buku ini sedang kosong");
        }
    }

    public void loadData() {
        setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

}
