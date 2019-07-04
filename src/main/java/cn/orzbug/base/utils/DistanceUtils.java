package cn.orzbug.base.utils;

/**
 * 经纬度距离的工具类
 *
 * @author: SQJ
 * @data: 2018/5/21 16:05
 * @version:
 */
public class DistanceUtils {

    /**
     * 地球半径
     */
    private static final double EARTH_RADIUS = 6378137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }


    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位M）
     *
     * @param lat1 用户经度
     * @param lon1 用户纬度
     * @param lat2 商家经度
     * @param lon2 商家纬度
     * @return
     */
    public static double GetDistance(double lon1, double lat1, double lon2, double lat2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lon1) - rad(lon2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        //单位米
        return s;
    }


}
