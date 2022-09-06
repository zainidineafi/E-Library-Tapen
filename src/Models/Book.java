/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Core.Model;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Illuminate
 */
public class Book extends Model {
    protected String table = "books";
    
    protected int id, stock, price, publish_year, author_id, publisher_id, shelf_id;
    protected String isbn, title, name, description, image, created_at;

    public Book(int id, int stock, int price, int publish_year, int author_id, int publisher_id, int shelf_id, String isbn, String title, String name, String description, String image) {
        this.id = id;
        this.stock = stock;
        this.price = price;
        this.publish_year = publish_year;
        this.author_id = author_id;
        this.publisher_id = publisher_id;
        this.shelf_id = shelf_id;
        this.isbn = isbn;
        this.title = title;
        this.name = name;
        this.description = description;
        this.image = image;
    }
    
    public Book() {
        this.setTable(this.table);
    }
    
    @Override
    public ResultSet getAll() throws SQLException {
        Author author = new Author();
        String query = "SELECT " + this.table + ".*, " + author.getTable() + ".name FROM " + this.table + " JOIN " + author.getTable() + " ON " + author.getTable() + ".id = " + this.table + ".author_id";
        ResultSet response = this.getQuery(query);
        return response;
    }
    
    @Override
    public ResultSet getBySearch(String search) throws SQLException {
        Author author = new Author();
        String query = "SELECT " + this.table + ".*, " + author.getTable() + ".name FROM " + this.table + " JOIN " + author.getTable() + " ON " + author.getTable() + ".id = " + this.table + ".author_id WHERE CONCAT(" + this.table + "." + this.getTableColumn() + ", name) LIKE '%" + search + "%'";
        ResultSet response = this.getQuery(query);

        return response;
    }
    
    @Override
    public ResultSet getCount() throws SQLException {
        String query = "SELECT SUM(stock) as total_stock, COUNT(*) as total_book FROM " + this.table;
        ResultSet response = this.getQuery(query);

        return response;
    }
    
    public ResultSet getByISBN(String isbn) throws SQLException {
        Author author = new Author();
        String query = "SELECT " + this.table + ".*, " + author.getTable() + ".name FROM " + this.table + " JOIN " + author.getTable() + " ON " + author.getTable() + ".id = " + this.table + ".author_id WHERE books.isbn = '" + isbn + "'";
        ResultSet response = this.getQuery(query);

        return response;
    }
    
    public boolean store(String isbn, String title, int price, String image, String description, int publish_year, int author_id, int publisher_id, int shelf_id) throws SQLException {
        String query = "INSERT INTO " + this.table + " (id, isbn, title, price, image, description, publish_year, author_id, publisher_id, shelf_id, created_at, updated_at) VALUES (NULL, '" + isbn + "', '" + title + "', " + price + ", '" + image + "', '" + description + "', " + publish_year + ", " + author_id + ", " + publisher_id + ", " + shelf_id + ", NOW(), NOW())";
        this.executeQuery(query);
        return true;
    }
    
    public boolean update(int id, String isbn, String title, int price, String image, String description, int publish_year, int author_id, int publisher_id, int shelf_id) throws SQLException {
        String query = "UPDATE " + this.table + " SET "
                + "isbn = '" + isbn + "', "
                + "title = '" + title + "', "
                + "price = " + price + ", "
                + "updated_at = NOW() WHERE id = '" + id + "'";
        this.executeQuery(query);
        return true;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setPublish_year(int publish_year) {
        this.publish_year = publish_year;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    public void setPublisher_id(int publisher_id) {
        this.publisher_id = publisher_id;
    }

    public void setShelf_id(int shelf_id) {
        this.shelf_id = shelf_id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public int getStock() {
        return stock;
    }

    public int getPrice() {
        return price;
    }

    public int getPublish_year() {
        return publish_year;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public int getPublisher_id() {
        return publisher_id;
    }

    public int getShelf_id() {
        return shelf_id;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getCreated_at() {
        return created_at;
    }}

