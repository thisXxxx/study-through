package team.weilai.studythrough.util;

/**
 * @author gwj
 * @date 2024/11/7 20:18
 */
public class HaversineUtil {
    // 地球半径，单位：米
    private static final double R = 6371000;

    public static double haversine(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度从度转换为弧度
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 计算经纬度之间的差值
        double dlat = lat2Rad - lat1Rad;
        double dlon = lon2Rad - lon1Rad;

        // Haversine公式
        double a = Math.sin(dlat / 2) * Math.sin(dlat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dlon / 2) * Math.sin(dlon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // 计算距离，单位：米
        return R * c;
    }


}
