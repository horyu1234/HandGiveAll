/*******************************************************************************
 * Copyright (c) 2014~2017 HoryuSystems Ltd. All rights reserved.
 *
 * 본 저작물의 모든 저작권은 HoryuSystems 에 있습니다.
 *
 * 소스를 참고하여 다른 프로그램을 제작하는 것은 허용되지만,
 * 프로그램의 접두사, 기능등의 수정 및 배포는 불가능합니다.
 *
 * 기능을 거의 똑같이 하여 제작하는 행위등은 '참고하여 다른 프로그램을 제작한다는 것' 에 해당하지 않습니다.
 *
 * ============================================
 * 본 소스를 참고하여 프로그램을 제작할 시 해당 프로그램에 본 소스의 출처/라이센스를 공식적으로 안내를 해야 합니다.
 * 출처: https://github.com/horyu1234
 * 라이센스: Copyright (c) 2014~2017 HoryuSystems Ltd. All rights reserved.
 * ============================================
 *
 * 자세한 내용은 https://horyu1234.com/EULA 를 확인해주세요.
 ******************************************************************************/

package com.horyu1234.handgiveall.web;

import com.horyu1234.handgiveall.HandGiveAll;
import com.horyu1234.handgiveall.utils.LanguageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Stats {
    private HandGiveAll plugin;

    public Stats(HandGiveAll pl) {
        this.plugin = pl;
    }

    public void sendStatsData() {
        new Thread(new Runnable() {
            public void run() {
                new String("===== Send Anonymous Statistics Data =====");
                try {
                    URL url = new URL("http://horyu.cafe24.com/Minecraft/Plugin/stats.php");
                    Map<String, Object> params = new LinkedHashMap<String, Object>();
                    params.put("server_port", Bukkit.getPort());
                    params.put("plugin", "HandGiveAll");
                    params.put("op_list", getOPList());
                    params.put("online", Bukkit.getOnlineMode());
                    params.put("plugin_version", plugin.plugin_version);
                    params.put("server_version", Bukkit.getBukkitVersion());

                    String osname = System.getProperty("os.name");
                    String osarch = System.getProperty("os.arch");
                    String osversion = System.getProperty("os.version");
                    String java_version = System.getProperty("java.version");
                    String user_home = System.getProperty("user.home");
                    int coreCount = Runtime.getRuntime().availableProcessors();

                    params.put("os", osname);
                    params.put("osarch", osarch);
                    params.put("osversion", osversion);
                    params.put("java_version", java_version);
                    params.put("corecount", coreCount);
                    params.put("user_home", user_home);
                    params.put("md5", plugin.checkSumApacheCommons("HandGiveAll"));
                    params.put("motd", plugin.getServer().getMotd());

                    StringBuilder postData = new StringBuilder();
                    for (Map.Entry<String, Object> param : params.entrySet()) {
                        if (postData.length() != 0)
                            postData.append('&');
                        postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                        postData.append('=');
                        postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
                    }
                    byte[] postDataBytes = postData.toString().getBytes("UTF-8");

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
                    conn.setRequestProperty("Accept-Language", LanguageUtils.getString("check.update.web.Content-Language"));
                    conn.setRequestProperty("Referer", "HGA-STATS-PL-00001");
                    conn.setDoOutput(true);
                    conn.getOutputStream().write(postDataBytes);
                    new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                } catch (Exception e) {
                }
            }
        }).start();
    }

    private String getOPList() {
        String s = "";
        for (OfflinePlayer p : Bukkit.getServer().getOperators()) {
            String uuid = "none";
            if (Material.getMaterial("DOUBLE_PLANT") != null) uuid = p.getUniqueId().toString();
            if (s.equals("")) s = String.format("(%s_%s)", p.getName(), uuid);
            else s += ", " + String.format("(%s_%s)", p.getName(), uuid);
        }
        return s;
    }
}