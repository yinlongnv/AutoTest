package com.dadalong.autotest.service.impl;

import cn.com.dbapp.slab.common.model.dto.SearchRequest;
import cn.com.dbapp.slab.common.model.dto.SlabPage;
import cn.com.dbapp.slab.java.commons.exceptions.ConflictException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dadalong.autotest.bean.v1.mapper.*;
import com.dadalong.autotest.bean.v1.pojo.*;
import com.dadalong.autotest.bean.v1.wrapper.ApiWrapper;
import com.dadalong.autotest.bean.v1.wrapper.TestCaseWrapper;
import com.dadalong.autotest.bean.v1.wrapper.UserWrapper;
import com.dadalong.autotest.model.response.TestCaseListResponse;
import com.dadalong.autotest.model.testCase.*;
import com.dadalong.autotest.service.ITestCaseService;
import com.dadalong.autotest.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by 78089 on 2020/4/24.
 */
@Service
@Transactional
public class TestCaseServiceImpl extends ServiceImpl<TestCaseMapper, TestCase> implements ITestCaseService {

    @Resource
    private TestCaseMapper testCaseMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private ApiMapper apiMapper;

    @Resource
    private NoticeMapper noticeMapper;

    @Resource
    private NoticeUsersMapper noticeUsersMapper;

    @Resource
    InsertOperateLogUtils insertOperateLogUtils;

    @Resource
    TestCaseUtils testCaseUtils;

    @Resource
    HandleFileUtils handleFileUtils;

    @Override
    public IPage<TestCaseListResponse> listWithSearch(SearchRequest searchRequest) {
        try {
            TestCaseWrapper testCaseWrapper = new TestCaseWrapper();
            ApiWrapper apiWrapper = new ApiWrapper();

            Map<String,Object> map = searchRequest.getSearch();
            if (StringUtils.isNotBlank(map.get("projectName").toString())) {
                Api a = new Api();
                a.setProjectName(map.get("projectName").toString());
                a.setApiGroup(map.get("apiGroup").toString());
                String apiInfo[] = map.get("apiMerge").toString().split(" ");
                a.setApiName(apiInfo[0]);
                a.setApiPath(apiInfo[1]);
                apiWrapper.ofApiId(a);
                Integer apiId = apiMapper.selectOne(apiWrapper).getId();
                searchRequest.setSearch("apiId", apiId);
            }
            testCaseWrapper.ofListWithSearch(searchRequest).orderByDesc("created_at");
            List<TestCaseListResponse> testCaseListResponseList = new ArrayList<>();

            SlabPage<TestCase> testCaseSlabPage = new SlabPage<>(searchRequest);
            IPage<TestCase> testCaseResults = testCaseMapper.selectPage(testCaseSlabPage,testCaseWrapper);
            for (TestCase record : testCaseResults.getRecords()) {
                Api api = apiMapper.selectById(record.getApiId());
                TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
                BeanUtils.copyProperties(api, testCaseListResponse, "id");
                testCaseListResponse.setApiId(api.getId());
                String merge = api.getApiName() + " " + api.getApiPath();
                testCaseListResponse.setApiMerge(merge);
                BeanUtils.copyProperties(record, testCaseListResponse);
                User createdBy = userMapper.selectById(record.getUserId());
                User username = userMapper.selectById(record.getExecuteByUserId());
                if(createdBy != null && StringUtils.isNotBlank(createdBy.toString())) {
                    testCaseListResponse.setCreatedBy(createdBy.getUsername());
                } else {
                    testCaseListResponse.setCreatedBy("root");
                }
                if(username != null && StringUtils.isNotBlank(username.toString())) {
                    testCaseListResponse.setUsername(username.getUsername());
                } else {
                    testCaseListResponse.setUsername("root");
                }
                testCaseListResponse.setLastExecuteTime(record.getUpdatedAt());
                testCaseListResponseList.add(testCaseListResponse);
            }
            SlabPage<TestCaseListResponse> testCaseListResponseSlabPage = new SlabPage<>(searchRequest);
            testCaseListResponseSlabPage.setRecords(testCaseListResponseList);
            testCaseListResponseSlabPage.setTotal(testCaseResults.getTotal());
            Object userId = map.get("userId");
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(Integer.valueOf(String.valueOf(userId)), LogContentEnumUtils.CASELIST, OperatePathEnumUtils.CASELIST);
            return testCaseListResponseSlabPage;
        }catch (Exception e){
            throw new ConflictException("listWithSearchError");
        }
    }

