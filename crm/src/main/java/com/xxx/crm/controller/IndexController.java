package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.vo.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
//@RequestMapping("user")
public class IndexController extends BaseController {

    @Resource
    UserService userService;

    /**
     * 系统登录页
     * @return
     */
    @RequestMapping("index")
    public String index(){
        return "index";
    }
    /**
     * 系统界面欢迎页
     * @return
     */
    @RequestMapping("welcome")
    public String welcome(){
        return "welcome";
    }
    /**
     * 后端管理主页面
     * @return
     */
    @RequestMapping("main")
    public String main(HttpServletRequest request){

        // 获取cookie中的用户Id
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);
        // 查询用户对象，不能用request作用域，因为它是一次请求，跳转其他页面的时候用户名就会消失，应该设置session作用域
        User user = userService.selectByPrimaryKey(userId);
        request.getSession().setAttribute("user",user);

        return "main";
    }

}
