package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.model.api.BatchDTO;
import com.dadalong.autotest.model.api.CreateOrEditApiDTO;
import com.dadalong.autotest.model.api.DetailDTO;
import com.dadalong.autotest.model.api.UploadDTO;
import com.dadalong.autotest.model.response.*;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@Transactional
public class ApiServiceImpl extends ServiceImpl<ApiMapper,Api> implements IApiService {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Resource
    DeleteFileUtils deleteFileUtils;

    @Resource
    ExcelUtils excelUtils;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

    /**
     * 获取接口列表，包含筛选查询
     * @param searchRequest
     * @return
     */
    public IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest){
        try {
            ApiWrapper wrapper = new ApiWrapper();
            wrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<ApiListResponse> apiListResponseList = new ArrayList<>();
            Map<String,Object> map = searchRequest.getSearch();

            SlabPage<Api> apiSlabPage = new SlabPage<>(searchRequest);
            IPage<Api> apiResults = apiMapper.selectPage(apiSlabPage,wrapper);
            for (Api record : apiResults.getRecords()) {
                User user = userMapper.selectById(record.getUserId());
                ApiListResponse apiListResponse = new ApiListResponse();
                BeanUtils.copyProperties(record, apiListResponse);
                if(user != null && StringUtils.isNotBlank(user.toString())) {
                    apiListResponse.setCreatedBy(user.getUsername());
                } else {
                    apiListResponse.setCreatedBy("root");
                }
                apiListResponseList.add(apiListResponse);
            }
            SlabPage<ApiListResponse> apiListResponseSlabPage = new SlabPage<>(searchRequest);
            apiListResponseSlabPage.setRecords(apiListResponseList);
            apiListResponseSlabPage.setTotal(apiResults.getTotal());

            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(Integer.parseInt(String.valueOf(userId)), LogContentEnumUtils.APILIST, OperatePathEnumUtils.APILIST);
            return apiListResponseSlabPage;
        }catch (Exception e){
            LOGGER.error("listWithSearchError",e);
            throw new ConflictException("listWithSearchError");
        }
    }

    /**
     * 创建/编辑接口
     * @param createOrEditApiDTO 从前端传回来的json格式数据转换的对象
     */
    @Override
    public void createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO) {
        Api api = new Api();
        BeanUtils.copyProperties(createOrEditApiDTO, api, "userId");

        if(createOrEditApiDTO.getId() == null || createOrEditApiDTO.getId().toString().equals("")) {
            api.setUserId(createOrEditApiDTO.getUserId());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APICREATE, OperatePathEnumUtils.APICREATE);
            apiMapper.insert(api);
        } else {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APIEDIT, OperatePathEnumUtils.APIEDIT);
            apiMapper.updateById(api);
        }
    }

    /**
     * (批量)删除接口
     * @param batchDTO
     */
    @Override
    public void deleteBatch(BatchDTO batchDTO) {
        try {
            removeByIds(batchDTO.getApiIds());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.APIDELETE, OperatePathEnumUtils.APIDELETE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取接口详情
     * @param detailDTO
     * @return
     */
    @Override
    public ApiListResponse detail(DetailDTO detailDTO) {
        ApiListResponse apiListResponse = new ApiListResponse();
        Api api = apiMapper.selectById(detailDTO.getId());
        BeanUtils.copyProperties(api, apiListResponse);
        User user = userMapper.selectById(api.getUserId());
        if (user != null && StringUtils.isNotBlank(user.toString())) {
            String username = user.getUsername();
            apiListResponse.setCreatedBy(username);
        } else {
            apiListResponse.setCreatedBy("root");
        }
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.APIDETAIL, OperatePathEnumUtils.APIDETAIL);
        return apiListResponse;
    }

    @Override
    public String upload(UploadDTO uploadDTO) {
        if (uploadDTO.getFile().isEmpty()) {
            return "导入失败，请选择文件！";
        }

        String fileName = uploadDTO.getFile().getOriginalFilename();
//        System.out.println("上传文件的文件名是："+fileName);
        String filePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi\\";
        File dest = new File(filePath + fileName);
        try {
            uploadDTO.getFile().transferTo(dest);
//            String filename = "file.txt";// 文件名
            String[] strArray = new String[0];
            if (fileName != null) {
                strArray = fileName.split("\\.");
            }
            int suffixIndex = strArray.length -1;
            //获取上传文件的文件类型
            String fileType = strArray[suffixIndex];
//            System.out.println("文件类型为："+strArray[suffixIndex]);
            if (fileType.equals("html")) {
//                String pyPath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\pyToSql\\analysis_html.py";
                String[] argument = new String[]{"python", "D://Workspace/IDEA/AutoTest/src/main/resources/static/pyToSql/analysis_html.py", uploadDTO.getUserId().toString(), uploadDTO.getBaseUrl()};
                try{
                    Process process = Runtime.getRuntime().exec(argument);
                    //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
                    //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
                    int re = process.waitFor();
                    System.out.println("python运行的返回值："+re);
                    return "批量导入成功！";
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                System.out.println("不是html文件");
            }
        } catch (IOException e) {
            System.out.println("批量导入错误：" + e);
        }
        return "批量导入失败！";
    }

    @Override
    public Api exportDemo() {
        return apiMapper.selectById(1);
    }
}
