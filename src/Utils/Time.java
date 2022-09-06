/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Illuminate
 */
public class Time {
    public long getEpoch(String time) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:mm:ss.SSS zzz");
        Date date = df.parse(time);
        long epoch = date.getTime();
        
        return epoch;
    }
}
