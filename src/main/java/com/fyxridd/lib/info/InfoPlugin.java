package com.fyxridd.lib.info;

import com.fyxridd.lib.core.api.SimplePlugin;
import com.fyxridd.lib.info.manager.DaoManager;
import com.fyxridd.lib.info.manager.InfoManager;

public class InfoPlugin extends SimplePlugin{
    public static InfoPlugin instance;

    private InfoManager infoManager;
    private DaoManager daoManager;

    @Override
    public void onEnable() {
        instance = this;

        infoManager = new InfoManager();
        daoManager = new DaoManager();

        super.onEnable();
    }

    public InfoManager getInfoManager() {
        return infoManager;
    }

    public DaoManager getDaoManager() {
        return daoManager;
    }
}