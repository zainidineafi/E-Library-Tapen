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
import Models.Stock;
import Utils.InputNumber;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class StockController extends Controller implements Initializable {

    private Stock stock = null;
    private Stock selectionData = null;
    private final ObservableList<String> typeList = FXCollections.observableArrayList("Masuk", "Keluar");
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    public StockController() {
        stock = new Stock();
    }

    @FXML
    private Label nameBox;

    @FXML
    private AnchorPane formPage;

    @FXML
    private TableView tableStock;
    @FXML
    private TableColumn<Stock, Integer> idCol;
    @FXML
    private TableColumn<Stock, String> bookCol;
    @FXML
    private TableColumn<Stock, String> typeCol;
    @FXML
    private TableColumn<Stock, String> amountCol;
    @FXML
    private TableColumn<Stock, Integer> createdAtCol;

    ObservableList<Stock> StockList = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        formPage.setVisible(false);
        this.initList();
        loadData();
        new InputNumber().getInputNumber(amountInput);
    }

    private void initList() {
        try {
            typeInput.setItems(typeList);
            ResultSet books;

            bookInput.setConverter(new StringConverter<Book>() {
                @Override
                public String toString(Book object) {
                    return object.getTitle();
                }

                @Override
                public Book fromString(String string) {
                    return bookInput.getItems().stream().filter(ap
                            -> ap.getTitle().equals(string)).findFirst().orElse(null);
                }
            });

            books = new Book().getAll();

            while (books.next()) {
                bookList.add(new Book(
                        books.getInt("id"),
                        books.getInt("stock"),
                        books.getInt("price"),
                        books.getInt("publish_year"),
                        books.getInt("author_id"),
                        books.getInt("publisher_id"),
                        books.getInt("shelf_id"),
                        books.getString("isbn"),
                        books.getString("title"),
                        books.getString("name"),
                        books.getString("description"),
                        books.getString("image")
                ));

                bookInput.setItems(bookList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    @FXML
    private Button btnTambah;

    public void btnTambahHandle(ActionEvent act) {
        resetForm();
        formPage.setVisible(true);
    }

    @FXML
    private Button btnDetail;

    public void btnDetailHandle(ActionEvent act) {
        if (tableStock.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih stock terlebih dahulu");
        } else {
            this.selectionData = (Stock) tableStock.getSelectionModel().getSelectedItem();
            setForm(this.selectionData);
            setDetailOff();
            formPage.setVisible(true);
        }
    }

    public void setDetailOff() {
        amountInput.setEditable(false);
        detailInput.setEditable(false);
        btnSubmit.setVisible(false);
    }

    public void setDetailOn() {
        amountInput.setEditable(true);
        detailInput.setEditable(true);
        btnSubmit.setVisible(true);
    }

    @FXML
    private Button btnHapus;

    public void btnHapusHandle(ActionEvent act) {
        if (tableStock.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih stock terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghpus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Stock) tableStock.getSelectionModel().getSelectedItem();

                Book book = new Book();
                try {
                    ResultSet resultBook = book.getById(selectionData.getBook_id());
                    if (resultBook.next()) {
                        int bookStock = resultBook.getInt("stock");
                        if (bookStock < selectionData.getAmount()) {
                            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Stok melebihi batas");
                        } else {
                            String query;
                            PreparedStatement ps;

                            // Query
                            Connection conn = Database.GetConnection();
                            try {
                                try {
                                    conn.setAutoCommit(false);
                                    if (selectionData.getType().equals("Masuk")) {
                                        query = "UPDATE " + book.getTable() + " SET stock = stock - " + this.selectionData.getAmount() + ", updated_at = NOW() WHERE id =" + resultBook.getInt("id");
                                    } else {
                                        query = "UPDATE " + book.getTable() + " SET stock = stock + " + this.selectionData.getAmount() + ", updated_at = NOW() WHERE id = " + resultBook.getInt("id");
                                    }
                                    ps = conn.prepareStatement(query);
                                    ps.execute();

                                    this.stock.delete(selectionData.getId());

                                    conn.commit();
                                    this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Stok berhasil dihapus");
                                    loadData();
                                } catch (Exception e) {
                                    conn.rollback();
                                    System.out.println(e.getMessage());
                                }
                            } catch (Exception e) {
                                this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Terjadi kesalahan koneksi");
                            }
                        }
                    }
                } catch (Exception e) {
                    this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Terjadi kesalahan : " + e.getMessage());
                }
            }
        }
    }

    @FXML
    private Button btnKembaliFormPage;

    public void btnKembaliFormPageHandle(ActionEvent act) {
        setDetailOn();
        resetForm();
        formPage.setVisible(false);
    }

    @FXML
    private TextField idInput;
    @FXML
    private ComboBox<Book> bookInput;
    @FXML
    private ComboBox typeInput;
    @FXML
    private TextField amountInput;
    @FXML
    private TextArea detailInput;

    public void setForm(Stock stock) {
        idInput.setText(String.valueOf(stock.getId()));
        bookList.filtered(book -> {
            if (book.getId() == stock.getBook_id()) {
                bookInput.getSelectionModel().select(book);
            }
            return true;
        });
        typeInput.setValue(stock.getType());
        amountInput.setText(String.valueOf(stock.getAmount()));
        detailInput.setText(stock.getDetail());
    }

    public void resetForm() {
        idInput.setText(null);
        bookInput.setValue(null);
        typeInput.setValue(null);
        amountInput.setText(null);
        detailInput.setText(null);
    }

    private void inputValidation() throws Exception {
        if ((bookInput.getValue() == null || bookInput.getValue().equals("Pilih"))
                || (typeInput.getValue() == null || typeInput.getValue().equals("Pilih"))
                || (amountInput.getText() == null || amountInput.getText().equals(""))
                || (detailInput.getText() == null || detailInput.getText().equals(""))) {
            throw new Exception("Data wajib diisi");
        }
    }

    @FXML
    private Button btnSubmit;

    public void btnSubmitHandle(ActionEvent act) {
        try {
            this.inputValidation();
            String type = (String) typeInput.getValue(),
                    detail = detailInput.getText();
            int amount = Integer.valueOf(amountInput.getText());
            Book book = bookInput.getValue();

            if (book.getStock() < 1 && type.equals("Keluar")) {
                throw new Exception("Stok buku masih kosong");
            } else {
                Connection conn = Database.GetConnection();
                PreparedStatement ps;
                String query;
                try {
                    conn.setAutoCommit(false);
                    query = "INSERT INTO " + stock.getTable() + " (id, book_id, type, amount, detail, created_at, updated_at) VALUES (NULL, '" + book.getId() + "', '" + type + "', '" + amount + "', '" + detail + "', NOW(), NOW())";
                    ps = conn.prepareStatement(query);
                    ps.execute();

                    if (type.equals("Masuk")) {
                        query = "UPDATE " + new Book().getTable() + " SET stock = stock + " + amount + ", updated_at = NOW() WHERE id = " + book.getId();
                    } else {
                        query = "UPDATE " + new Book().getTable() + " SET stock = stock - " + amount + ", updated_at = NOW() WHERE id = " + book.getId();

                    }
                    ps = conn.prepareStatement(query);
                    ps.execute();

                    conn.commit();
                    this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Stok berhasil ditambahkan");
                    loadData();
                    formPage.setVisible(false);
                } catch (Exception e) {
                    conn.rollback();

                    throw new Exception(e.getMessage());
                }
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    // Set Datatable
    public void setDataTable() {
        try {
            StockList.clear();
            ResultSet stocks = this.stock.getStocks();

            while (stocks.next()) {
                StockList.add(new Stock(
                        stocks.getInt("id"),
                        stocks.getInt("book_id"),
                        stocks.getInt("amount"),
                        stocks.getString("title"),
                        stocks.getString("type"),
                        stocks.getString("detail"),
                        stocks.getTimestamp("created_at").toString()
                ));
                tableStock.setItems(StockList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        this.setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        createdAtCol.setCellValueFactory(new PropertyValueFactory<>("created_at"));
    }
}
