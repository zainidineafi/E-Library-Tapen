/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Config.Storage;
import Core.Controller;
import Models.Auth;
import Models.Book;
import Models.Author;
import Models.Publisher;
import Models.Shelf;
import Utils.InputNumber;
import java.io.File;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author Illuminate
 */
public class BookController extends Controller implements Initializable {
    private Book book = null;
    private Book selectionData = null;
    private final ObservableList<Author> authorList = FXCollections.observableArrayList();
    private final ObservableList<Publisher> publisherList = FXCollections.observableArrayList();
    private final ObservableList<Shelf> shelvesList = FXCollections.observableArrayList();
    
    public BookController() {
        book = new Book();
    }
    
    @FXML
    private Label nameBox;
    
    @FXML
    private AnchorPane formPage;
    
    @FXML
    private TableView tableBuku;
    @FXML
    private TableColumn <Book, Integer> idCol;
    @FXML
    private TableColumn <Book, String> isbnCol;
    @FXML
    private TableColumn <Book, String> titleCol;
    @FXML
    private TableColumn <Book, Integer> stockCol;
    @FXML
    private TableColumn <Book, Integer> priceCol;
    @FXML
    private TableColumn <Book, Integer> publishYearCol;
    
    ObservableList<Book> BookList = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameBox.setText(Auth.getName().toUpperCase());
        formPage.setVisible(false);
        this.initList();
        loadData();
        new InputNumber().getInputNumber(publishYearInput);
        new InputNumber().getInputNumber(priceInput);
    }
    
    private void initList() {
        try { 
            ResultSet authors, publishers, shelves;
            // Author
            authorInput.setConverter(new StringConverter<Author>() {
                @Override
                public String toString(Author object) {
                    return object.getName();
                }

                @Override
                public Author fromString(String string) {
                    return authorInput.getItems().stream().filter(ap -> 
                        ap.getName().equals(string)).findFirst().orElse(null);
                }
            });
            
            authors = new Author().getAll();
            
            while(authors.next()) {
                authorList.add(new Author(
                        authors.getInt("id"),
                        authors.getString("name"),
                        authors.getString("phone"),
                        authors.getString("address"),
                        authors.getTimestamp("created_at").toString()
                ));
                authorInput.setItems(authorList);
            }
            
            // Publisher
            publisherInput.setConverter(new StringConverter<Publisher>() {
                @Override
                public String toString(Publisher object) {
                    return object.getName();
                }

                @Override
                public Publisher fromString(String string) {
                    return publisherInput.getItems().stream().filter(ap -> 
                        ap.getName().equals(string)).findFirst().orElse(null);
                }
            });
            
            publishers = new Publisher().getAll();
            
            while(publishers.next()) {
                publisherList.add(new Publisher(
                        publishers.getInt("id"),
                        publishers.getString("name"),
                        publishers.getString("phone"),
                        publishers.getString("address"),
                        publishers.getString("created_at")
                ));
                
                publisherInput.setItems(publisherList);
            }
            
            // Shelves
            shelfInput.setConverter(new StringConverter<Shelf>() {
                @Override
                public String toString(Shelf object) {
                    return object.getName();
                }

                @Override
                public Shelf fromString(String string) {
                    return shelfInput.getItems().stream().filter(ap -> 
                        ap.getName().equals(string)).findFirst().orElse(null);
                }
            });
            
            shelves = new Shelf().getAll();
            
            while(shelves.next()) {
                shelvesList.add(new Shelf(
                        shelves.getInt("id"),
                        shelves.getString("code"),
                        shelves.getString("name"),
                        shelves.getString("created_at")
                ));
                
                shelfInput.setItems(shelvesList);
            }
        } catch(Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }
    
    @FXML
    private Button btnTambah;
    
    public void btnTambahHandle(ActionEvent evt) {
        this.btnSubmit.setText("Tambah");
        this.resetForm();
        formPage.setVisible(true);
    }
    
    @FXML
    private Button btnEdit;
    
    public void btnEditHandle(ActionEvent evt) {
        if(tableBuku.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih petugas terlebih dahulu");
        } else {
            this.selectionData = (Book) tableBuku.getSelectionModel().getSelectedItem();
            this.setForm(this.selectionData);
            this.btnSubmit.setText("Simpan");
            formPage.setVisible(true);        
        }
    }
    
    @FXML
    private Button btnHapus;
    
    public void btnHapusHandle(ActionEvent evt) throws SQLException {
        if(tableBuku.getSelectionModel().getSelectedItem() == null) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", "Silahkan pilih petugas terlebih dahulu");
        } else {
            if (showConfirm("Anda yakin akan menghpus data ini?", "Data yang anda hapus tidak akan bisa dikembalikan").get() == ButtonType.OK) {
                this.selectionData = (Book) tableBuku.getSelectionModel().getSelectedItem();
                new Storage().delete(this.selectionData.getImage());
                this.book.delete(this.selectionData.getId());
                loadData();
            }
        }
    }
    
    @FXML
    private TextField idInput;
    @FXML
    private TextField isbnInput;
    @FXML
    private TextField publishYearInput;
    @FXML
    private TextField titleInput;
    @FXML
    private TextField priceInput;
    @FXML
    private ComboBox<Author> authorInput;
    @FXML
    private ComboBox<Publisher> publisherInput;
    @FXML
    private ComboBox<Shelf> shelfInput;
    @FXML
    private ImageView previewPhoto;
    @FXML
    private TextField photoInput;
    @FXML
    private TextArea descInput;
    
    
    private void setForm(Book book) {
        idInput.setText(String.valueOf(book.getId()));
        isbnInput.setText(book.getIsbn());
        publishYearInput.setText(String.valueOf(book.getPublish_year()));
        titleInput.setText(book.getTitle());
        priceInput.setText(String.valueOf(book.getPrice()));
        
        authorList.filtered(author -> {
            if(author.getId() == book.getAuthor_id()) {
                authorInput.getSelectionModel().select(author);
            }
            return true;
        });
        publisherList.filtered(publisher -> {
            if(publisher.getId() == book.getPublisher_id()) {
                publisherInput.getSelectionModel().select(publisher);
            }
            return true;
        });
        shelvesList.filtered(shelf -> {
            if(shelf.getId() == book.getShelf_id()) {
                shelfInput.getSelectionModel().select(shelf);
            }
            return true;
        });
        
        descInput.setText(book.getDescription());
        Image image = null;
        if(book.getImage() == null || book.getImage().equals("")) {
            image = new Image(getClass().getResource("../Public/Images/book/example.png").toString());
        } else {
            image = new Image(new File(book.getImage()).toURI().toString(), previewPhoto.getFitWidth(), previewPhoto.getFitHeight(), false, false);
        }
        previewPhoto.setImage(image);
        photoInput.setText(null);
    }
    
    private void resetForm() {
        idInput.setText(null);
        isbnInput.setText(null);
        publishYearInput.setText(null);
        titleInput.setText(null);
        priceInput.setText(null);
        authorInput.setValue(null);
        publisherInput.setValue(null);
        shelfInput.setValue(null);
        descInput.setText(null);
        previewPhoto.setImage(new Image(getClass().getResource("../Public/Images/book/example.png").toString()));
        photoInput.setText(null);
    }
    
    @FXML
    private Button btnKembaliFormPage;
    
    public void btnKembaliFormPageHandle(ActionEvent act) {
        this.resetForm();
        formPage.setVisible(false);
    }
    
    private void inputValidation() throws Exception {
        if((isbnInput.getText() == null || isbnInput.getText().equals(""))
                || (titleInput.getText() == null || titleInput.getText().equals(""))
                || (priceInput.getText() == null || priceInput.getText().equals(""))
                || (publisherInput.getValue() == null)
                || (authorInput.getValue() == null)
                || (shelfInput.getValue() == null)
                || (descInput.getText() == null || descInput.getText().equals(""))) {
            throw new Exception("Data wajib diisi");
        }
    }
    
    @FXML
    private Button btnSubmit;
    
    public void btnSubmitHandle(ActionEvent act) {
        try {
            this.inputValidation();
            String path = "src/Public/Images/book", 
                   fileName = photoInput.getText(),
                   isbn = isbnInput.getText(),
                   title = titleInput.getText(),
                   desc = descInput.getText();
            int price = Integer.valueOf(priceInput.getText()),
                publish_year = Integer.valueOf(publishYearInput.getText());
            Author author = authorInput.getValue();
            Publisher publisher = publisherInput.getValue();
            Shelf shelf = shelfInput.getValue();
            
            if(btnSubmit.getText().toLowerCase().equals("tambah")) {
                String filePath = "";
                if(fileName != null) {
                    filePath = new Storage().upload(path, fileName, isbn);
                }
                
                this.book.store(isbn, title, price, filePath, desc, publish_year, author.getId(), publisher.getId(), shelf.getId());
                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data buku berhasil ditambahkan");
            } else {
                int id = this.selectionData.getId();
                String filePath = "";
                if(fileName != null) {
                    if(new Storage().delete(this.selectionData.getImage())) {
                        filePath = new Storage().upload(path, fileName, isbn);
                    }
                }
                
                this.book.update(id, isbn, title, price, filePath, desc, publish_year, author.getId(), publisher.getId(), shelf.getId());
                this.showAlert(Alert.AlertType.INFORMATION, "SUKSES", "", "Data buku berhasil diubah");
            }
            
            this.loadData();
            formPage.setVisible(false);
        } catch(Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }
    
    @FXML
    private Button btnChoosePhoto;
    
    public void btnChoosePhotoHandle(ActionEvent act) {
        File selectedFile = new Storage().open("image");
        if(selectedFile != null) {
            Image image =  new Image(selectedFile.toURI().toString(), previewPhoto.getFitWidth(), previewPhoto.getFitHeight(), false, false);
            previewPhoto.setImage(image);
            photoInput.setText(selectedFile.getAbsolutePath());
        }
    }
    
    
    
    
    // Set Datatable
    public void setDataTable() {
        try {
            BookList.clear();
            ResultSet books = this.book.getAll();
            
            while(books.next()) {
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
        } catch(Exception e) {
            this.showAlert(Alert.AlertType.ERROR, "Ups...", "", e.getMessage());
        }
    }
    
    public void loadData() {
        this.setDataTable();
        
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        stockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        publishYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
    }
}
