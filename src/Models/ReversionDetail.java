 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import Core.Model;

/**
 *
 * @author Illuminate
 */
public class ReversionDetail extends Model {
    protected String table = "reversion_details";
    int book_id, reversion_id, borrowing_detail_id, price;
    String name, status;
    
    public ReversionDetail() {
        this.setTable(this.table);
    }

    public ReversionDetail(int borrowing_detail_id, int book_id, String name, int price, String status) {
        this.borrowing_detail_id = borrowing_detail_id;
        this.book_id = book_id;
        this.name = name;
        this.price = price;
        this.status = status;
    }
    
    public ReversionDetail(int reversion_id, int borrowing_detail_id, String status) {
        this.reversion_id = reversion_id;
        this.borrowing_detail_id = borrowing_detail_id;
        this.status = status;
    }

    public int getReversion_id() {
        return reversion_id;
    }

    public void setReversion_id(int reversion_id) {
        this.reversion_id = reversion_id;
    }

    public int getBorrowing_detail_id() {
        return borrowing_detail_id;
    }

    public void setBorrowing_detail_id(int borrowing_detail_id) {
        this.borrowing_detail_id = borrowing_detail_id;
    }

    public int getPrice() {
        return price;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    
}
