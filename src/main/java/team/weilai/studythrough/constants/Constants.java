package team.weilai.studythrough.constants;

/**
 * @author gwj
 * @date 2024/5/16 22:35
 */
public class Constants {

    public static final String ARG_ERROR = "参数错误";
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String YEAR_DATE_FORMAT = "yyyy-MM-dd";
    public static final String SOURCE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
    public static final String PATH_PREFIX = "http://81.70.144.36:9000/";
    public static final String WEBSOCKET_PROTOCOL="Sec-WebSocket-Protocol";

    public static final String PRO_URL = System.getProperty("user.dir").replaceAll("\\\\", "/");
    public static final String TEMP = "/file/tmp/";

    public static final Integer SUCCESS = 0;
    public static final Integer TIME_EXCEED = 1;
    public static final Integer DISTANT_EXCEED = 2;

    public static final Integer NO_CHOOSE = 3;
}
