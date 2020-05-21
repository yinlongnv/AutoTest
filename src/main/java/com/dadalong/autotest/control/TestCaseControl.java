package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.*;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.utils.FilterMapUtils;
import com.dadalong.autotest.utils.HandleUploadMsgUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(value="/", description = "用例管理下的全部接口")
@RestController
@RequestMapping("/case")
public class TestCaseControl {

    @Resource
    FilterMapUtils filterMapUtils;

    @Resource
    HandleUploadMsgUtils handleUploadMsgUtils;

    private final ITestCaseService iTestCaseService;
    private final HttpServletRequest request;

    public TestCaseControl(ITestCaseService iTestCaseService, HttpServletRequest request){
        this.iTestCaseService = iTestCaseService;
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

    @ApiOperation(value="创建/编辑用例", httpMethod = "POST")
    @PostMapping("/createOrEdit")
    public @ResponseBody TypedApiResponse createOrEditApi(@RequestBody CreateOrEditCaseDTO createOrEditCaseDTO){
        iTestCaseService.createOrEditCase(createOrEditCaseDTO);
        return TypedApiResponse.ok().message("createOrEdit-success");
    }

    @ApiOperation(value="(批量)删除用例",httpMethod = "POST")
    @PostMapping("/delete")
    public @ResponseBody TypedApiResponse delete(@RequestBody BatchDTO batchDTO){
        iTestCaseService.deleteBatch(batchDTO);
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="查看用例详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(DetailDTO detailDTO){
        return TypedApiResponse.ok().message("detail-success").data(iTestCaseService.detail(detailDTO));
    }

    @ApiOperation(value="批量导入用例数据",httpMethod = "POST")
    @PostMapping("/upload")
    public TypedApiResponse upload(UploadDTO uploadDTO) {
        String uploadMsg = iTestCaseService.upload(uploadDTO);
        return handleUploadMsgUtils.handleUploadMsgUtils(uploadMsg);
    }

    @ApiOperation(value="获取三级级联",httpMethod = "GET")
    @GetMapping("/filterMap")
    public TypedApiResponse filterMap() {
        return TypedApiResponse.ok().message("filterMap-success").data(filterMapUtils.filterMap());
    }

    @ApiOperation(value="执行用例",httpMethod = "POST")
    @PostMapping("/execute")
    public TypedApiResponse execute(@RequestBody ExecuteDTO executeDTO) {
        if (iTestCaseService.execute(executeDTO)) {
            return TypedApiResponse.ok().message("执行成功");
        } else {
            return TypedApiResponse.error().message("用例执行失败，请重试");
        }
    }

}
