package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.service.IApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Api(value="/", description = "接口管理下的全部接口")
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
        //将分页信息和筛选查询到的接口列表信息引入pages
        IPage<ApiListResponse> pages = iApiService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new ApiListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="创建/编辑接口", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditApi(@RequestBody CreateOrEditApiDTO createOrEditApiDTO){
        iApiService.createOrEditApi(createOrEditApiDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    @ApiOperation(value="(批量)删除接口",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iApiService.deleteBatch(batchDTO.getApiIds());
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="查看接口详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(Integer id){
        return TypedApiResponse.ok().message("detail-success").data(iApiService.detail(id));
    }

    @ApiOperation(value="上传json文件",httpMethod = "POST")
    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
        iApiService.handleUploadedFile(file);

    }

    @ApiOperation(value="获取三级级联",httpMethod = "GET")
    @GetMapping("/filterMap")
    public FilterMapResponse filterMap() {
        return iApiService.filterMap();
    }
}
