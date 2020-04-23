package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.*;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@Api(value="/", description = "这是用户管理下的全部接口")
@RestController
@RequestMapping("/user")
public class UserControl {

    private final IUserService iUserService;
    private final HttpServletRequest request;

    public UserControl(IUserService iUserService, HttpServletRequest request){
        this.iUserService = iUserService;
        this.request = request;
    }

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public @ResponseBody TypedApiResponse login(@RequestBody LoginDTO loginDTO) {
        User user = iUserService.login(loginDTO);
        if (user == null) {
            return TypedApiResponse.error().message("用户名或密码错误！");
        } else {
            return TypedApiResponse.ok().message("Login Success").data(user);
        }
    }

    @ApiOperation(value="显示用户列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用户列表信息引入pages
        IPage<User> pages = iUserService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new User());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="创建/编辑账号", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditUser(@RequestBody CreateOrEditUserDTO createOrEditUserDTO){
        iUserService.createOrEditUser(createOrEditUserDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    //不做userIds判空了

    @ApiOperation(value="(批量)删除账号",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse deleteBatch(@RequestBody BatchDTO batchDTO){
        iUserService.deleteBatch(batchDTO.getUserIds());
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="(批量)禁用账号",httpMethod = "POST")
    @PostMapping("/disable")
    public @ResponseBody TypedApiResponse disableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO.getUserIds());
        return TypedApiResponse.ok().message("disable-success");
    }

    @ApiOperation(value="(批量)启用账号",httpMethod = "POST")
    @PostMapping("/enable")
    public @ResponseBody TypedApiResponse enableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO.getUserIds());
        return TypedApiResponse.ok().message("enable-success");
    }

    @ApiOperation(value="上传暂时放在这里",httpMethod = "POST")
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
         iUserService.handleUploadedFile(file);

    }


}
