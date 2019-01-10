package com.espay.mapper.one;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.espay.pojo.entity.FrspUser;
import org.apache.ibatis.annotations.Param;

public interface FrspUserOneMapper extends BaseMapper<FrspUser> {

    int checkAccount(@Param("account") String account);

    FrspUser findUserByNameAndPassword(@Param("account") String account, @Param("password") String password);

    Integer addUser(@Param("account") String account,@Param("password") String password,@Param("username") String username,@Param("sex") int sex,@Param("mobile") String mobile,@Param("email") String email);

}