    @Override
    public void createOrEditCase(CreateOrEditCaseDTO createOrEditCaseDTO) {
        TestCase testCase = new TestCase();
        BeanUtils.copyProperties(createOrEditCaseDTO, testCase, "userId");
        Integer apiId = testCaseUtils.getApiId(createOrEditCaseDTO.getProjectName(), createOrEditCaseDTO.getApiGroup(), createOrEditCaseDTO.getApiMerge());
        testCase.setApiId(apiId);
        if(createOrEditCaseDTO.getId() == null) {
            testCase.setExecuteStatus(0);
            testCase.setUserId(createOrEditCaseDTO.getUserId());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditCaseDTO.getUserId(), LogContentEnumUtils.CASECREATE, OperatePathEnumUtils.CASECREATE);
            testCaseMapper.insert(testCase);
        } else {
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(createOrEditCaseDTO.getUserId(), LogContentEnumUtils.CASEEDIT, OperatePathEnumUtils.CASEEDIT);
            testCaseMapper.updateById(testCase);
        }
    }

    @Override
    public void deleteBatch(BatchDTO batchDTO) {
        try {
            removeByIds(batchDTO.getCaseIds());
            //插入操作日志
            insertOperateLogUtils.insertOperateLog(batchDTO.getUserId(), LogContentEnumUtils.CASEDELETE, OperatePathEnumUtils.CASEDELETE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TestCaseListResponse detail(DetailDTO detailDTO) {
        TestCaseListResponse testCaseListResponse = new TestCaseListResponse();
        TestCase testCase = testCaseMapper.selectById(detailDTO.getId());
        BeanUtils.copyProperties(testCase, testCaseListResponse);
        Api api = apiMapper.selectById(testCase.getApiId());
        BeanUtils.copyProperties(api, testCaseListResponse, "id");
        String merge = api.getApiName() + " " + api.getApiPath();
        testCaseListResponse.setApiMerge(merge);
        User createdBy = userMapper.selectById(testCase.getUserId());
        User username = userMapper.selectById(testCase.getExecuteByUserId());
        if(createdBy != null && StringUtils.isNotBlank(createdBy.toString())) {
            testCaseListResponse.setCreatedBy(createdBy.getUsername());
        } else {
            testCaseListResponse.setCreatedBy("root");
        }
        if(username != null && StringUtils.isNotBlank(username.toString())) {
            testCaseListResponse.setUsername(username.getUsername());
        } else {
            testCaseListResponse.setUsername("root");
        }
        testCaseListResponse.setLastExecuteTime(testCase.getUpdatedAt());
        //插入操作日志
        insertOperateLogUtils.insertOperateLog(detailDTO.getUserId(), LogContentEnumUtils.CASEDETAIL, OperatePathEnumUtils.CASEDETAIL);
        return testCaseListResponse;
    }

    @Override
    public String upload(UploadDTO uploadDTO) {
        if (uploadDTO.getFile().isEmpty()) {
            return "导入失败，请选择文件";
        }
        String fileName = uploadDTO.getFile().getOriginalFilename();
        String filePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadCase\\";
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

            Integer apiId = testCaseUtils.getApiId(uploadDTO.getProjectName(), uploadDTO.getApiGroup(), uploadDTO.getApiMerge());

            if (fileType.equals("xlsx")) {
//                System.out.println("上传的是xlsx");
                ReadExcelUtils readExcelUtils = new ReadExcelUtils("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadCase\\case.xlsx");
                try {
                    Map<Integer, Map<Integer, Object>> map = readExcelUtils.readExcelContent();
                    for (Integer key : map.keySet()) {
                        Map<Integer, Object> line = map.get(key);
//                        System.out.println(line);
                        TestCase testCase = new TestCase();
                        testCase.setCaseBody(line.get(0).toString());
                        testCase.setCaseDescription(line.get(1).toString());
                        testCase.setCaseResponse(line.get(2).toString());
                        testCase.setApiId(apiId);
                        testCase.setUserId(uploadDTO.getUserId());
                        testCase.setExecuteStatus(0);
                        testCaseMapper.insert(testCase);
                    }
                    String deletePath = "D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\uploadCase";
                    if (!handleFileUtils.delAllFile(deletePath)) {
                        //插入操作日志
                        insertOperateLogUtils.insertOperateLog(uploadDTO.getUserId(), LogContentEnumUtils.CASEIMPORT, OperatePathEnumUtils.CASEIMPORT);
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
    public Boolean execute(ExecuteDTO executeDTO) {
        TestCase testCase = testCaseMapper.selectById(executeDTO.getCaseId());
        Api api = apiMapper.selectById(testCase.getApiId());
        XmlSuite suite = new XmlSuite();
        suite.setName("演训产品中心API接口测试套件");
        XmlTest test = new XmlTest(suite);
        test.setName("演训产品中心API接口测试用例");
        List<XmlClass> classes = new ArrayList<>();
        XmlClass xmlClass = new XmlClass("com.dadalong.autotest.testng.TestCases");
        if (api.getReqMethod().equals("POST")) {
            String postUrl = api.getBaseUrl() + api.getApiPath();
            String postData = testCase.getCaseBody();
            String getUrl = "http://localhost:9001/api/filterMap";
            Map<String, String> map = new HashMap<>();
            map.put("postUrl", postUrl);
            map.put("postData", postData);
            map.put("getUrl", getUrl);
            xmlClass.setParameters(map);
        } else if (api.getReqMethod().equals("GET")){
            String caseBody = testCase.getCaseBody();
            String postData = "{\"username\":\"大大龙\",\"password\":\"123456\",\"lastIp\":\"127.0.0.1\"}";
            String postUrl = "http://localhost:9001/user/login";
            if (caseBody != null && StringUtils.isNotBlank(caseBody)) {
                JSONObject jsonObject = JSONObject.parseObject(caseBody);
                String params = "";
                for(String key : jsonObject.keySet()){
                    params = key + "=" + jsonObject.get(key).toString() + "&";
                }
                System.out.println(params);
                params = params.substring(0, params.length() - 1);// 去除字符串最后一个字符
                String getUrl = api.getBaseUrl() + api.getApiPath() + "?" + params;
                System.out.println(getUrl);
                Map<String, String> map = new HashMap<>();
                map.put("getUrl", getUrl);
                map.put("postUrl", postUrl);
                map.put("postData", postData);
                xmlClass.setParameters(map);
            } else {
                String getUrl = api.getBaseUrl() + api.getApiPath();
                Map<String, String> map = new HashMap<>();
                map.put("getUrl", getUrl);
                map.put("postUrl", postUrl);
                map.put("postData", postData);
                xmlClass.setParameters(map);
            }
        }
        classes.add(xmlClass);
        test.setXmlClasses(classes);
        suite.addListener("com.dadalong.autotest.utils.ExtentTestNGIReportListenerUtils");
        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        suites.add(suite);
        TestNG testNG = new TestNG();
        testNG.setXmlSuites(suites);
        testNG.run();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //延迟10000ms后执行以下语句
                String newestFile = handleFileUtils.findNewFile("D:\\Workspace\\IDEA\\AutoTest\\src\\main\\resources\\static\\");
                testCase.setHtmlUrl(newestFile);
                testCase.setExecuteByUserId(executeDTO.getUserId());
                testCase.setExecuteStatus(1);
                testCase.setExecuteCount(testCase.getExecuteCount()+1);
                testCaseMapper.updateById(testCase);// 更新该条被执行用例的数据信息
                Notice notice = new Notice();
                notice.setCaseId(executeDTO.getCaseId());
                notice.setCreatorId(executeDTO.getUserId());
                notice.setApiId(api.getId());
                notice.setHtmlUrl(newestFile);
                noticeMapper.insert(notice);// 每当用例执行完产生一条公告
                UserWrapper userWrapper = new UserWrapper();
                List<User> userList = userMapper.selectList(userWrapper.select("id"));
                for (User user : userList) {
                    NoticeUsers noticeUsers = new NoticeUsers();
                    noticeUsers.setIsRead("0");
                    noticeUsers.setNoticeId(notice.getId());
                    noticeUsers.setUserId(user.getId());
                    noticeUsersMapper.insert(noticeUsers);
                }// 创建该公告与所有用户的公告用户关联表
            } }, 10000);
        // 插入操作日志
        insertOperateLogUtils.insertOperateLog(executeDTO.getUserId(), LogContentEnumUtils.CASEEXECUTE, OperatePathEnumUtils.CASEEXECUTE);
        return true;
//        XmlSuite suite = new XmlSuite();
//        suite.setName("演训产品中心API接口测试套件");
//        XmlTest test = new XmlTest(suite);
//        test.setName("演训产品中心API接口测试用例");
//        List<XmlClass> classes = new ArrayList<>();
//        XmlClass xmlClass = new XmlClass("com.dadalong.autotest.testng.TestCases");
//        String postData = "{\"username\":\"大大龙\",\"password\":\"123456\",\"lastIp\":\"127.0.0.1\"}";
//        String postUrl = "http://localhost:9001/user/login";
//        Map<String, String> map = new HashMap<>();
//        map.put("postUrl", postUrl);
//        map.put("postData", postData);
//        xmlClass.setParameters(map);
//        classes.add(xmlClass);
//        test.setXmlClasses(classes);
//        suite.addListener("com.dadalong.autotest.utils.ExtentTestNGIReportListenerUtils");
//        List<XmlSuite> suites = new ArrayList<XmlSuite>();
//        suites.add(suite);
//        TestNG testNG = new TestNG();
//        testNG.setXmlSuites(suites);
//        testNG.run();
    }

}
