package com.onecoderspace.base.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.onecoderspace.base.domain.Permission;
import com.onecoderspace.base.domain.Role;
import com.onecoderspace.base.domain.User;
import com.onecoderspace.base.service.RolePermissionService;
import com.onecoderspace.base.service.RoleService;
import com.onecoderspace.base.service.UserRoleService;
import com.onecoderspace.base.service.UserService;


public class MyShiroRealm extends AuthorizingRealm {
	private static final Logger logger = LoggerFactory.getLogger(MyShiroRealm.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private RolePermissionService rolePermissionService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {


        //获取用户的输入的账号.
        String idObj = (String) token.getPrincipal();
        Integer id = NumberUtils.toInt(idObj);
        User user = userService.findById(id);

        if (user == null) {
            // 返回null的话，就会导致任何用户访问被拦截的请求时，都会自动跳转到unauthorizedUrl指定的地址
            return null;
        }

        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user.getId(),
                user.getPwd(), getName());

        return authenticationInfo;

    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        	/*
		 * 当没有使用缓存的时候，不断刷新页面的话，这个代码会不断执行，
		 * 当其实没有必要每次都重新设置权限信息，所以我们需要放到缓存中进行管理；
		 * 当放到缓存中时，这样的话，doGetAuthorizationInfo就只会执行一次了，
		 * 缓存过期之后会再次执行。
		 */
        logger.debug("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.addRole("ACTUATOR");

        Integer userId = Integer.parseInt(principals.getPrimaryPrincipal().toString());
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法

        Set<Integer> roleIds = userRoleService.findRoleIds(userId);
        Set<Role> roles = roleService.findByIds(roleIds);
        for(Role role : roles){
        	authorizationInfo.addRole(role.getCode());
        }

        //设置权限信息.
        List<Permission> permissions = rolePermissionService.getPermissions(roleIds);
        Set<String> set = new HashSet<String>(permissions.size()*2);
        for(Permission permission : permissions){
        	if(StringUtils.isNotBlank(permission.getCode())){
        		set.add(permission.getCode());
        	}
        }
        authorizationInfo.setStringPermissions(set);
        return authorizationInfo;
    }

}
