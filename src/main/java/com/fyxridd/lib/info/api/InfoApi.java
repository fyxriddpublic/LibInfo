package com.fyxridd.lib.info.api;

import com.fyxridd.lib.info.InfoPlugin;

public class InfoApi {
    /**
     * 获取玩家的属性值
     * @param name 玩家名,不为null
     * @param flag 属性名,不为null
     * @return 属性值,不存在返回null
     */
    public static String getInfo(String name, String flag) {
        return InfoPlugin.instance.getInfoManager().getInfo(name, flag);
    }

    /**
     * 设置玩家的属性信息(玩家不存在此属性信息会新建)
     * @param name 玩家名,不为null
     * @param flag 属性名,不为null
     * @param data 属性值,null表示删除属性信息
     */
    public static void setInfo(String name, String flag, String data) {
        InfoPlugin.instance.getInfoManager().setInfo(name, flag, data);
    }
}
