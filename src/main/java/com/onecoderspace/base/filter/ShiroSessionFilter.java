package com.onecoderspace.base.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
  
/** 
 * 通过拦截器设置shiroSession过期时间
 * @author yangwk 
 */  
public class ShiroSessionFilter implements Filter {  
	private static Logger logger = LoggerFactory.getLogger(ShiroSessionFilter.class);
	
	public List<String> excludes = new ArrayList<String>();
	
	private long serverSessionTimeout = 180000L;//ms
  
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,ServletException {  
    	if(logger.isDebugEnabled()){
  			logger.debug("shiro session filter is open");
  		}
  		
  		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;
  		if(handleExcludeURL(req, resp)){
  			filterChain.doFilter(request, response);
			return;
		}
  		
  		Subject currentUser = SecurityUtils.getSubject();
  		if(currentUser.isAuthenticated()){
  			currentUser.getSession().setTimeout(serverSessionTimeout);
  		}
  		filterChain.doFilter(request, response);
    }
    
    private boolean handleExcludeURL(HttpServletRequest request, HttpServletResponse response) {

		if (excludes == null || excludes.isEmpty()) {
			return false;
		}

		String url = request.getServletPath();
		for (String pattern : excludes) {
			Pattern p = Pattern.compile("^" + pattern);
			Matcher m = p.matcher(url);
			if (m.find()) {
				return true;
			}
		}

		return false;
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		if(logger.isDebugEnabled()){
			logger.debug("shiro session filter init~~~~~~~~~~~~");
		}
		String temp = filterConfig.getInitParameter("excludes");
		if (temp != null) {
			String[] url = temp.split(",");
			for (int i = 0; url != null && i < url.length; i++) {
				excludes.add(url[i]);
			}
		}
		String timeout = filterConfig.getInitParameter("serverSessionTimeout");
		if(StringUtils.isNotBlank(timeout)){
			this.serverSessionTimeout = NumberUtils.toLong(timeout,1800L)*1000L;
		}
	}

	@Override
	public void destroy() {}  
  
}  
