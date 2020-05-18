package com.dadalong.autotest.control;

import cn.com.dbapp.slab.common.model.dto.CollectionResponse;
import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.java.commons.models.TypedApiResponse;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dadalong.autotest.bean.v1.model.ApiExcel;
import com.dadalong.autotest.model.response.ApiListResponse;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.response.FilterBaseUrlResponse;
import com.dadalong.autotest.model.response.FilterMapResponse;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.DeleteFileUtils;
import com.dadalong.autotest.utils.ExcelUtils;
import com.dadalong.autotest.utils.FilterMapUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Api(value="/", description = "接口管理下的全部接口")
@RestController
@RequestMapping("/api")
public class ApiControl {

    @Resource
    FilterMapUtils filterMapUtils;

    @Resource
    DeleteFileUtils deleteFileUtils;

    @Resource
    ExcelUtils excelUtils;

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
        iApiService.deleteBatch(batchDTO);
        return TypedApiResponse.ok().message("delete-success");
    }

    @ApiOperation(value="查看接口详情",httpMethod = "GET")
    @GetMapping("/detail")
    public TypedApiResponse detail(DetailDTO detailDTO){
        return TypedApiResponse.ok().message("detail-success").data(iApiService.detail(detailDTO));
    }

    @ApiOperation(value="批量导入接口数据",httpMethod = "POST")
    @PostMapping("/upload")
    public TypedApiResponse upload(UploadDTO uploadDTO) {
        String upload_msg = iApiService.upload(uploadDTO);
        System.out.println("上传文件："+uploadDTO.getFile());
        switch (upload_msg) {
            case "导入失败，请选择文件！":
                return TypedApiResponse.error().message("导入失败，请选择文件！");
            case "批量导入失败！":
                return TypedApiResponse.error().message("批量导入失败！");
            case "批量导入成功！":
                return TypedApiResponse.error().message("批量导入成功！");
            default:
                return TypedApiResponse.error().message("loading");
        }
    }

    @GetMapping("/deleteFile")
    public String deleteFile() {
        String filePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadCase\\";
        if (!deleteFileUtils.delAllFile(filePath)) {
            return "删除文件夹下的所有文件！";
        } else {
            return "failed";
        }
    }
//    @GetMapping("/export")
//    public void exportExcel(HttpServletResponse response) {
//        List<ApiExcel> personList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            ApiExcel personVo = new ApiExcel();
//            personVo.setUserName("张三" + i);
//            personVo.setUserAge(18);
//            personVo.setFavorite("羽毛球");
//            personList.add(personVo);
//        }
//        try {
//            excelUtils.exportExcel(personList, "体育课学生名单", "学生信息", ApiExcel.class, "体育课学生名单", response);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }

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

}
