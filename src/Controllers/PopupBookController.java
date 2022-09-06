/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Models.Book;
import Core.Controller;
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
public class PopupBookController extends Controller implements Initializable {

    private Book book, selectedData;
    ObservableList<Book> BookList = FXCollections.observableArrayList();

    public Book getSelectedData() {
        return this.selectedData;
    }

    public PopupBookController() {
        this.book = new Book();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        loadData();
    }

    @FXML
    private TableView tableBuku;
    @FXML
    private TableColumn<Book, Integer> idCol;
    @FXML
    private TableColumn<Book, String> isbnCol;
    @FXML
    private TableColumn<Book, String> titleCol;
    @FXML
    private TableColumn<Book, String> authorCol;

    public void btnTambahHandle(ActionEvent act) {
        if (tableBuku.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih buku terlebih dahulu");
        } else {
            this.selectedData = (Book) tableBuku.getSelectionModel().getSelectedItem();
            ((Node) (act.getSource())).getScene().getWindow().hide();
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
            BookList.clear();
            ResultSet books;
            if (search.getText().equals("") || search.getText() == null) {
                books = this.book.getAll();
            } else {
                books = this.book.getBySearch(search.getText());
            }

            while (books.next()) {
                BookList.add(new Book(
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
                tableBuku.setItems(BookList);
            }
        } catch (Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }

    public void loadData() {
        this.setDataTable();

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}
