package com.securityloginadv.demo.security;

import com.securityloginadv.demo.pojo.SysRole;
import com.securityloginadv.demo.pojo.SysUser;
import com.securityloginadv.demo.service.SysUserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by liuhongdi on 2019/12/9.
*/
@Component("SecUserDetailService")
public class SecUserDetailService implements UserDetailsService{
    @Resource
    private SysUserService sysUserService;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser oneUser = sysUserService.getOneUserByUsername(s);//数据库查询 看用户是否存在
        String encodedPassword = oneUser.getPassword();
        Collection<GrantedAuthority> collection = new ArrayList<>();//权限集合

        List<SysRole> roles = oneUser.getRoles();
        System.out.println(roles);
        for (SysRole roleone : roles) {
            //System.out.println("----roleone.getRoleName:"+roleone.getRoleName());
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_"+roleone.getRoleName());
            collection.add(grantedAuthority);
       }
        SecUser user = new SecUser(s,encodedPassword,collection);
        user.setUserid(oneUser.getUserId());
        user.setNickname(oneUser.getNickName());
        return user;
    }
}