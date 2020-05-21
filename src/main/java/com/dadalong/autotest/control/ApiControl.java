package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.other.CaseRules;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.response.FilterBaseUrlResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.model.response.ReqBodyResponse;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.FilterMapUtils;
import com.dadalong.autotest.utils.HandleUploadMsgUtils;
import com.dadalong.autotest.utils.RandomUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.RandomStringUtils;
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

    @Resource
    HandleUploadMsgUtils handleUploadMsgUtils;

    @Resource
    RandomUtils randomUtils;

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
            return TypedApiResponse.error().message("createOrEdit-failed");
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
            return TypedApiResponse.error().message("detail-failed");
        }
    }

    @ApiOperation(value="批量导入接口数据",httpMethod = "POST")
    @PostMapping("/upload")
    public TypedApiResponse upload(UploadDTO uploadDTO) {
        String uploadMsg = iApiService.upload(uploadDTO);
        return handleUploadMsgUtils.handleUploadMsgUtils(uploadMsg);
    }

    @ApiOperation(value="为指定apiId填入参数规则",httpMethod = "POST")
    @PostMapping("/caseRules")
    public TypedApiResponse putCaseRules(@RequestBody CaseRulesDTO caseRulesDTO) {
        if (iApiService.putCaseRules(caseRulesDTO)) {
            return TypedApiResponse.ok().message("填入参数规则成功");
        } else {
            return TypedApiResponse.error().message("填入参数规则失败，请重试");
        }
    }

    @ApiOperation(value="获取三级级联",httpMethod = "GET")
    @GetMapping("/filterMap")
    public TypedApiResponse filterMap() {
        return TypedApiResponse.ok().message("filterMap-success").data(filterMapUtils.filterMap());
    }

    @ApiOperation(value="获取现有的环境域名下拉列表",httpMethod = "GET")
    @GetMapping("/filterBaseUrl")
    public TypedApiResponse filterBaseUrl() {
        return TypedApiResponse.ok().message("filterBaseUrl-success").data(filterMapUtils.filterBaseUrl());
    }

    @ApiOperation(value="获取指定apiId的接口参数信息",httpMethod = "GET")
    @GetMapping("/getReqBody")
    public TypedApiResponse getReqBody(Integer apiId) {
        List<CaseRules> caseRulesList;
        caseRulesList = filterMapUtils.getReqBodyOrCaseRules(apiId);
        if (caseRulesList != null && StringUtils.isNotBlank(caseRulesList.toString())) {
            return TypedApiResponse.ok().message("getReqQueryOrBody-success").data(caseRulesList);
        } else {
            return TypedApiResponse.error().message("该接口不符合条件，请直接创建测试用例");
        }
    }

}
