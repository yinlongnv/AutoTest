package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.response.UserListResponse;
import com.dadalong.autotest.model.user.*;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value="/", description = "用户管理下的全部接口")
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
        } else if (user.getStatus() == 0){
            return TypedApiResponse.ok().message("登录成功").data(user);
        } else {
            return TypedApiResponse.error().message("该用户已被禁用！登录失败");
        }
    }

    @ApiOperation(value="显示用户列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用户列表信息引入pages
        IPage<UserListResponse> pages = iUserService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new UserListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="创建/编辑账号", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditUser(@RequestBody CreateOrEditUserDTO createOrEditUserDTO){
        String returnMsg = iUserService.createOrEditUser(createOrEditUserDTO);
        if (returnMsg.equals("编辑成功") || returnMsg.equals("创建成功")) {
            return TypedApiResponse.ok().message("createOrEdit-success");
        } else {
            return TypedApiResponse.error().message(returnMsg);
        }
    }

    @ApiOperation(value="(批量)删除账号",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        if (iUserService.deleteBatch(batchDTO)) {
            return TypedApiResponse.ok().message("删除成功");
        } else {
            return TypedApiResponse.error().message("删除失败");
        }
    }

    @ApiOperation(value="(批量)禁用账号",httpMethod = "POST")
    @PostMapping("/disable")
    public @ResponseBody TypedApiResponse disable(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO);
        return TypedApiResponse.ok().message("禁用成功");
    }

    @ApiOperation(value="(批量)启用账号",httpMethod = "POST")
    @PostMapping("/enable")
    public @ResponseBody TypedApiResponse enable(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO);
        return TypedApiResponse.ok().message("启用成功");
    }

    @ApiOperation(value="查看用户详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(DetailDTO detailDTO){
        User user = iUserService.detail(detailDTO);
        if (user != null && StringUtils.isNotBlank(user.toString())) {
            return TypedApiResponse.ok().message("detail-success").data(user);
        } else {
            return TypedApiResponse.error().message("detail-failed");
        }
    }
}
