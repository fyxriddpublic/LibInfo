package com.fyxridd.lib.info.manager;

import com.fyxridd.lib.core.api.SqlApi;
import com.fyxridd.lib.info.mapper.InfoUserMapper;
import com.fyxridd.lib.info.model.InfoUser;
import org.apache.ibatis.session.SqlSession;

import java.util.Collection;

/**
 * 与数据库交互
 */
public class DaoManager {
    public InfoUser getInfoUser(String name, String flag) {
        SqlSession session = SqlApi.getSqlSessionFactory().openSession();
        try {
            InfoUserMapper mapper = session.getMapper(InfoUserMapper.class);
            return mapper.select(name, flag);
        } finally {
            session.close();
        }
    }

    public void saveOrUpdateInfoUsers(Collection<InfoUser> c) {
        if (c == null || c.isEmpty()) return;

        SqlSession session = SqlApi.getSqlSessionFactory().openSession();
        try {
            InfoUserMapper mapper = session.getMapper(InfoUserMapper.class);
            for (InfoUser user:c) {
                if (!mapper.exist(user.getName(), user.getFlag())) mapper.insert(user);
                else mapper.update(user);
            }
            session.commit();
        } finally {
            session.close();
        }
    }

    public void deleteInfoUsers(Collection<InfoUser> c) {
        if (c == null || c.isEmpty()) return;

        SqlSession session = SqlApi.getSqlSessionFactory().openSession();
        try {
            InfoUserMapper mapper = session.getMapper(InfoUserMapper.class);
            for (InfoUser user:c) mapper.delete(user);
            session.commit();
        } finally {
            session.close();
        }
    }
}
