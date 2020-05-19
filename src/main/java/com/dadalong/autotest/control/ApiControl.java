package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.response.FilterBaseUrlResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.FilterMapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@Api(value="/", description = "接口管理下的全部接口")
@RestController
@RequestMapping("/api")
public class ApiControl {

    @Resource
    FilterMapUtils filterMapUtils;

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
        String returnMsg = iApiService.createOrEditApi(createOrEditApiDTO);
        if (returnMsg.equals("编辑成功") || returnMsg.equals("创建成功")) {
            return TypedApiResponse.ok().message("createOrEdit-success");
        } else {
            return TypedApiResponse.error().message("操作失败");
        }
    }

    @ApiOperation(value="(批量)删除接口",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iApiService.deleteBatch(batchDTO);
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="查看接口详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(DetailDTO detailDTO){
        ApiListResponse apiListResponse = iApiService.detail(detailDTO);
        if (apiListResponse != null && StringUtils.isNotBlank(apiListResponse.toString())) {
            return TypedApiResponse.ok().message("detail-success").data(apiListResponse);
        } else {
            return TypedApiResponse.error().message("detail-error");
        }
    }

    @ApiOperation(value="批量导入接口数据",httpMethod = "POST")
    @PostMapping("/upload")
    public TypedApiResponse upload(UploadDTO uploadDTO) {
        String uploadMsg = iApiService.upload(uploadDTO);
//        System.out.println("上传文件：" + uploadDTO.getFile());
        switch (uploadMsg) {
            case "导入失败，请选择文件":
                return TypedApiResponse.error().message("导入失败，请选择文件");
            case "批量导入失败":
                return TypedApiResponse.error().message("批量导入失败");
            case "批量导入成功":
                return TypedApiResponse.ok().message("批量导入成功");
            default:
                return TypedApiResponse.error().message("loading");
        }
    }

    @ApiOperation(value="获取三级级联",httpMethod = "GET")
    @GetMapping("/filterMap")
    public FilterMapResponse filterMap() {
        return filterMapUtils.filterMap();
    }

    @ApiOperation(value="获取现有的环境域名下拉列表",httpMethod = "GET")
    @GetMapping("/filterBaseUrl")
    public FilterBaseUrlResponse filterBaseUrl() {
        return filterMapUtils.filterBaseUrl();
    }

    @ApiOperation(value="获取现接口参数信息",httpMethod = "GET")
    @GetMapping("/getReqQueryOrBody")
    public List<String> getReqBody(Integer apiId) {
        return filterMapUtils.getReqBody(apiId);
    }
}
