package com.dadalong.autotest.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.CreateUserDTO;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.sql.Date;
import java.util.List;


public interface IUserService {

    /**
     *创建账号
     * @param createUserDTO 从前端传回来的json格式数据转换的对象
     */
    public void addUser(CreateUserDTO createUserDTO);

    /**
     * 通过传回来的用户编号进行批量删除
     * @param lists 用户编号列表
     */
    public void deleteBatch(String[] lists);

    /**
     * 通过传回来的用户编号进行批量禁用
     * @param lists
     */
    public void disableBatch(String[] lists);

    /**
     * 通过传回的用户编号进行批量启用
     * @param lists
     */
    public void enableBatch(String[] lists);

    /**
     * 通过传回来的账号角色进行筛选
     * @param role 角色类型
     * @return 返回筛选结果
     */
    public Page<User> filterRole(String role,Integer page);

    /**
     * 根据最后登陆的时间进行搜索
     * @param lastLoginTime 最后登陆的时间
     * @return 返回筛选结果
     */
    public Page<User> searchByDate(Date lastLoginTime,Integer page);

    /**
     * 根据传进来的用户名/用户编号进行模糊搜索
     * @param name
     * @return 返回搜索结果
     */
    public Page<User> searchByName(String name,Integer page);

    /**
     * 返回用户列表
     * @param page 分页
     * @return
     */
    public Page<User> list(Integer page);

    /**
     * 接收上传的json文件
     * @param file
     * @return
     * @throws IOException
     */
    public String handleUploadedFile(MultipartFile file) throws IOException;
}
