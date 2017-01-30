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

package com.horyu1234.handgiveall.utils;

import com.horyu1234.handgiveall.HandGiveAll;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Collection;

public class PlayerUtils {
    @SuppressWarnings("unused")
    private HandGiveAll plugin;

    public PlayerUtils(HandGiveAll pl) {
        this.plugin = pl;
    }

    public static Player[] getOnlinePlayers() {
        Class<?>[] parameterTypes = {};
        Object obj = null;
        try {
            obj = Bukkit.class.getMethod("getOnlinePlayers", parameterTypes).invoke(null, (Object[]) null);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        if (obj instanceof Player[]) {
            return (Player[]) obj;
        } else if (obj instanceof Collection) {
            @SuppressWarnings("unchecked")
            Collection<? extends Player> onlinePlayer = (Collection<? extends Player>) obj;
            Player[] onlinePlayers = new Player[onlinePlayer.size()];
            int i = 0;
            for (Player player : onlinePlayer) {
                onlinePlayers[i] = player;
                i++;
            }
            return onlinePlayers;
        }
        return new Player[]{};
    }

    public static void sendMsg(String msg) {
        Bukkit.getConsoleSender().sendMessage(msg);
        for (Player all : getOnlinePlayers()) {
            all.sendMessage(msg);
        }
    }

    public static String getOPList() {
        String s = "";
        for (OfflinePlayer p : Bukkit.getServer().getOperators()) {
            String uuid = "none";
            if (Material.getMaterial("DOUBLE_PLANT") != null) uuid = p.getUniqueId().toString();
            if (s.equals("")) s = String.format("(%s_%s)", p.getName(), uuid);
            else s += ", " + String.format("(%s_%s)", p.getName(), uuid);
        }
        return s;
    }

    public Player getPlayer(String name) {
        for (Player all : getOnlinePlayers())
            if (all.getName().equalsIgnoreCase(name))
                return all;
        return null;
    }
}