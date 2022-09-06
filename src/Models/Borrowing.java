/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Core.Model;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

/**
 *
 * @author Illuminate
 */
public class Borrowing extends Model {

    protected String table = "borrowings";
    protected int id, member_id, admin_id, total;
    protected String member_name, admin_name, borrow_date, return_date, created_at;

    public Borrowing(int member_id, int admin_id, String member_name, String admin_name, String borrow_date, String return_date, String created_at) {
        this.member_id = member_id;
        this.admin_id = admin_id;
        this.member_name = member_name;
        this.admin_name = admin_name;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
        this.created_at = created_at;
    }

    public Borrowing(int id, String member_name, String admin_name, String borrow_date, String return_date) {
        this.id = id;
        this.member_name = member_name;
        this.admin_name = admin_name;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
    }

    public Borrowing(int id) {
        this.id = id;
    }

    public Borrowing() {
        this.setTable(this.table);
    }

    // Get Borrowing History
    @Override
    public ResultSet getAll() throws SQLException {
        Model admin = new Admin();
        Model member = new Member();
        String query = "SELECT " + this.table + ".*, " + member.getTable() + ".name AS member_name, " + admin.getTable() + ".name AS admin_name FROM " + this.table + " JOIN " + admin.getTable() + " ON " + admin.getTable() + ".id = " + this.table + ".admin_id JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id ORDER BY created_at DESC";
        ResultSet response = this.getQuery(query);

        return response;
    }

    public ResultSet getAll(LocalDate start_date, LocalDate end_date) throws SQLException {
        Model admin = new Admin();
        Model member = new Member();
        String query = "SELECT " + this.table + ".*, " + member.getTable() + ".name AS member_name, " + admin.getTable() + ".name AS admin_name FROM " + this.table + " JOIN " + admin.getTable() + " ON " + admin.getTable() + ".id = " + this.table + ".admin_id JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id WHERE borrow_date BETWEEN DATE_FORMAT('" + start_date + "', '%Y-%m-%d 00:00:00') AND DATE_FORMAT('" + end_date + "', '%Y-%m-%d 23:59:59') ORDER BY created_at DESC";
        ResultSet response = this.getQuery(query);

        return response;
    }

    public ResultSet getWasReturn(LocalDate start_date, LocalDate end_date) throws SQLException {
        Model admin = new Admin();
        Model member = new Member();
        Model reversion = new Reversion();
        String query = "SELECT " + this.table + ".*, " + member.getTable() + ".name AS member_name, " + admin.getTable() + ".name AS admin_name FROM " + this.table + " JOIN " + admin.getTable() + " ON " + admin.getTable() + ".id = " + this.table + ".admin_id JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id WHERE EXISTS(SELECT null FROM " + reversion.getTable() + " WHERE " + reversion.getTable() + ".borrowing_id = " + this.table + ".id) AND borrow_date BETWEEN DATE_FORMAT('" + start_date + "', '%Y-%m-%d 00:00:00') AND DATE_FORMAT('" + end_date + "', '%Y-%m-%d 23:59:59') ORDER BY created_at DESC";
        ResultSet response = this.getQuery(query);

        return response;
    }

    public ResultSet getNotReturn(LocalDate start_date, LocalDate end_date) throws SQLException {
        Model admin = new Admin();
        Model member = new Member();
        Model reversion = new Reversion();
        String query = "SELECT " + this.table + ".*, " + member.getTable() + ".name AS member_name, " + admin.getTable() + ".name AS admin_name FROM " + this.table + " JOIN " + admin.getTable() + " ON " + admin.getTable() + ".id = " + this.table + ".admin_id JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id WHERE NOT EXISTS(SELECT null FROM " + reversion.getTable() + " WHERE " + reversion.getTable() + ".borrowing_id = " + this.table + ".id) AND borrow_date BETWEEN DATE_FORMAT('" + start_date + "', '%Y-%m-%d 00:00:00') AND DATE_FORMAT('" + end_date + "', '%Y-%m-%d 23:59:59') ORDER BY created_at DESC";
        ResultSet response = this.getQuery(query);

        return response;
    }

    // END
    public Borrowing(int id, int member_id, int total, String member_name, String return_date) {
        this.id = id;
        this.total = total;
        this.member_id = member_id;
        this.member_name = member_name;
        this.return_date = return_date;
    }

    public ResultSet getNotReturned() throws SQLException {
        Model member = new Member();
        Model borrowing_detail = new BorrowingDetail();
        Model reversion = new Reversion();

        String query = "SELECT " + this.table + ".id, " + this.table + ".return_date, " + member.getTable() + ".id as member_id, " + member.getTable() + ".name as name, (SELECT COUNT(*) FROM " + borrowing_detail.getTable() + " WHERE " + borrowing_detail.getTable() + ".borrowing_id = " + this.table + ".id) as total FROM " + this.table + " JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id WHERE NOT EXISTS (SELECT null FROM " + reversion.getTable() + " WHERE " + reversion.getTable() + ".borrowing_id = " + this.table + ".id)";
        ResultSet response = this.getQuery(query);

        return response;
    }

    public ResultSet getNotReturned(int search) throws SQLException {
        Model member = new Member();
        Model borrowing_detail = new BorrowingDetail();
        Model reversion = new Reversion();

        String query = "SELECT " + this.table + ".id, " + this.table + ".return_date, " + member.getTable() + ".id as member_id, " + member.getTable() + ".name as name, (SELECT COUNT(*) FROM " + borrowing_detail.getTable() + " WHERE " + borrowing_detail.getTable() + ".borrowing_id = " + this.table + ".id) as total FROM " + this.table + " JOIN " + member.getTable() + " ON " + member.getTable() + ".id = " + this.table + ".member_id WHERE NOT EXISTS (SELECT null FROM " + reversion.getTable() + " WHERE " + reversion.getTable() + ".borrowing_id = " + this.table + ".id) AND " + this.table + ".id = " + search;
        ResultSet response = this.getQuery(query);

        return response;
    }

    public int getNotReturnedMember(int member_id) throws SQLException {
        int result = 0;
        Reversion reversion = new Reversion();
        String query = "SELECT COUNT(*) FROM " + this.table + " WHERE NOT EXISTS (SELECT null FROM " + reversion.getTable() + " WHERE " + reversion.getTable() + ".borrowing_id = " + this.table + ".id) AND member_id = " + member_id;
        ResultSet response = this.getQuery(query);
        if (response.next()) {
            if (response.getInt(1) > 0) {
                result = 1;
            }
        }
        return result;
    }

    public int getPerMonth(int month) throws SQLException {
        int result = 0;
        String query = "SELECT COUNT(*) AS total_trx FROM " + this.table + " WHERE MONTH(borrow_date) = " + month + " AND YEAR(borrow_date) = " + new SimpleDateFormat("Y").format(new Date());
        ResultSet res = this.getQuery(query);
        if (res.next()) {
            result = res.getInt("total_trx");
            if (res.wasNull()) {
                result = 0;
            }
        }
        return result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getAdmin_name() {
        return admin_name;
    }

    public String getBorrow_date() {
        return borrow_date;
    }

    public String getReturn_date() {
        return return_date;
    }

    public int getMember_id() {
        return member_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
    }

    public void setBorrow_date(String borrow_date) {
        this.borrow_date = borrow_date;
    }

    public void setReturn_date(String return_date) {
        this.return_date = return_date;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
