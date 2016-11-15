package com.springmvc.configs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import com.springmvc.models.Authority;
import com.springmvc.models.Resource;
import com.springmvc.models.Role;
import com.springmvc.repositories.ResourceRepository;

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource  {

	@Autowired ResourceRepository resourceRepository;
	
	/**
	 * Get all allowed roles for the url resource.
	 * When return null, the resource will be permit.
	 */
	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		FilterInvocation fi = (FilterInvocation) object;
	    String requestUrl = fi.getRequestUrl();
	    String httpMethod = fi.getRequest().getMethod(); // GET/POST/PUT/PATCH/DELETE
	    
	    // css, js, login等等特殊页面，虽然是允许不登录访问的，但是只是影响AccessDecisionManager最终通过
	 	if (requestUrl.startsWith("/css/") 
	 			|| requestUrl.startsWith("/js/") 
	 			|| requestUrl.startsWith("/images/")
	 			|| "/auth/registration".equals(requestUrl)
	 			|| "/login".equals(requestUrl)
	 			|| "/favicon.ico".equals(requestUrl)) {
	 		return null;
	 	}
	    
	    // in normal case, my project's url like: /controller_request_mapping/method_request_mapping
	    String[] requestUrlParts = requestUrl.split("/");
	    String controllerRequestMappping = "home";
	    if (requestUrlParts.length > 1) {
	    	controllerRequestMappping = requestUrlParts[1];
	    }
	    
	    // lookup the db, and fetch the permit ROLE according the url
	    Iterable<Resource> possibleResources = resourceRepository.findByControllerMappingAndHttpMethod(controllerRequestMappping, httpMethod);
	    Iterator<Resource> possibleResourceIterator = possibleResources.iterator();
	    
	    Collection<ConfigAttribute> configAttrCollection = new  ArrayList<ConfigAttribute>();
		while (possibleResourceIterator.hasNext()) {
			Resource resource = possibleResourceIterator.next();
			
			RequestMatcher requestMatcher = new AntPathRequestMatcher(resource.getUrlPattern(), resource.getHttpMethod());
			if (requestMatcher.matches(fi.getHttpRequest())) {
				
				Authority authority = resource.getAuthority();
				Set<Role> roles = authority.getRoles();
				Iterator<Role> roleIteror = roles.iterator();
				while (roleIteror.hasNext()) {
					Role role = roleIteror.next();
					
					// UserDetailsService的GrantedAuthority使用什么作为role的标识(构造函数中传入)，这里就应该使用什么作为SecurityConfig的构造函数的值
					// 大约原理：这里会把该url资源对应的允许的角色(s)找出来，然后传递到下去，跟UserDetailsService中当前用户对应的角色(s)对比，匹配则允许访问
					ConfigAttribute configAttr = new SecurityConfig(role.getCode().toString()); // WebExpressionConfigAttribute
			        configAttrCollection.add(configAttr);
				}  
			}
	    }
	    
		// RoleVoter will allow non ROLE_ANONYMOUS user access the url if the list size is zero or it is null.
		//ConfigAttribute configAttr = new SecurityConfig("ROLE_HR"); // WebExpressionConfigAttribute
        //configAttrCollection.add(configAttr);
		return configAttrCollection;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return FilterInvocation.class.isAssignableFrom(clazz);
	}

}
