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
public class BorrowingDetail extends Model {
    protected String table = "borrowing_details";

    public BorrowingDetail() {
        this.setTable(this.table);
    }
    
    public ResultSet getByParent(int borrowing_id) throws SQLException {
        String query = "SELECT * FROM " + this.table + " WHERE borrowing_id = '" + borrowing_id + "'";
        ResultSet response = this.getQuery(query);

        return response;
    }
    
    public ResultSet getDetailByParent(int borrowing_id) throws SQLException {
        Model book = new Book();
        String query = "SELECT " + this.table + ".*, " + book.getTable() + ".id as book_id, " + book.getTable() + ".title, " + book.getTable() + ".price FROM " + this.table + " JOIN " + book.getTable() + " ON " + book.getTable() + ".id = " + this.table + ".book_id WHERE borrowing_id = '" + borrowing_id + "'";
        ResultSet response = this.getQuery(query);

        return response;
    }
}
