package com.dadalong.autotest.control;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dadalong.autotest.bean.v1.pojo.User;
import com.dadalong.autotest.model.user.*;
import com.dadalong.autotest.service.IUserService;
import com.google.gson.JsonArray;
import org.apache.tomcat.util.http.fileupload.FileItem;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserControl {

    /**
     * 为方便都是如此定义 到时候再进行优化  所有异常判定都没有进行捕获，逻辑存在缺陷
     * 但是正常的CRUD操作没有问题
     */
    private final IUserService iUserService;
    private final HttpServletRequest request;

    public UserControl(IUserService iUserService,HttpServletRequest request){
        this.iUserService = iUserService;
        this.request = request;
    }

    @PostMapping("/create")
    public String create(@RequestBody CreateUserDTO createUserDTO){
        iUserService.addUser(createUserDTO);
        return "创建成功";
    }

    @PostMapping("/delete")
    public String deleteBatch(@RequestBody BatchDTO batchDTO){
        iUserService.deleteBatch(batchDTO.getUserIds());
        return "删除成功";
    }

    @PostMapping("/disable")
    public String disableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO.getUserIds());
        return "禁用成功";
    }

    @PostMapping("/enable")
    public String enableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO.getUserIds());
        return "恢复成功";
    }


    @GetMapping("/list")
    public @ResponseBody Page<User> list(SearchDTO searchDTO){
        Page<User> pages = iUserService.list(searchDTO);
        return pages;
    }

    @PostMapping("/upload")
    public void upload(MultipartFile file) throws IOException {
         iUserService.handleUploadedFile(file);

    }



}
