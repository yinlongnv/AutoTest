package com.dadalong.autotest.service;


import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.model.user.ListWithSearchDTO;
import com.dadalong.autotest.model.user.LoginDTO;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.sql.Date;

public interface IUserService {

    /**
     * 用户登录，更新用户最后登录IP和最后登录时间，返回当前登录用户全部信息
     * @param loginDTO
     * @return
     */
    public User login(LoginDTO loginDTO);

    /**
     * 获取用户列表，可同时筛选搜索条件
     * @param listWithSearchDTO
     * @return
     */
    public Page<User> listWithSearch(ListWithSearchDTO listWithSearchDTO);

    /**
     *创建或编辑账号
     * @param createOrEditUserDTO 从前端传回来的json格式数据转换的对象
     */
    public void createOrEditUser(CreateOrEditUserDTO createOrEditUserDTO);

    /**
     * 通过传回来的用户编号进行批量删除
     * @param lists 用户编号列表
     */
    public void deleteBatch(Integer[] lists);

    /**
     * 通过传回来的用户编号进行批量禁用
     * @param lists
     */
    public void disableBatch(Integer[] lists);

    /**
     * 通过传回的用户编号进行批量启用
     * @param lists
     */
    public void enableBatch(Integer[] lists);

    /**
     * 接收上传的json文件
     * @param file
     * @return
     * @throws IOException
     */
    public String handleUploadedFile(MultipartFile file) throws IOException;

    public IPage<User> search(SearchRequest searchRequest);
}
