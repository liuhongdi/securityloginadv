package com.securityloginadv.demo.mapper;

import com.securityloginadv.demo.pojo.SysRole;
import com.securityloginadv.demo.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface UserMapper {
    public SysUser selectOneUserByUserName(String username);
}