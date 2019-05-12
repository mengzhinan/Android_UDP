package com.duke.udp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author: duke
 * @DateTime: 2019-05-11 09:55
 * @Description:
 */
public class UDPUtil {

    public static boolean isNULL(Object o) {
        return o == null;
    }

    public static boolean isNULL(Object[] arr) {
        return isNULL(arr) || arr.length == 0;
    }

    public static boolean isEmpty(String text) {
        return isNULL(text) || text.trim().length() == 0;
    }

    /**
     * 是否是多播地址
     *
     * @return
     */
    public static boolean isMulticastAddress(String ip) {
        InetAddress address = getInetAddressByIP(ip);
        return address != null && address.isMulticastAddress();
    }

    /**
     * 获取局域网默认的广播地址
     *
     * @return InetAddress 对象。如："/255.255.255.255"
     */
    public static InetAddress getDefaultBroadCastIP() {
        return getInetAddressByIP("255.255.255.255");
    }

    /**
     * 获取 ip 对应的 InetAddress 对象
     *
     * @param ip ip 地址，如：239.2.3.41
     * @return InetAddress 对象。如："/239.2.3.41"
     */
    public static InetAddress getInetAddressByIP(String ip) {
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(ip + " is Incorrect IP address");
        }
    }

    /**
     * 获取本地的ip地址
     *
     * @return InetAddress 对象。如："/192.168.120.36"
     */
    public static InetAddress getLocalIPAddress() {
        try {
            // 获取所有网卡接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                // 获取某一网卡接口
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 得到该网卡接口上的所有ip信息
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    // 得到具体ip数据
                    InetAddress inetAddress = inetAddresses.nextElement();
                    // 判断此ip是否为环回地址
                    // 当IP地址是loopback地址时返回true，否则返回false.loopback地址就是代表本机的IP地址。
                    // IPv4的loopback地址的范围是127.0.0.0 ~ 127.255.255.255，
                    // 也就是说，只要第一个字节是127，就是lookback地址。如127.1.2.3、127.0.200.200都是loopback地址。
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
                        return inetAddress;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 待验证
     *
     * @param context
     * @return
     */
    public static String getLocalIP2(Context context) {
        String ip = null;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // 需要<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            ip = intToIp(ipAddress);
        } else if (mobileNetworkInfo.isConnected()) {
            InetAddress inetAddress = getLocalIPAddress();
            ip = getAddressIPToString(inetAddress);
        }
        return ip;
    }


    /**
     * 如果手机被设置为热点时，返回的广播地址是：192.168.43.255 或 255.255.255.255
     *
     * @param context
     * @return InetAddress 对象。如："/192.168.120.255"
     * @throws UnknownHostException
     */
    public static InetAddress getBroadcastAddress(Context context) throws UnknownHostException {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        if (dhcp == null) {
            return getDefaultBroadCastIP();
        }
        // ip地址 与 子网掩码 & 运算，再与子网掩码取反 | 运算
        int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        byte[] bytes = new byte[4];
        for (int k = 0; k < 4; k++) {
            bytes[k] = (byte) ((broadcast >> k * 8) & 0xFF);
        }
        return InetAddress.getByAddress(bytes);
    }

    /**
     * 返回 inetAddress 的字符串表示形式
     *
     * @param inetAddress
     * @return 如："192.168.120.36"
     */
    public static String getAddressIPToString(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
//        inetAddress.getHostName();
        return inetAddress.getHostAddress();
    }

    /**
     * 偷懒
     *
     * @return
     */
    public static String getSimpleIP() {
        return getAddressIPToString(getLocalIPAddress());
    }

    //获取Wifi ip 地址
    public static String intToIp(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    public static String getBytesIPToString(byte[] bytes) {
        return (bytes[0] & 0xFF) + "." +
                (bytes[1] & 0xFF) + "." +
                (bytes[2] & 0xFF) + "." +
                (bytes[3] & 0xFF);
    }

    /**
     * 是否是热点设备
     *
     * @param context
     * @return
     */
    public static Boolean isWifiApEnabled(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getMethod("isWifiApEnabled");
            return (Boolean) method.invoke(manager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

//    public ArrayList getClientList(boolean onlyReachables, int reachableTimeout) {
//        BufferedReader br = null;
//        ArrayList<ClientScanResultSO> result = null;
//
//        try {
//            result = new ArrayList<ClientScanResultSO>();
//            br = new BufferedReader(new FileReader("/proc/net/arp"));
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] splitted = line.split(" +");
//
//                if ((splitted != null) && (splitted.length >= 4)) {
//                    // Basic sanity check
//                    String mac = splitted[3];
//                    System.out.println("mac is***************"+ mac);
//                    if (mac.matches("..:..:..:..:..:..")) {
//                        boolean isReachable = InetAddress.getByName(splitted[0]).isReachable(reachableTimeout);
//                        String name = InetAddress.getByName(splitted[0]).getHostName();
//                        if (!onlyReachables || isReachable) {
//                            result.add(new ClientScanResultSO(splitted[0], splitted[3], splitted[5], isReachable, name));
//                        }
//                    }
//                }
//            }
//        } catch (Exception e) {
//            Log.e(this.getClass().toString(), e.getMessage());
//        } finally {
//            try {
//                br.close();
//            } catch (IOException e) {
//                Log.e(this.getClass().toString(), e.getMessage());
//            }
//        }
//
//        return result;
//    }

}
