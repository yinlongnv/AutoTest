package com.dadalong.autotest.bean.v1.wrapper;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.ListWithSearchDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Map;

public class UserWrapper extends QueryWrapper<User> {

    public UserWrapper ofUsernameAndPassword(String username, String password) {
        this.eq("username", username).eq("password", password);
        return this;
    }

    public UserWrapper ofUsername(String username) {
        this.eq("username", username);
        return this;
    }

    public UserWrapper ofUserNumber(String userNumber){
        this.eq("user_number",userNumber);
        return this;
    }

    public UserWrapper filterOfRole(String role){
        this.eq("role",role);
        return this;
    }

    public UserWrapper ofLastLoginDate(Date lastLogin){
        this.eq("last_login",lastLogin);
        return this;
    }

    public UserWrapper oflikeName(String name){
        this.like("user_number",name).or().like("username",name);
        return this;
    }

    public UserWrapper ofListWithSearch(ListWithSearchDTO listWithSearchDTO){
//            this.like(StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()), "user_number", listWithSearchDTO.getUserNumber())
//                    .like(StringUtils.isNotBlank(String.valueOf(listWithSearchDTO.getRole())), "role", listWithSearchDTO.getRole())
//                    .like(StringUtils.isNotBlank(listWithSearchDTO.getUsername()), "username", listWithSearchDTO.getUsername())
//                    .between("last_login", listWithSearchDTO.getStartTime(), listWithSearchDTO.getEndTime());
        System.out.println("role：" + StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()));
        this.like(StringUtils.isNotBlank(listWithSearchDTO.getUserNumber()), "user_number", listWithSearchDTO.getUserNumber())
                .like(StringUtils.isNotBlank(String.valueOf(listWithSearchDTO.getRole())), "role", listWithSearchDTO.getRole())
                .like(StringUtils.isNotBlank(listWithSearchDTO.getUsername()), "username", listWithSearchDTO.getUsername())
                .between(StringUtils.isNotBlank(listWithSearchDTO.getStartTime()) || StringUtils.isNotBlank(listWithSearchDTO.getEndTime()), "last_login", listWithSearchDTO.getStartTime(), listWithSearchDTO.getEndTime());
        return this;
    }

    /**
     * private String username;
     *     private String userNumber;
     *     private Integer role;
     *     private String startTime;
     *     private String endTime;
     *     private Integer page;
     * @param request
     * @return
     */
    public UserWrapper search(SearchRequest request){
        Map<String,Object> map = request.getSearch();
        Object search = map.get("search");
        if (search != null && StringUtils.isNotBlank(search.toString())) {
            this.and(wrapper ->
                    wrapper.like("username", search.toString())
                            .or().like("user_number", search.toString())
            );
        }
        this.ofRole(map.get("role"));
        Object startTime = map.get("startTime");
        Object endTime = map.get("endTime");
        this.ofTime(startTime,endTime);
        return  this;
    }


    public UserWrapper ofRole(Object role){
        if(role != null && StringUtils.isNotBlank(role.toString())){
            System.out.println(Integer.parseInt(role.toString()));
            this.eq("role",Integer.parseInt(role.toString()));
        }
        return this;
    }

    //时间
    public UserWrapper ofTime(Object startTime,Object endTime){
        if(startTime != null && endTime != null && StringUtils.isNotBlank(startTime.toString()) && StringUtils.isNotBlank(endTime.toString())){
            this.between("created_at",startTime,endTime);
            return this;
        }
        return this;
    }


}
