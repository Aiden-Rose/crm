package com.xxx.crm.dao;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.vo.User;

public interface UserMapper extends BaseMapper<User,Integer>{
    //根据用户名查询用户对象
     User queryUserByName(String userName);
}