package com.onecoderspace.base.util;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;

public class HttpServletHelper {

    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        
        if(ArrayUtils.isEmpty(cookies)){
            return StringUtils.EMPTY;
        }
        
        for (Cookie cookie : cookies) {
            if(StringUtils.equals(cookie.getName(), key) ){
                return cookie.getValue();
            }
        }
        return StringUtils.EMPTY;
    }
    
    
    public static  void delCookie(HttpServletResponse response,String key){
        Cookie del= new Cookie(key,null);
        del.setMaxAge(0);
        response.addCookie(del);
    }
    
    public static  void delCookie(HttpServletResponse response,String key,String path,String domain){
        Cookie del= new Cookie(key,null);
        del.setMaxAge(0);
        if(StringUtils.isNotBlank(path)){
            del.setPath(path);
        }
        if(StringUtils.isNotBlank(domain)){
            del.setDomain(domain);
        }
        response.addCookie(del);
    }
    
    public static void addCookie(HttpServletResponse response, Cookie cookie){
        response.addCookie(cookie);
    }
    
    
    //Set-Cookie: <name>=<value>[; <Max-Age>=<age>][; expires=<date>][; domain=<domain_name>]=[; path=<some_path>][; secure][; HttpOnly]
    public static void addCookieHttpOnly(HttpServletResponse response, Cookie cookie){
        
       int  maxAge = cookie.getMaxAge();
       if(maxAge==0){
           return;
       }
       StringBuilder sb = new StringBuilder(cookie.getName());
       sb.append("=");
       sb.append(cookie.getValue());
       
       if(maxAge>0){
           sb.append(";").append("Max-Age=").append(cookie.getMaxAge());
       }
       
       if(StringUtils.isNotBlank(cookie.getDomain())){
           sb.append(";").append("Domain=").append(cookie.getDomain());   
       }
       if(StringUtils.isNotBlank(cookie.getPath())){
           sb.append(";").append("Path=").append(cookie.getPath());
       }
       sb.append("; HttpOnly");
        
        String cookieStr = sb.toString();
        response.addHeader("Set-Cookie", cookieStr);
        //response.setHeader("Set-Cookie", cookieStr); //错误用法，其他cookie会被覆盖
    }
    
    
    private static final String SESSION_KEY_CALLBACK = "cb_url";
    public static void setCallBackUrl(HttpServletRequest request){
        
        String callback = getCurrentUrl(request);// eg. /iapp/study/me.do?a=b
        request.getSession().setAttribute(SESSION_KEY_CALLBACK, callback);
    }
    

    public static String getCallBackUrlAndClean(HttpServletRequest request){
        String callback =(String) request.getSession().getAttribute(SESSION_KEY_CALLBACK);
        
        if(StringUtils.isNotBlank(callback)){
            request.getSession().removeAttribute(SESSION_KEY_CALLBACK);
        }
        return callback;
    }
    
    public static String getCurrentUrl(HttpServletRequest request){
        return  Joiner.on("?").skipNulls().join(request.getRequestURL().toString(), request.getQueryString()); // eg. http://xxx:xx/iapp/study/me.do?a=b
        
    }
    
    
    /**
     * 获取跟路径
     * @param request
     * @return 
     */
    public static String getRootPath(HttpServletRequest request){
    	String rootPath = String.format("%s:%s%s", request.getServerName(),request.getServerPort(),request.getContextPath());
    	return rootPath;
    }

    /**
     * 获取绝对路径
     * @param request
     * @param relativePath
     * @return
     */
	public static String getAbsolutePath(HttpServletRequest request,
			String relativePath) {
		return String.format("http://%s/%s", getRootPath(request),relativePath);
	}

	
    private static MapJoiner queryStringMapJoiner = Joiner.on("&").withKeyValueSeparator("=");

    public static String replaceQueryString(String queryString, String key, String value) {
        Map<String, String> myMap = Splitter.on("&").trimResults().withKeyValueSeparator("=").split(queryString);
        myMap = Maps.newHashMap(myMap);
        myMap.put(key, value);
        return queryStringMapJoiner.join(myMap);
    }
    
    public static String removeParamQueryString(String queryString, String key) {
        Map<String, String> myMap = Splitter.on("&").trimResults().withKeyValueSeparator("=").split(queryString);
        myMap = Maps.newHashMap(myMap);
        myMap.remove(key);
        return queryStringMapJoiner.join(myMap);
    }
    
    public static String getRequestUri(){
    	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest(); 
        //获取请求的URL
        String requestUri = request.getRequestURI();
        return requestUri;
    }

}
