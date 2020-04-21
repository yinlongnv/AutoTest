package com.dadalong.autotest.control;


import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
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

    @ApiOperation(value = "用户登录", httpMethod = "POST")
    @PostMapping("/login")
    public @ResponseBody TypedApiResponse login(LoginDTO loginDTO) {
        User user = iUserService.login(loginDTO);
        if (user == null) {
            return TypedApiResponse.error().message("用户名或密码错误！");
        } else {
            return TypedApiResponse.ok().message("Login Success").data(user);
        }
    }

    @ApiOperation(value="显示用户列表",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public @ResponseBody TypedApiResponse listWithSearch(ListWithSearchDTO listWithSearchDTO){
        if (listWithSearchDTO.getPage() == null) {
            return TypedApiResponse.error().message("page can't be null");
        } else {
            Page<User> pages = iUserService.listWithSearch(listWithSearchDTO);
            return TypedApiResponse.ok().message("listWithSearch-success").data(pages);
        }
    }

    @ApiOperation(value="创建/编辑账号", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditUser(@RequestBody CreateOrEditUserDTO createOrEditUserDTO){
        iUserService.createOrEditUser(createOrEditUserDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    @ApiOperation(value="删除账号",httpMethod = "POST")
    @PostMapping("/delete")
    public String deleteBatch(@RequestBody BatchDTO batchDTO){
        iUserService.deleteBatch(batchDTO.getUserIds());
        return "删除成功";
    }

    @ApiOperation(value="批量禁用账号",httpMethod = "POST")
    @PostMapping("/disable")
    public String disableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO.getUserIds());
        return "禁用成功";
    }

    @ApiOperation(value="批量启用账号",httpMethod = "POST")
    @PostMapping("/enable")
    public String enableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO.getUserIds());
        return "恢复成功";
    }

    @ApiOperation(value="上传暂时放在这里",httpMethod = "POST")
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
         iUserService.handleUploadedFile(file);

    }


}
