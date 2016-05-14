package com.fyxridd.lib.info.manager;

import com.fyxridd.lib.info.InfoPlugin;
import com.fyxridd.lib.info.model.InfoUser;
import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.EventExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InfoManager {
    //注: 如果一个属性InfoUser需要删除,则没有与InfoUser的data值为null是一样的
    //在这种情况下,会删除数据库中的InfoUser好节省空间,同时缓存中(infoHash)会保留InfoUser(data为null),这样下次不用再去数据库中读取

    //缓存

    //动态读取
    //玩家名 属性名 信息
    private Map<String, Map<String, InfoUser>> infoHash = new HashMap<>();

    //需要保存的信息列表
    private Set<InfoUser> needUpdateList = new HashSet<>();
    //需要删除的信息列表
    private Set<InfoUser> needDeleteList = new HashSet<>();

    public InfoManager() {
        //插件停止时更新
        Bukkit.getPluginManager().registerEvent(PluginDisableEvent.class, InfoPlugin.instance, EventPriority.HIGHEST, new EventExecutor() {
            @Override
            public void execute(Listener listener, Event e) throws EventException {
                saveAll();
            }
        }, InfoPlugin.instance);
        //定时更新
        Bukkit.getScheduler().scheduleSyncRepeatingTask(InfoPlugin.instance, new Runnable() {
            @Override
            public void run() {
                saveAll();
            }
        }, 308, 308);
    }

    /**
     * @see com.fyxridd.lib.info.api.InfoApi#getInfo(String, String)
     */
    public String getInfo(String name, String flag) {
        return get(name, flag).getData();
    }

    /**
     * @see com.fyxridd.lib.info.api.InfoApi#setInfo(String, String, String)
     */
    public void setInfo(String name, String flag, String data) {
        InfoUser info = get(name, flag);
        //一样的
        if (data == null) {
            if (info.getData() == null) return;
        }else {
            if (data.equals(info.getData())) return;
        }
        //设置
        info.setData(data);
        //更新
        if (data == null) {
            needUpdateList.remove(info);
            needDeleteList.add(info);
        }else {
            needUpdateList.add(info);
            needDeleteList.remove(info);
        }
    }

    /**
     * 获取玩家的属性信息(先从缓存中读取,没有再从数据库中读取并保存缓存)
     * @param name 玩家名,不为null
     * @param flag 属性名,不为null
     * @return 不为null(返回的属性信息勿修改,修改请调用setWithCheck方法)
     */
    private InfoUser get(String name, String flag) {
        //数据
        Map<String, InfoUser> hash = infoHash.get(name);
        if (hash == null) {
            hash = new HashMap<>();
            infoHash.put(name, hash);
        }

        //先从缓存读取
        InfoUser info = hash.get(flag);
        if (info != null) return info;

        //再从数据库中读取
        info = InfoPlugin.instance.getDaoManager().getInfoUser(name, flag);
        if (info == null) info = new InfoUser(name, flag, null);//null时不需要保存到数据库
        //保存缓存
        hash.put(flag, info);

        //返回
        return info;
    }

    /**
     * 更新
     */
    private void saveAll() {
        //保存
        if (!needUpdateList.isEmpty()) {
            InfoPlugin.instance.getDaoManager().saveOrUpdateInfoUsers(needUpdateList);
            needUpdateList.clear();
        }
        //删除
        if (!needDeleteList.isEmpty()) {
            InfoPlugin.instance.getDaoManager().deleteInfoUsers(needDeleteList);
            needDeleteList.clear();
        }
    }
}
