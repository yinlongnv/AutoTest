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
import org.springframework.beans.factory.annotation.Value;
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
    HandleFileUtils handleFileUtils;

    @Resource
    RandomUtils randomUtils;

    @Resource
    PutListToTextUtils putListToTextUtils;

    @Value("${upload-api-path}")
    String uploadApiPath;

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
            insertOperateLogUtils.insertOperateLog(Integer.valueOf(String.valueOf(userId)), LogContentEnumUtils.APILIST, OperatePathEnumUtils.APILIST);
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
            // 把这批要被批量删除的api下的用例也删了，清除数据冗余带来的异常
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
    public String upload(UploadDTO uploadDTO) throws IOException{
        if (uploadDTO.getFile().isEmpty()) {
            return "导入失败，请选择文件";
        }
        String fileName = uploadDTO.getFile().getOriginalFilename();
        File dest = new File(uploadApiPath + fileName);
        uploadDTO.getFile().transferTo(dest);
        String fileType = handleFileUtils.getFileType(fileName);
        if (fileType.equals("html")) {
            String[] argument = new String[]{"python", "D://Workspace/IDEA/AutoTest/src/main/resources/static/pyToSql/analysis_html.py", uploadDTO.getUserId().toString(), uploadDTO.getBaseUrl()};
            try{
                Process process = Runtime.getRuntime().exec(argument);
                //java代码中的process.waitFor()返回值为0表示我们调用python脚本成功，
                //返回值为1表示调用python脚本失败，这和我们通常意义上见到的0与1定义正好相反
                int re = process.waitFor();
                //导入成功后删除已保存的api.html文件
                if (re == 0) {
                    if (!handleFileUtils.delAllFile(uploadApiPath)) {
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
            ReadExcelUtils readExcelUtils = new ReadExcelUtils("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadApi\\api.xlsx");
            try {
                Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
                for (Integer key : map.keySet()) {
                    Map<Integer, Object> line = map.get(key);
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
                if (!handleFileUtils.delAllFile(uploadApiPath)) {
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
        return "批量导入失败";
    }

    @Override
    public Boolean putCaseRules(CaseRulesDTO caseRulesDTO) {
        Api api = apiMapper.selectById(caseRulesDTO.getApiId());
        JSONArray jsonArray= JSONArray.parseArray(JSON.toJSONString(caseRulesDTO.getCaseRulesList()));
        api.setCaseRules(jsonArray.toString());
        apiMapper.updateById(api);
        // 插入操作日志
        insertOperateLogUtils.insertOperateLog(caseRulesDTO.getUserId(), LogContentEnumUtils.CASERULES, OperatePathEnumUtils.CASERULES);
        // 执行自动生成测试用例
        createCases(caseRulesDTO);
        return true;
    }

    // 生成测试用例
    public Boolean createCases(CaseRulesDTO caseRulesDTO) {
        List<List<String>> params = new ArrayList<>();
        List<String> keys = new ArrayList<>();
        // 根据参数规则随机生成测试数据
        for (CaseRules caseRules : caseRulesDTO.getCaseRulesList()) {
            keys.add(caseRules.getName());
            if (caseRules.getMin()!=null||caseRules.getMax()!=null) {
                if (caseRules.getMin() == null||caseRules.getMin() == "") {
                    caseRules.setMin("1");
                }
                if (caseRules.getMax() == null||caseRules.getMax() == "") {
                    caseRules.setMax("999");
                }
                if (caseRules.getType().equals("int")) {
                    List<String> integers = randomUtils.getIntegerRandom(Integer.parseInt(caseRules.getMin()), Integer.parseInt(caseRules.getMax()));
                    params.add(integers);
                } else if (caseRules.getType().equals("string")) {
                    List<String> strings;
                    if (caseRules.getName().contains("password")) {
                        strings = randomUtils.getPasswordRandom(Integer.parseInt(caseRules.getMin()), Integer.parseInt(caseRules.getMax()));
                    } else {
                        strings = randomUtils.getStringOrOtherRandom(Integer.parseInt(caseRules.getMin()), Integer.parseInt(caseRules.getMax()));
                    }
                    params.add(strings);
                }
            } else if (caseRules.getOptions()!=null) {
                List<String> strings = new ArrayList<>();
                String [] strArr= caseRules.getOptions().split(",");
                strings.addAll(Arrays.asList(strArr));
                params.add(strings);
            } else if (caseRules.getModel()!=null) {
                List<String> models = new ArrayList<>();
                if (caseRules.getModel().equals("phone")) {
                    models.add(randomUtils.getPhoneRandom());
                } else if (caseRules.getModel().equals("email")) {
                    models.add(randomUtils.getEmailRandom());
                } else if (caseRules.getModel().equals("idNumber")) {
                    models.add(randomUtils.getIdNumberRandom());
                }
                params.add(models);
            }
        }
        // 给参数值打标，int型为true，反之false
        Map<String,Boolean> judgeInt = new HashMap<>();
        for (CaseRules caseRules : caseRulesDTO.getCaseRulesList()) {
            if("int".equals(caseRules.getType())){
                judgeInt.put(caseRules.getName(),true);
            }else{
                judgeInt.put(caseRules.getName(),false);
            }
        }

        // 写到txt中传给py做处理
        try {
            putListToTextUtils.putKeysToText(keys);
            putListToTextUtils.putParamsToText(params);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // pairwise生成测试用例
        String[] argument = new String[]{"python", "D://Workspace/IDEA/AutoTest/src/main/resources/static/pyToSql/pairwise.py", "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\pyToSql\\params.txt"};
        try{
            Process process = Runtime.getRuntime().exec(argument);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String result = input.readLine();

            String[] sp = result.split(";");
            String[] wp = Arrays.copyOfRange(sp,1,sp.length);// 除去切分时候的一个空格
            List<JSONObject> jsonObjectList = new ArrayList<>();
            for (String s : wp) {
                JSONObject jon = JSONObject.parseObject(s);
                for(String key : jon.keySet()){
                    if(judgeInt.get(key)){
                        int num = Integer.valueOf(jon.get(key).toString());
                        jon.put(key,num);
                    }
                }
                jsonObjectList.add(jon);
            }
            input.close();
            ir.close();
            int re = process.waitFor();
            // py 执行成功返回0，继续处理存入case表
            if (re == 0) {
                Api api = apiMapper.selectById(caseRulesDTO.getApiId());
                String apiResponse = api.getApiResponse();
                String apiName = api.getApiName();
                int code = 1;
                for (JSONObject jsonObject : jsonObjectList) {
                    TestCase testCase = new TestCase();
                    testCase.setApiId(caseRulesDTO.getApiId());
                    testCase.setUserId(caseRulesDTO.getUserId());
                    testCase.setCaseResponse(apiResponse);
                    testCase.setExecuteStatus(0);
                    String caseDescription = apiName + "case" + code;
                    testCase.setCaseDescription(caseDescription);
                    testCase.setCaseBody(jsonObject.toString());
                    testCaseMapper.insert(testCase);
                    code ++;
                }
                //插入操作日志
                insertOperateLogUtils.insertOperateLog(caseRulesDTO.getUserId(), LogContentEnumUtils.CREATECASES, OperatePathEnumUtils.CREATECASES);
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
