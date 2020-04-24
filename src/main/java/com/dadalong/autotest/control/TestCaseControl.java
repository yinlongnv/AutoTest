package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.ApiNameListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.BatchDTO;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(value="/", description = "这是用例管理下的全部接口")
@RestController
@RequestMapping("/case")
public class TestCaseControl {

    private final ITestCaseService iTestCaseService;
    private final IApiService iApiService;
    private final HttpServletRequest request;

    public TestCaseControl(ITestCaseService iTestCaseService, IApiService iApiService, HttpServletRequest request){
        this.iTestCaseService = iTestCaseService;
        this.iApiService = iApiService;
        this.request = request;
    }

    @ApiOperation(value="显示用例列表，包含筛选查询",httpMethod = "GET")
    @GetMapping("/listWithSearch")
    public TypedApiResponse listWithSearch(){
        //提取请求中的参数到map中
        SearchRequest searchRequest = new SearchRequest(request);
        //将分页信息和筛选查询到的用例列表信息引入pages
        IPage<TestCaseListResponse> pages = iTestCaseService.listWithSearch(searchRequest);
        //构造响应体pageInfo+tbody形式
        CollectionResponse response = new CollectionResponse<>(pages, new TestCaseListResponse());
        return TypedApiResponse.ok().message("listWithSearch-success").data(response);
    }

    @ApiOperation(value="获取所有关联接口筛选项列表",httpMethod = "GET")
    @GetMapping("/getApiNameList")
    public TypedApiResponse getApiNameList(){
        List<ApiNameListResponse> apiNameListResponses = iApiService.getApiNameList();
        return TypedApiResponse.ok().message("apiNameList-success").data(apiNameListResponses);
    }

    @ApiOperation(value="(批量)删除用例",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iTestCaseService.deleteBatch(batchDTO.getCaseIds());
        return TypedApiResponse.ok().message("delete-success");
    }

}
