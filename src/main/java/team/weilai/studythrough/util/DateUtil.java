package team.weilai.studythrough.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static team.weilai.studythrough.constants.Constants.DATE_FORMAT;
import static team.weilai.studythrough.constants.Constants.YEAR_DATE_FORMAT;


public class DateUtil {
    public static Date parseString(String date) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.parse(date);
    }

    public static String getDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YEAR_DATE_FORMAT);
        return simpleDateFormat.format(new Date());
    }

    public static long between(Date start, Date end) {
        long st = start.getTime();
        long en = end.getTime();
        long diff = en - st;
        return diff / (60 * 1000);
    }
}
