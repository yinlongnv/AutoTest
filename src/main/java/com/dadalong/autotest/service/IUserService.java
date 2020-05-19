package com.dadalong.autotest.service;


import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.response.UserListResponse;
import com.dadalong.autotest.model.user.BatchDTO;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.DetailDTO;
import com.dadalong.autotest.model.user.LoginDTO;

public interface IUserService {

    /**
     * 用户登录，更新用户最后登录IP和最后登录时间，返回当前登录用户全部信息
     * @param loginDTO
     * @return
     */
    User login(LoginDTO loginDTO);

    /**
     * 获取用户列表，可同时筛选搜索条件
     * @param searchRequest
     * @return
     */
    IPage<UserListResponse> listWithSearch(SearchRequest searchRequest);

    /**
     * 创建/编辑用户，以Id区分是创建还是编辑
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
    String createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO);

    /**
     * (批量)删除用户
     * @param batchDTO
     */
    Boolean deleteBatch(BatchDTO batchDTO);

    /**
     * (批量)禁用用户
     * @param batchDTO
     */
    void disableBatch(BatchDTO batchDTO);

    /**
     * (批量)启用用户
     * @param batchDTO
     */
    void enableBatch(BatchDTO batchDTO);

    /**
     * 查看用户详情
     * @param detailDTO
     * @return
     */
    User detail(DetailDTO detailDTO);

}
