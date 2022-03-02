package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.exceptions.ParamsException;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {
    @Resource
    private UserService userService;


    /**
     * 登录功能
     * @param userName
     * @param userPwd
     * @return
     */
    @PostMapping("login")
    @ResponseBody  //如果返回的是对象,应该加上这个注解,如果返回的页面则可以不加这个注解
    public ResultInfo userLogin(String userName, String userPwd) {
        ResultInfo resultInfo = new ResultInfo();
        //通过try catch捕获service的异常,如果service层抛出异常,则表示登录失败,否则登录成功

        //调用service层登录方法
        UserModel userModel = userService.userLogin(userName, userPwd);

        //设置ResultInfo的result值 (将数据返回给请求)
        resultInfo.setResult(userModel);

       /* try{


        }catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("操作失败！");
        }*/

        return resultInfo;
    }

    /**
     * 用户修改密码
     *
     */
    @PostMapping("updatePwd")
    @ResponseBody
    public ResultInfo updateUserPassword(HttpServletRequest request,String oldPwd,String newPwd,String resetPwd){

        ResultInfo resultInfo = new ResultInfo();

        //获取cookie中的用户ID
        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

        //调用Service层修改密码方法
        userService.updatePassWord(userId,oldPwd,newPwd,resetPwd);

/*        try {

        } catch (ParamsException p){
            resultInfo.setCode(p.getCode());
            resultInfo.setMsg(p.getMsg());
            p.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
            resultInfo.setCode(500);
            resultInfo.setMsg("修改密码失败！");
        }*/

        return resultInfo;
    }


    /**
     * 进入修改密码的页面
     *
     */

    @RequestMapping("toPasswordPage")
    public String toPasswordPage(){
        return "user/password";
    }

}
