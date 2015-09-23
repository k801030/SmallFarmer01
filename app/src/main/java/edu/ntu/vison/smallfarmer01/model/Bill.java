package edu.ntu.vison.smallfarmer01.model;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Vison on 2015/9/22.
 */
public class Bill {
    String id;
    String begin_at; // date format
    String end_at; // date format

    public String getId() {
        return id;
    }

    public String getBeginAt() {
        return parseDate(begin_at);
    }

    public String getEndAt() {
        return parseDate(end_at);
    }

    private String parseDate(String oldDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(oldDate);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);

            int Y = calendar.get(Calendar.YEAR);
            int M = calendar.get(Calendar.MONTH) + 1;
            int D = calendar.get(Calendar.DATE);

            DecimalFormat mFormat= new DecimalFormat("00");

            return Y+"-"+ mFormat.format(M) + "-"+ mFormat.format(D);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }
}
