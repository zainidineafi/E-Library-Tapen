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
public class Shelf extends Model {

    protected String table = "shelves";
    protected int id;
    protected String code, name, created_at;

    public Shelf(int id, String code, String name, String created_at) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.created_at = created_at;
    }

    public Shelf() {
        this.setTable(this.table);
    }

    public boolean store(String code, String name) throws SQLException {
        String query = "INSERT INTO " + this.table + " (id, code,  name, created_at, updated_at) VALUES (NULL, '" + code + "', '" + name + "', NOW(), NOW())";
        this.executeQuery(query);
        return true;
    }

    public boolean update(int id, String code, String name) throws SQLException {
        String query = "UPDATE " + this.table + " SET "
                + "code = '" + code + "', "
                + "name = '" + name + "', "
                + "updated_at = NOW() WHERE id = '" + id + "'";
        this.executeQuery(query);
        return true;
    }

    public ResultSet getBookCount() throws SQLException {
        Model book = new Book();
        String query = "SELECT DISTINCT " + this.table + ".name, concat(round(( (SELECT COUNT(*) FROM " + book.getTable() + " WHERE " + book.getTable() + ".shelf_id = " + this.table + ".id)/(SELECT COUNT(*) FROM " + book.getTable() + ") * 100 ),2),'%') AS book_count FROM " + this.table;
        ResultSet response = this.getQuery(query);
        
        return response;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
