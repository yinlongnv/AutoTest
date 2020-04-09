package com.dadalong.autotest.control;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.*;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

@Api(value="/", description = "这是用户管理下的全部接口")
@RestController
@RequestMapping("/user")
public class UserControl {

    /**
     * 为方便都是如此定义 到时候再进行优化  所有异常判定都没有进行捕获，逻辑存在缺陷
     * 但是正常的CRUD操作没有问题
     */
    private final IUserService iUserService;
    private final HttpServletRequest request;

    public UserControl(IUserService iUserService,HttpServletRequest request){
        this.iUserService = iUserService;
        this.request = request;
    }

    @ApiOperation(value="创建账号",httpMethod = "POST")
    @PostMapping("/create")
    public String create(@RequestBody CreateUserDTO createUserDTO){
        iUserService.addUser(createUserDTO);
        return "创建成功";
    }

    @ApiOperation(value="删除账号",httpMethod = "POST")
    @PostMapping("/delete")
    public String deleteBatch(@RequestBody BatchDTO batchDTO){
        iUserService.deleteBatch(batchDTO.getUserNumbers());
        return "删除成功";
    }

    @ApiOperation(value="批量禁用账号",httpMethod = "POST")
    @PostMapping("/disable")
    public String disableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO.getUserNumbers());
        return "禁用成功";
    }

    @PostMapping("/enable")
    public String enableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO.getUserNumbers());
        return "恢复成功";
    }

    @GetMapping("/filter-role")
    public @ResponseBody Page<User> filterRole(String role,Integer page){
        return iUserService.filterRole(role,page);
    }

//    @GetMapping("/filter-name")
//    public @ResponseBody Page<User> searchByName(String name, Integer page){
//        return iUserService.searchByName(name,page);
//    }

//    @CrossOrigin("http://localhost:8080")
    @ApiOperation(value="显示用户列表",httpMethod = "GET")
    @GetMapping("/list")
    public @ResponseBody Page<User> list(String name,Integer page){
        Page<User> pages = iUserService.list(name,page);
        return pages;
    }

    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
         iUserService.handleUploadedFile(file);

    }


}
