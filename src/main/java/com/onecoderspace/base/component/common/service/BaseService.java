package com.onecoderspace.base.component.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.onecoderspace.base.component.common.domain.BaseModel;

public interface BaseService<T extends BaseModel<ID>, ID extends Serializable> {

	/**
	 * 新增或更新
	 */
	T save(T t);
	
	/**
	 * 新增或更新
	 * 注意数量不要太大，特别是数据迁移时不要使用该方法
	 */
	Iterable<T> save(Iterable<T> entities);
	
	/**
	 * 根据ID删除
	 */
	void del(ID id);
	
	/**
	 * 根据实体删除
	 */
	void del(T t);
	
	/**
	 * 根据ID查找对象
	 */
	T findById(ID id);
	
	List<T> findAll();
	
	/**
	 * 分页排序获取数据
	 * 禁止使用该接口进行count操作
	 * Pageable pageable = new PageRequest(0, 10, new Sort(Sort.Direction.DESC,"id"));
	 * @param pageable
	 * @return
	 */
	Page<T> findAll(Pageable pageable);
	
	/**
	 * 多条件查询
	 * 注：多个条件间是and关系 & 参数是属性对应的类型 使用时注意避免结果集过大
	 * @author yangwk
	 * @time 2017年8月1日 下午3:50:46
	 * @param params {"username:like":"test"} 键的格式为字段名:过滤方式,过滤方式见{@code QueryTypeEnum}
	 * @return
	 */
	List<T> list(Map<String, Object> params);
	
	/**
	 * 分页多条件查询
	 * 注：多个条件间是and关系 & 参数是属性对应的类型
	 * @author yangwk
	 * @time 2017年8月1日 下午3:50:46
	 * @param params {"username:like":"test"} 键的格式为字段名:过滤方式,过滤方式见{@code QueryTypeEnum}
	 * @param pageable 分页信息 new PageRequest(page, size,new Sort(Direction.DESC, "updateTime"))
	 * @return
	 */
	Page<T> list(Map<String, Object> params,Pageable pageable);
	
}
