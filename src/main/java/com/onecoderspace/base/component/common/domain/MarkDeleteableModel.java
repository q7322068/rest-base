package com.onecoderspace.base.component.common.domain;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import com.onecoderspace.base.util.Constants;

/**
 * 需要标记删除的实体继承该类
 * @author yangwk
 */
@MappedSuperclass
public abstract class MarkDeleteableModel<ID extends Serializable> extends AbstractBaseModel<ID>{

	private static final long serialVersionUID = -1880548221110317053L;
	
	@ApiModelProperty(value="标记删除字段 0未删除 1已删除 ")
	@Column(name = "del", columnDefinition = "tinyint default 0")
	private int del = Constants.DEL_NO;

	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

}
