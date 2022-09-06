/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Config;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Illuminate
 */
public class Helper {
    private static boolean isStringLowerCase(String str){
        char[] charArray = str.toCharArray();
        
        for(int i=0; i < charArray.length; i++){
            if( !Character.isLowerCase( charArray[i] ))
                return false;
        }
        
        return true;
    }
    
    public String hashToMD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(plainText.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
              sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {}
        return null;
    }
    
    public String getDate() {
        String pattern = "dd-MM-yyyy";
        String dateInString =new SimpleDateFormat(pattern).format(new Date());
        
        return dateInString;
    }
    
    public String getNumber(String value) {
        String n = "";
        try {
            return String.valueOf(Integer.parseInt(value));
        } catch (Exception e) {
            String[] array = value.split("");
            for (String tab : array) {
                try {
                    System.out.println(tab);
                    n = n.concat(String.valueOf(Integer.parseInt(String.valueOf(tab))));
                } catch (Exception ex) {
                    System.out.println("not nomber");
                }
            }
            return n;
        }
    }
}
