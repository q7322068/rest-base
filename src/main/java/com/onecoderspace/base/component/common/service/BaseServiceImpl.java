package com.onecoderspace.base.component.common.service;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.onecoderspace.base.component.common.dao.BaseDao;
import com.onecoderspace.base.component.common.domain.AbstractBaseModel;
import com.onecoderspace.base.component.common.domain.BaseModel;
import com.onecoderspace.base.component.common.domain.MarkDeleteableModel;

@SuppressWarnings("unchecked")
public abstract class BaseServiceImpl<T extends BaseModel<ID>, ID extends Serializable> implements BaseService<T, ID> {

	public abstract BaseDao<T, ID> getDAO();

	@Override
	public T save(T t){
		if(t instanceof AbstractBaseModel){
			AbstractBaseModel<T> baseModel = (AbstractBaseModel<T>)t;
			Timestamp current = new Timestamp(System.currentTimeMillis());
			if(baseModel.getId() == null){
				baseModel.setCreateTime(current);
			}
			baseModel.setUpdateTime(current);
		}
		return getDAO().save(t);
	}
	
	@Override
	public Iterable<T> save(Iterable<T> entities){
		for (T t : entities) {
			if(t instanceof AbstractBaseModel){
				AbstractBaseModel<T> baseModel = (AbstractBaseModel<T>)t;
				Timestamp current = new Timestamp(System.currentTimeMillis());
				if(baseModel.getId() == null){
					baseModel.setCreateTime(current);
				}
				baseModel.setUpdateTime(current);
			}
		}
		return getDAO().save(entities);
	}
	
	@Override
	public void del(ID id){
		T t = findById(id);
		if(t == null){
			return;
		}
		if(t instanceof MarkDeleteableModel){
			MarkDeleteableModel<T> baseModel = (MarkDeleteableModel<T>)t;
			baseModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			baseModel.setDel(1);
			getDAO().save(t);
		} else {
			getDAO().delete(t);;
		}
		
	}
	
	@Override
	public void del(T t) {
		if(t instanceof MarkDeleteableModel){
			MarkDeleteableModel<T> baseModel = (MarkDeleteableModel<T>)t;
			baseModel.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			baseModel.setDel(1);
			getDAO().save(t);
		} else {
			getDAO().delete(t);
		}
	}
	
	@Override
	public T findById(ID id) {
		return getDAO().findOne(id);
	}
	
	@Override
	public List<T> findAll() {
		return getDAO().findAll();
	}
	
	@Override
	public Page<T> findAll(Pageable pageable){
		return getDAO().findAll(pageable);
	}
	
