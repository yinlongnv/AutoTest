package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.ApiMapper;
import com.dadalong.autotest.bean.v1.mapper.TestCaseMapper;
import com.dadalong.autotest.bean.v1.mapper.UserMapper;
import com.dadalong.autotest.bean.v1.pojo.Api;
import com.dadalong.autotest.bean.v1.pojo.TestCase;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.TestCaseWrapper;
import com.dadalong.autotest.model.api.*;
import com.dadalong.autotest.model.other.CaseRules;
import com.dadalong.autotest.model.response.*;
import com.dadalong.autotest.service.IApiService;
import com.dadalong.autotest.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

@Service
@Transactional
public class ApiServiceImpl extends ServiceImpl<ApiMapper,Api> implements IApiService {

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private TestCaseMapper testCaseMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Resource
    DeleteFileUtils deleteFileUtils;

    @Resource
    RandomUtils randomUtils;

    public IPage<ApiListResponse> listWithSearch(SearchRequest searchRequest){
        try {
            ApiWrapper wrapper = new ApiWrapper();
            wrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<ApiListResponse> apiListResponseList = new ArrayList<>();
            Map<String,Object> map = searchRequest.getSearch();

            SlabPage<Api> apiSlabPage = new SlabPage<>(searchRequest);
            IPage<Api> apiResults = apiMapper.selectPage(apiSlabPage, wrapper);
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
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public String createOrEditApi(CreateOrEditApiDTO createOrEditApiDTO) {
        Api api = new Api();
        BeanUtils.copyProperties(createOrEditApiDTO, api, "userId");

        if(createOrEditApiDTO.getId() == null || createOrEditApiDTO.getId().toString().equals("")) {
            api.setUserId(createOrEditApiDTO.getUserId());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APICREATE, OperatePathEnumUtils.APICREATE);
            apiMapper.insert(api);
            return "创建成功";
        } else {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditApiDTO.getUserId(), LogContentEnumUtils.APIEDIT, OperatePathEnumUtils.APIEDIT);
            apiMapper.updateById(api);
            return "编辑成功";
        }
    }

    @Override
    public void deleteBatch(BatchDTO batchDTO) {
        try {
            removeByIds(batchDTO.getApiIds());
            for (Integer apiId : batchDTO.getApiIds()) {
                List<TestCase> testCaseList = testCaseMapper.selectList(new TestCaseWrapper().eq("api_id", apiId));
                for (TestCase testCase : testCaseList) {
                    testCaseMapper.deleteById(testCase.getId());
                }
            }
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.APIDELETE, OperatePathEnumUtils.APIDELETE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
            return "导入失败，请选择文件";
        }
        String fileName = uploadDTO.getFile().getOriginalFilename();
        String filePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi\\";
        File dest = new File(filePath + fileName);
        try {
            uploadDTO.getFile().transferTo(dest);
            String[] strArray = new String[0];
            if (fileName != null) {
                strArray = fileName.split("\\.");
            }
            int suffixIndex = strArray.length -1;
            //获取上传文件的文件类型
            String fileType = strArray[suffixIndex];
            if (fileType.equals("html")) {
                String[] argument = new String[]{"python", "D://Workspace/IDEA/AutoTest/src/main/resources/static/pyToSql/analysis_html.py", uploadDTO.getUserId().toString(), uploadDTO.getBaseUrl()};
                try{
                    Process process = Runtime.getRuntime().exec(argument);
                    //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
                    //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
                    int re = process.waitFor();
//                    System.out.println("python运行的返回值："+re);
                    //导入成功后删除已保存的api.html文件
                    if (re == 0) {
                        String deletePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi";
                        if (!deleteFileUtils.delAllFile(deletePath)) {
                            //插入操作日志
                            insertOperateLogUtils.insertOperateLog(uploadDTO.getUserId(), LogContentEnumUtils.APIIMPORT, OperatePathEnumUtils.APIIMPORT);
                            return "批量导入成功";
                        } else {
                            return "failed";
                        }
                    } else {
                        return "批量导入失败";
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else if (fileType.equals("xlsx")){
//                System.out.println("上传的是xlsx");
                ReadExcelUtils readExcelUtils = new ReadExcelUtils("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi\\api.xlsx");
                try {
                    Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
                    for (Integer key : map.keySet()) {
                        Map<Integer, Object> line = map.get(key);
//                        System.out.println(line);
                        Api api = new Api();
                        api.setBaseUrl(uploadDTO.getBaseUrl());
                        api.setProjectName(line.get(0).toString());
                        api.setApiGroup(line.get(1).toString());
                        api.setApiName(line.get(2).toString());
                        api.setApiPath(line.get(3).toString());
                        api.setReqMethod(line.get(4).toString());
                        api.setApiDescription(line.get(5).toString());
                        api.setReqHeaders(line.get(6).toString());
                        api.setReqQuery(line.get(7).toString());
                        api.setReqBody(line.get(8).toString());
                        api.setCaseRules(line.get(9).toString());
                        api.setApiResponse(line.get(10).toString());
                        api.setUserId(uploadDTO.getUserId());
                        apiMapper.insert(api);
                    }
                    String deletePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi";
                    if (!deleteFileUtils.delAllFile(deletePath)) {
                        //插入操作日志
                        insertOperateLogUtils.insertOperateLog(uploadDTO.getUserId(), LogContentEnumUtils.APIIMPORT, OperatePathEnumUtils.APIIMPORT);
                        return "批量导入成功";
                    } else {
                        return "failed";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "批量导入失败";
    }

    @Override
    public Boolean putCaseRules(CaseRulesDTO caseRulesDTO) {
        Api api = apiMapper.selectById(caseRulesDTO.getApiId());
        JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(caseRulesDTO.getCaseRulesList()));
        api.setCaseRules(jsonArray.toString());
        apiMapper.updateById(api);
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(caseRulesDTO.getUserId(), LogContentEnumUtils.CASERULES, OperatePathEnumUtils.CASERULES);
        return true;
    }

    @Override
    public Boolean createCases(CreateCasesDTO createCasesDTO) {
        Api api = apiMapper.selectById(createCasesDTO.getApiId());
        if (api.getCaseRules() != null && StringUtils.isNotBlank(api.getCaseRules())) {
            JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(api.getCaseRules()));
            String jsonString = JSONObject.toJSONString(jsonArray);
            List<CaseRules> caseRulesList = JSONObject.parseArray(jsonString, CaseRules.class);
            System.out.println(caseRulesList);
        }
//        String[] argument = new String[]{"python", "D://Workspace/IDEA/AutoTest/src/main/resources/static/pyToSql/pairwise.py", createCasesDTO.getUserId().toString(), uploadDTO.getBaseUrl()};
//        try{
//            Process process = Runtime.getRuntime().exec(argument);
//
//            InputStreamReader ir = new InputStreamReader(process.getInputStream());
//            LineNumberReader input = new LineNumberReader(ir);
//            String result = input.readLine();
//            input.close();
//            ir.close();
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            while ((in.readLine()) != null) {
//                String line = in.readLine();
//                System.out.println(line);
//            }
//            in.close();
//            process.waitFor();
//            int re = process.waitFor();
//            System.out.println("python运行的返回值："+re);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        //插入操作日志
//        insertOperateLogUtils.insertOperateLog(createCasesDTO.getUserId(), LogContentEnumUtils.CREATECASES, OperatePathEnumUtils.CREATECASES);
        return null;
    }

}
