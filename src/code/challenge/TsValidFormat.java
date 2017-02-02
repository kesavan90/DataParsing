package code.challenge;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TsValidFormat {

    ArrayList<String> list = new ArrayList<String>();


    public Date TsValidFormat(String event_time) {

        list.add("yyyy-MM-dd:HH:mm:ss.SSS'Z'");
        list.add("yyyy-MM-dd:HH:mm.ss.SSS'Z'");
        list.add("yyyy-MM-dd:HH:mm.SSS'Z'");

        Date date = null;

        for (int i = 0; i < list.size(); i++) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(list.get(i));
                sdf.setLenient(false);
                date = sdf.parse(event_time);
                if (event_time.equals(sdf.format(date))) {
                    return date;
                }

            } catch (ParseException ex) {
                //ex.printStackTrace();
            }
        }
        return date;
    }

}

