/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Core.Model;
import java.sql.SQLException;

/**
 *
 * @author Illuminate
 */
public class Admin extends Model {
    protected String table = "admins";
    protected int id;
    protected String name, username, phone, gender, address, created_at;

    public Admin(int id, String name, String username, String phone, String gender, String address, String created_at) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.created_at = created_at;
    }
    
    public Admin() {
        this.setTable(this.table);
    }
    
    public boolean store(String name, String username, String password, String phone, String gender, String address) throws SQLException {
        String query = "INSERT INTO " + this.table + " (id, name, username, password, phone, gender, address, created_at, updated_at) VALUES (NULL, '" + name + "', '" + username + "', '" + password + "', '" + phone + "', '" + gender + "', '" + address + "', NOW(), NOW())";
        this.executeQuery(query);
        return true;
    }
    
    public boolean update(int id, String name, String username, String password, String phone, String gender, String address) throws SQLException {
        String query = "UPDATE " + this.table + " SET "
                + "name = '" + name + "', "
                + "username = '" + username + "', "
                + "password = '" + password + "', "
                + "phone = '" + phone + "', "
                + "gender = '" + gender + "', "
                + "address = '" + address + "', "
                + "updated_at = NOW() WHERE id = '" + id + "'";
        this.executeQuery(query);
        return true;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
