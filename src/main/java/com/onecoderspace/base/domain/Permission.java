package com.onecoderspace.base.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.onecoderspace.base.component.common.domain.BaseModel;

/**
 * 菜单及操作权限,树形结构
 */
@Entity
@Table(name = "permission")
public class  Permission implements BaseModel<Integer>{
	
	private static final long serialVersionUID = 6581772362165179231L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;//主键
	
	@Column(name="type",columnDefinition="tinyint default 0")
	private int type; //权限类型 0菜单 1按钮 2 操作权限
	
	@Column(name="name",length=50)
	private String name; //权限名称,用来展示
	
	@Column(name="code",length=50)
	private String code; //权限的值，校验权限时使用
	
	private String url;//操作权限的url
	
	@Column(name="weight",columnDefinition="int default 10000")
	private int weight = 10000; //排序值，升序
	
	@Column(name="pid",columnDefinition="int default 0")
	private int pid; //父节点ID
	
	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
   	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
   	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
   	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
   	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
	
   	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
	@Override
	public String toString() {
		return "Permission [id=" + id + ", type=" + type + ", name=" + name
				+ ", code=" + code + ", weight=" + weight + ", pid=" + pid
				+ "]";
	}

}
