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
public class Reversion extends Model {
    protected String table = "reversions";
    
    public Reversion() {
        this.setTable(this.table);
    }
}
