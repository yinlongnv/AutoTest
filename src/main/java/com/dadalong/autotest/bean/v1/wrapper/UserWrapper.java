package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class UserWrapper extends QueryWrapper<User> {

    /**
     * 用于用户登录
     * @param username
     * @param password
     * @return
     */
    public UserWrapper ofUsernameAndPassword(String username, String password) {
        this.eq("username", username).eq("password", password);
        return this;
    }

    /**
     * 显示用户列表，包含筛选查询功能
     * @param request
     * @return
     */
    public UserWrapper ofListWithSearch(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        // 筛选登录起止时间段
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        this.ofTime(startTime, endTime);
        // 筛选用户角色
        this.ofRole(map.get("role"));
        // 筛选用户信息：用户名/用户编号
        Object search = map.get("userInfo");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.like("username", search.toString()).or().like("user_number", search.toString());
        }
        return this;
    }

    /**
     * 筛选角色
     * @param role
     * @return
     */
    public UserWrapper ofRole(Object role){
        if(role != null && StringUtils.isNotBlank(role.toString())){
            this.eq("role",Integer.parseInt(role.toString()));
        }
        return this;
    }

    /**
     * 筛选最后登录起止时间段
     * @param startTime
     * @param endTime
     * @return
     */
    public UserWrapper ofTime(Object startTime,Object endTime){
        if(startTime != null && endTime != null && StringUtils.isNotBlank(startTime.toString()) && StringUtils.isNotBlank(endTime.toString())){
            this.between("last_login",startTime,endTime);
            return this;
        }
        return this;
    }

}
