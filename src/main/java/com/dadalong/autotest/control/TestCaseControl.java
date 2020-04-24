package com.dadalong.autotest.control;


import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.user.CreateOrEditUserDTO;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Api(value="/", description = "这是用例管理下的全部接口")
@RestController
@RequestMapping("/case")
public class TestCaseControl {

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

//    @ApiOperation(value="删除账号",httpMethod = "POST")
//    @PostMapping("/delete")
//    public String deleteBatch(@RequestBody BatchDTO batchDTO){
//        iUserService.deleteBatch(batchDTO.getUserNumbers());
//        return "删除成功";
//    }
//
//    @ApiOperation(value="批量禁用账号",httpMethod = "POST")
//    @PostMapping("/disable")
//    public String disableBatch(@RequestBody BatchDTO batchDTO){
//        iUserService.disableBatch(batchDTO.getUserNumbers());
//        return "禁用成功";
//    }
//
//    @PostMapping("/enable")
//    public String enableBatch(@RequestBody BatchDTO batchDTO){
//        iUserService.enableBatch(batchDTO.getUserNumbers());
//        return "恢复成功";
//    }

//    @GetMapping("/filter-role")
//    public @ResponseBody List filterRole(String role){
//        return iUserService.filterRole(role);
//    }
//
//    @GetMapping("/filter-name")
//    public @ResponseBody List searchByName(String name){
//        return iUserService.searchByName(name);
//    }

}
