package com.fyxridd.lib.info.mapper;

import com.fyxridd.lib.info.model.InfoUser;
import org.apache.ibatis.annotations.Param;

public interface InfoUserMapper {
    /**
     * 检测是否存在
     */
    boolean exist(@Param("name") String name, @Param("flag") String flag);

    /**
     * @return 不存在返回null
     */
    InfoUser select(@Param("name") String name, @Param("flag") String flag);

    void insert(InfoUser user);

    void update(InfoUser user);

    void delete(InfoUser user);
}
