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
public class Auth extends Model {
    protected String table = "admins";
    protected static int id;
    protected static String name, username;
    
    public Auth() {
        this.setTable(this.table);
    }
    
    public static void setId(int id)
    {
        Auth.id = id;
    }
    
    public static void setUsername(String username)
    {
        Auth.username = username;
    }
    
    public static void setName(String name) {
        Auth.name = name;
    }
    
    public static int getId()
    {
        return Auth.id;
    }
    
    public static String getUsername()
    {
        return Auth.username;
    }
    
    public static String getName() {
        return Auth.name;
    }
}
