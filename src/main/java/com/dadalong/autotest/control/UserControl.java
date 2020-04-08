package com.dadalong.autotest.control;


import com.dadalong.autotest.model.user.CreateUserDTO;
import com.dadalong.autotest.model.user.BatchDTO;
import com.dadalong.autotest.service.IUserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
        iUserService.deleteBatch(batchDTO.getUserNumbers());
        return "删除成功";
    }

    @PostMapping("/disable")
    public String disableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.disableBatch(batchDTO.getUserNumbers());
        return "禁用成功";
    }

    @PostMapping("/enable")
    public String enableBatch(@RequestBody BatchDTO batchDTO){
        iUserService.enableBatch(batchDTO.getUserNumbers());
        return "恢复成功";
    }

    @GetMapping("/filter-role")
    public @ResponseBody List filterRole(String role){
        return iUserService.filterRole(role);
    }

    @GetMapping("/filter-name")
    public @ResponseBody List searchByName(String name){
        return iUserService.searchByName(name);
    }


}
