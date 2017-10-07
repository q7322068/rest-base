package com.onecoderspace.base.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;

public class ListOptionHelper {
	
	private static Logger logger = LoggerFactory.getLogger(JacksonHelper.class);

	/**
	 * 获取在first集合内而不在second集合内的元素
	 * @param first
	 * @param second
	 * @return
	 */
    public static List<String> getDiffList(Collection<String> first, Collection<String> second) {  
	    	long t = System.currentTimeMillis();
	    	Set<String> sameString = new HashSet<String>(second);
        List<String> result = new ArrayList<String>(first.size());  
        for (String s : first) { 
            if (!sameString.contains(s)) {  
                result.add(s);  
            }  
        }  
        if(System.currentTimeMillis() - t > 1){
        		logger.debug("getDiffList with list first.size={},sencond.size={},use time={}ms",first.size(),second.size(),System.currentTimeMillis()-t);
        }
        
        return result;  
    }  
    
    /**
     * 获得在list内同时在list2内的元素
     * 注意：在结果集内并不去重，如果list内本身有重复，返回的结果内可能包含相同元素
     * @param list
     * @param list2
     * @return
     */
    public static List<String> getSameElements(Collection<String> list, Collection<String> list2) {
	    	long t = System.nanoTime();
	    	Set<String> set = new HashSet<String>(list2);
		List<String> sameElements = new ArrayList<String>(list.size());  
		for(String item : list){
			if(set.contains(item)){
				sameElements.add(item);
			}
		}
		if(logger.isDebugEnabled()){
			logger.debug("getSameElements list.size={},list2.size={},use time={}ns",list.size(),list2.size(),System.nanoTime()-t);
        }
		return sameElements;
	}
	
	
	/**
	 * 判断集合list和list2内是否有相同元素
	 * @param list
	 * @param list2
	 * @return
	 */
	public static boolean hasSameItem(List<String> list,List<String> list2){
		for (String item : list) {
			if(list2.contains(item)){
				return true;
			}
		}
		return false;
	}

	/**
	 * 辅助方法，将字符串分割转换为list
	 * @author yangwenkui
	 * @time 2017年1月10日 下午4:22:20
	 * @param provinceIds
	 * @return
	 */
	public static List<String> stringToList(String str,String split) {
		if(StringUtils.isBlank(str)){
			return Lists.newArrayList();
		}
		String[] arr = str.split(split);
		List<String> list = new ArrayList<String>(arr.length);
		for(String item : arr){
			if(StringUtils.isNotBlank(item)){
				list.add(item);
			}
		}
		return list;
	}

	public static void main(String[] args) {
		
		for(int j=0;j<20;j++){
			List<String> all = Lists.newArrayList();
			for(int i=1000000;i<1001500;i++){
				all.add(String.valueOf(i));
			}
			
			List<String> hasSend = Lists.newArrayList();
			for(int i=1000000;i<1000500;i++){
				hasSend.add(String.valueOf(i));
			}
			long t = System.nanoTime();
//		List<String> list = getDiffList1(all, hasSend);//12ms
//		all.removeAll(hasSend);//9ms
			List<String> list = getDiffList(all, hasSend);//2ms 当前选用 20次调用时平均耗时0.2ms
//		List<String> list = getSameElements1(all, hasSend);//8ms
//		List<String> list = getSameElements2(all, hasSend);//6ms
//		List<String> list = getSameElements(all, hasSend);//1ms 当前选用
			System.out.println(System.nanoTime()-t);
			System.out.println("size="+list.size()+JacksonHelper.toJson(list));
		}
	}
	
	
	
	
	
	/**以下方法因低效，放弃使用*/
//	public static List<String> getDiffList1(List<String> firstList, List<String> secondList) {
//		if(CollectionUtils.isEmpty(firstList)){
//			return Lists.newArrayList();
//		}
//		if(CollectionUtils.isEmpty(secondList)){
//			return firstList;
//		}
//		
//		List<String> results = Lists.newArrayList();
//		for(String item : firstList){
//			if(!secondList.contains(item)){
//				results.add(item);
//			}
//		}
//		return results;
//	}
	
//	@SuppressWarnings("unchecked")
//	public static List<String> getSameElements1(List<String> list, List<String> list2) {
//		return new ArrayList<String>(CollectionUtils.intersection(list, list2));
//	}
	
//	public static List<String> getSameElements2(List<String> list, List<String> list2) {
//		List<String> sameElements = Lists.newArrayList();
//		for(String item : list){
//			if(list2.contains(item)){
//				sameElements.add(item);
//			}
//		}
//		return sameElements;
//	}
	
}

