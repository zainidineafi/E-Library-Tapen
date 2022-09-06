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
public class Stock extends Model {
    protected String table = "stocks";
    protected int id, book_id, amount;
    protected String title, type, detail, created_at;

    public Stock(int id, int book_id, int amount, String title, String type, String detail, String created_at) {
        this.id = id;
        this.book_id = book_id;
        this.amount = amount;
        this.title = title;
        this.type = type;
        this.detail = detail;
        this.created_at = created_at;
    }
    
    public Stock() {
        this.setTable(this.table);
    }
    
    public ResultSet getStocks() throws SQLException
    {
        Book book = new Book();
        String query = "SELECT " + this.table + ".*, " + book.getTable() +  ".title FROM " + this.table + " JOIN " + book.getTable() + " ON " + book.getTable() + ".id = " + this.table + ".book_id ORDER BY created_at DESC";
        ResultSet response = this.getQuery(query);
            
        return response;
    }

    public int getId() {
        return id;
    }

    public int getBook_id() {
        return book_id;
    }
    
    public int getAmount() {
        return amount;
    }

    public String getTitle() {
        return title;
    }
    
    public String getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
