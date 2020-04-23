package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.*;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

@Api(value="/", description = "这是接口管理下的全部接口")
@RestController
@RequestMapping("/api")
public class ApiControl {

    private final IApiService iApiService;
    private final HttpServletRequest request;

    public ApiControl(IApiService iApiService, HttpServletRequest request){
        this.iApiService = iApiService;
        this.request = request;
    }

    @ApiOperation(value="显示接口列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用户列表信息引入pages
        IPage<com.dadalong.autotest.bean.v1.pojo.Api> pages = iApiService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new com.dadalong.autotest.bean.v1.pojo.Api());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="创建/编辑账号", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditUser(@RequestBody CreateOrEditUserDTO createOrEditUserDTO){
        iApiService.createOrEditUser(createOrEditUserDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    //不做userIds判空了

    @ApiOperation(value="(批量)删除账号",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iApiService.deleteBatch(batchDTO.getUserIds());
        return TypedApiResponse.ok().message("delete-success");
    }


    @ApiOperation(value="查看接口详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用户列表信息引入pages
        IPage<com.dadalong.autotest.bean.v1.pojo.Api> pages = iApiService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new User());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="上传json文件",httpMethod = "POST")
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        iApiService.handleUploadedFile(file);

    }


}
