package com.coder.realm;

import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.coder.domain.User;
import com.coder.service.UserService;
import com.coder.util.CipherUtil;

public class ShiroDbRealm extends AuthorizingRealm{

	private static Logger Logger = LoggerFactory.getLogger(ShiroDbRealm.class);
	
	@Autowired
	private UserService userService;
	
	public ShiroDbRealm() {
		super();
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		/*这里应该根据userName使用role和permission 的serive层来做判断，并将对应 的权限加进来，下面简化了这一步*/  
        Set<String> roleNames = new HashSet<String>();  
        Set<String> permissions = new HashSet<String>();  
        roleNames.add("admin");//添加角色。对应到index.jsp  
        roleNames.add("administrator");  
        permissions.add("create");//添加权限,对应到index.jsp  
        permissions.add("login.do?main");  
        permissions.add("login.do?logout");  
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roleNames);  
        info.setStringPermissions(permissions);  
        return info;  
	}

	/**
	 * 登陆验证
	 */
	@SuppressWarnings("static-access")
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		System.out.println("this is shrioDbRealm");
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		System.out.println("token's username:"+token.getUsername());
		User user = null;
		try {
			user = userService.selectUserByUsername(token.getUsername());
		} catch (Exception e) {
			System.out.println("用户名不存在");
			e.printStackTrace();
		}
		CipherUtil cipherUtil = new CipherUtil();
		if (user != null) {
			return new SimpleAuthenticationInfo(user.getUserName(),
					cipherUtil.generatePassword(token.getPassword().toString()),getName());
		} else {
			throw new AuthenticationException();
		}
	}
	

	 /** 
     * 清除所有用户授权信息缓存. 
     */  
    public void clearCachedAuthorizationInfo(String principal) {  
        SimplePrincipalCollection principals = new SimplePrincipalCollection(principal, getName());  
        clearCachedAuthorizationInfo(principals);  
    }
    
    /** 
     * 清除所有用户授权信息缓存. 
     */  
    public void clearAllCachedAuthorizationInfo() {  
        Cache<Object, AuthorizationInfo> cache = getAuthorizationCache();  
        if (cache != null) {  
            for (Object key : cache.keys()) {  
                cache.remove(key);  
            }  
        }  
    }  
}