	@Override
	public List<T> list(final Map<String, Object> params) {
		Specification<T> spec = new Specification<T>() {  
			@Override
			public Predicate toPredicate(Root<T> root,CriteriaQuery<?> query,CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				for(Entry<String, Object> entry : params.entrySet()){
					Object value = entry.getValue();
					if(value == null || StringUtils.isBlank(value.toString())){
						continue;
					}
					String key = entry.getKey();
					String[] arr = key.split(":");
					Predicate predicate = getPredicate(arr,value,root,cb);
					list.add(predicate);
				}
			    Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));  
			}
		};  
		List<T> list = getDAO().findAll(spec);
		return list;
	}
	
	@Override
	public Page<T> list(final Map<String, Object> params,Pageable pageable){
		Specification<T> spec = new Specification<T>() {  
			@Override
			public Predicate toPredicate(Root<T> root,CriteriaQuery<?> query,CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				for(Entry<String, Object> entry : params.entrySet()){
					Object value = entry.getValue();
					if(value == null || StringUtils.isBlank(value.toString())){
						continue;
					}
					String key = entry.getKey();
					String[] arr = key.split(":");
					Predicate predicate = getPredicate(arr,value,root,cb);
					list.add(predicate);
				}
			    Predicate[] p = new Predicate[list.size()];  
			    return cb.and(list.toArray(p));  
			}
		};  
		Page<T> page = getDAO().findAll(spec, pageable);
		return page;
	}
	
	
	private Predicate getPredicate(String[] arr, Object value,
			Root<T> root, CriteriaBuilder cb) {
		if(arr.length == 1){
			return cb.equal(root.get(arr[0]).as(value.getClass()), value);  
		}
		if(QueryTypeEnum.like.name().equals(arr[1])){
			return cb.like(root.get(arr[0]).as(String.class), String.format("%%%s%%", value));
		}
		if(QueryTypeEnum.ne.name().equals(arr[1])){
			return cb.notEqual(root.get(arr[0]).as(value.getClass()), value);
		}
		if(QueryTypeEnum.lt.name().equals(arr[1])){
			return getLessThanPredicate(arr,value,root,cb);
		}
		if(QueryTypeEnum.lte.name().equals(arr[1])){
			return getLessThanOrEqualToPredicate(arr,value,root,cb);
		}
		if(QueryTypeEnum.gt.name().equals(arr[1])){
			return getGreaterThanPredicate(arr,value,root,cb);
		}
		if(QueryTypeEnum.gte.name().equals(arr[1])){
			return getGreaterThanOrEqualToPredicate(arr,value,root,cb);
		}
		throw new UnsupportedOperationException(String.format("不支持的查询类型[%s]",arr[1]));
	}

	private Predicate getLessThanPredicate(String[] arr, Object value,
			Root<T> root, CriteriaBuilder cb) {
		if(Integer.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Integer.class), (int)value);
		}
		if(Long.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Long.class), (long)value);
		}
		if(Double.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Double.class), (double)value);
		}
		if(Float.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Float.class), (float)value);
		}
		if(Timestamp.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Timestamp.class), (Timestamp)value);
		}
		if(Date.class == value.getClass()){
			return cb.lessThan(root.get(arr[0]).as(Date.class), (Date)value);
		}
		return cb.lessThan(root.get(arr[0]).as(String.class), String.valueOf(value));
	}

	private Predicate getLessThanOrEqualToPredicate(String[] arr,
			Object value, Root<T> root, CriteriaBuilder cb) {
		if(Integer.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Integer.class), (int)value);
		}
		if(Long.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Long.class), (long)value);
		}
		if(Double.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Double.class), (double)value);
		}
		if(Float.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Float.class), (float)value);
		}
		if(Timestamp.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Timestamp.class), (Timestamp)value);
		}
		if(Date.class == value.getClass()){
			return cb.lessThanOrEqualTo(root.get(arr[0]).as(Date.class), (Date)value);
		}
		return cb.lessThanOrEqualTo(root.get(arr[0]).as(String.class), String.valueOf(value));
	}

	private Predicate getGreaterThanPredicate(String[] arr,
			Object value, Root<T> root, CriteriaBuilder cb) {
		if(Integer.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Integer.class), (int)value);
		}
		if(Long.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Long.class), (long)value);
		}
		if(Double.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Double.class), (double)value);
		}
		if(Float.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Float.class), (float)value);
		}
		if(Timestamp.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Timestamp.class), (Timestamp)value);
		}
		if(Date.class == value.getClass()){
			return cb.greaterThan(root.get(arr[0]).as(Date.class), (Date)value);
		}
		return cb.greaterThan(root.get(arr[0]).as(String.class), String.valueOf(value));
	}

	private Predicate getGreaterThanOrEqualToPredicate(String[] arr,Object value,
			Root<T> root, CriteriaBuilder cb) {
		if(Integer.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Integer.class), (int)value);
		}
		if(Long.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Long.class), (long)value);
		}
		if(Double.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Double.class), (double)value);
		}
		if(Float.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Float.class), (float)value);
		}
		if(Timestamp.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Timestamp.class), (Timestamp)value);
		}
		if(Date.class == value.getClass()){
			return cb.greaterThanOrEqualTo(root.get(arr[0]).as(Date.class), (Date)value);
		}
		return cb.lessThanOrEqualTo(root.get(arr[0]).as(String.class), String.valueOf(value));
	}  
	
}
