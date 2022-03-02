package com.xxx.crm.service;

import com.github.pagehelper.util.StringUtil;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.dao.UserMapper;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.Md5Util;
import com.xxx.crm.utils.UserIDBase64;
import com.xxx.crm.vo.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class UserService extends BaseService<User,Integer> {

    @Resource
    private UserMapper userMapper;

    /**
     * Service层 （业务逻辑层：非空判、条件判断等业务逻辑处理）
     *         1.参数判断，判断用户姓名、用户密码非空
     * 如果参数为空是，抛出异常（异常被控制层捕获或处理）
     *         2.调用数据访问层，通过用户名查询用户访问记录，返回用户访问对象
     *             3.判断用户对象是否为空
     * 如果对象为空，抛出异常（异常被控制层捕获并处理）
     *         4.判断密码是否正确，比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码是否，
     * 如果密码不相等，抛出异常（异常被控制层捕获并处理）
     *         5.如果密码正确，登录成功
     */

    public UserModel userLogin(String userName,String userPwd) {
        /*1.参数判断，判断用户姓名、用户密码非空*/
        checkLoginParams(userName,userPwd);
        //2.调用数据访问层，通过用户名查询用户访问记录，返回用户访问对象

        User user = userMapper.queryUserByName(userName);

        //3.判断用户对象是否为空

        AssertUtil.isTrue(user == null,"用户不存在，或者已经被注销！");

        //4.判断密码是否正确,比较客户端传递的用户密码与数据库中查询的用户对象中的用户密码是否相等

        checkUserPwd(userPwd,user.getUserPwd());

        //5.如果密码正确，登录成功,返回构建用户对象
        return buildUserInfo(user);

    }


    /**
     *  修改密码
     *
     *  1、接收4个参数（用户ID，原始密码，新密码，确认密码）
     *  2、通过ID查询用户记录返回用户对象
     *  3、参数校验
     *      待更新用户记录是否存在 （用户对象是否为空）
     *      判断原始密码是否为空
     *      判断原始密码是否正确 （查询的用户对象中的用户密码是否原始密码一致）
     *      判断新密码是否为为空
     *      判断新密码与原始密码是否一致 （不允许新密码与原始密码一致）
     *      判断确认密码是否为空
     *      判断确认密码是否与新密码一致
     *  4、设置用户的新密码
     *      需要将新密码通过指定算法进行加密 （md5加密）
     *  5、执行更新操作，判断受影响的行数
     *
     * */

    @Transactional(propagation = Propagation.REQUIRED)
    public void updatePassWord(Integer userId,String oldPwd,String newPwd,String resetPwd){
        //1、通过ID查询用户记录返回用户对象
        User user = userMapper.selectByPrimaryKey(userId);

        //2、判断用户记录是否存在
        AssertUtil.isTrue( user == null ,"待更新用户记录不存在");

        //3、参数校验
        checkUpdatePwd(user,oldPwd,newPwd,resetPwd);

        //4、设置用户的新密码
        user.setUserPwd(Md5Util.encode(newPwd));

        //5、执行更新操作，判断受影响的行数
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user) < 1,"更新密码失败！");


    }

    private void checkUpdatePwd(User user, String oldPwd, String newPwd, String resetPwd) {

        //1、判断原始密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原始密码为空！");

        //2、判断原始密码是否正确 （查询的用户对象中的用户密码是否原始密码一致）
        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(oldPwd)),"原始密码不正确！");

        //3.判断新密码是否为为空
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码为空！");

        //4，判断新密码与原始密码是否一致 （不允许新密码与原始密码一致）
        AssertUtil.isTrue(newPwd.equals(oldPwd),"新密码与原始密码一致，请重新输入");

        //5.判断确认密码是否为空
        AssertUtil.isTrue(StringUtils.isBlank(resetPwd),"请输入确认密码");

        //6.判断确认密码是否与新密码一致
        AssertUtil.isTrue(!resetPwd.equals(newPwd),"确认密码与新密码不一致，请重新输入");

    }


    /**
     * 构建需要返回给客户端的用户对象
     * @param user
     */
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        //userModel.setUserId(user.getId());
        //设置加密的用户ID
        userModel.setUserIdStr(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    /**
     * 密码判断
     * 先将客户端传递的密码加密,再与数据库中查询的密码做比较
     * @param userPwd
     * @param pwd
     */
    private void checkUserPwd(String userPwd, String pwd) {
        //将客户端传递的密码加密
        userPwd = Md5Util.encode(userPwd);
        // 判断密码是否相等
        AssertUtil.isTrue(!userPwd.equals(pwd),"用户名密码不正确，请重新输入！");

    }

    /**
     *
     * @param userName
     * @param userPwd
     * 参数判断
     */
    private void checkLoginParams(String userName, String userPwd) {
        //验证用户姓名
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空！");
        //验证用户密码
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户名密码不能为空！");
    }

}
