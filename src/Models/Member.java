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
public class Member extends Model {
    protected String table = "members";
    protected int id, point, total;
    protected String name, phone, gender, address, created_at;
    
    public Member(int id,int point, String name, String phone, String gender, String address, String created_at){
        this.id = id;
        this.point = point;
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.address =  address;
        this.created_at = created_at;
    }

    public Member(String name, int total) {
        this.name = name;
        this.total = total;
    }
    
    public Member(){
       this.setTable(this.table);
    }
    
    public boolean store(String name, String phone, String gender, String address) throws SQLException {
        String query = "INSERT INTO " + this.table + " (id, name, phone, gender, address, created_at, updated_at) VALUES (NULL, '" + name + "', '" + phone + "', '" + gender + "', '" + address + "', NOW(), NOW())";
        this.executeQuery(query);
        return true;
    }
    
    public boolean update(int id, String name, String phone, String gender, String address) throws SQLException {
//        ResultSet currentFullData = this.getById(id);
        String query = "UPDATE " + this.table + " SET "
                + "name = '" + name + "', "
                + "phone = '" + phone + "', "
                + "gender = '" + gender + "', "
                + "address = '" + address + "', "
                + "updated_at = NOW() WHERE id = '" + id + "'";
        this.executeQuery(query);
        return true;
    }
    
    public ResultSet getTenMember() throws SQLException {
        Model borrowing = new Borrowing();
        String query = "SELECT " + this.table + ".name, (SELECT COUNT(*) FROM " + borrowing.getTable() + " WHERE " + borrowing.getTable() + ".member_id = " + this.table + ".id) AS total FROM " + this.table + " ORDER BY total DESC LIMIT 10";
        ResultSet response = this.getQuery(query);
        
        return response;
    }
    
    public int getId(){
        return id;
    }
    
    public int getPoint(){
        return point;
    }
    
    public String getName(){
        return name;
    }
    
    public String getPhone(){
        return phone;
    }
    
    public String getGender(){
        return gender;
    }
    
    public String getAddress(){
        return address;
    }
    
    public String getCreated_at(){
        return created_at;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
    
    public void setId(int id){
        this.id = id;
    }
    
    public void setPoint(int point){
        this.point = point;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public void setGender(String gender){
        this.gender = gender;
    }
    
    public void setAddress(String address){
        this.address = address;
    }
    
    public void setCreated_at(String created_at){
        this.created_at = created_at;
    }
}