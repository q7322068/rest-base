package com.onecoderspace.base.domain;

import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.onecoderspace.base.component.common.domain.BaseModel;

import java.sql.Timestamp;

/**
 * 用户
 */
@Entity
@Table(name = "user")
public class User implements BaseModel<Integer>{
	
	private static final long serialVersionUID = -91221104520172449L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;//主键
	
	@ApiModelProperty(value="用户类别  0普通用户 1运营人员")
	@Column(columnDefinition="tinyint default 0")
	private int type = 0;
	
	@ApiModelProperty(value="账号")
	@Column(name="username",length=50)
	private String username; 
	
	@ApiModelProperty(value="姓名")
	@Column(name="name",length=50)
	private String name; 
	
	@ApiModelProperty(value="密码（md5(mix(password,salt))）")
	@Column(name="pwd",length=50)
	private String pwd;
	
	@ApiModelProperty(value="密码加密的“盐”")
	@Column(name="salt",length=16)
	private String salt;
	
	@ApiModelProperty(value="手机号")
	@Column(name="mobile",length=15)
	private String mobile;
	
	@ApiModelProperty(value="邮箱")
	@Column(name="email",length=50)
	private String email; 
	
	@ApiModelProperty(value="昵称")
	@Column(name="nick_name",length=50)
	private String nickName;
	
	@ApiModelProperty(value="性别 0未设置 1男 2女")
	@Column(name="gender",columnDefinition="tinyint default 0")
	private int gender = 0;
	
	@ApiModelProperty(value="账号状态 0已注册未审核  1待审核 2审核通过  -1审核未通过")
	@Column(name="status",columnDefinition="tinyint default 0")
	private int status = 0;
	
	@ApiModelProperty(value="账号是否有效 1有效 0无效")
	@Column(name="enable",columnDefinition="tinyint default 1")
	private int enable = 0; 
	
	@ApiModelProperty(value="用户头像  相对路径，不要带域名")
	@Column(name="avatar",length=255)
	private String avatar;
	
	@ApiModelProperty(value="注册时间")
	@Column(name="register_time")
	private Timestamp registerTime;
	
	@ApiModelProperty(value="最后更新人")
	@Column(name="updator",columnDefinition="int default 0")
	private int updator = 0;
	
	@ApiModelProperty(value="最后更新时间")
	@Column(name="update_time")
	private Timestamp updateTime;
	
	@ApiModelProperty(value="标记删除字段  1已删除  0未删除")
	@Column(name="del",columnDefinition="tinyint default 0")
	private int del = 0;
	
	
	//账号状态 0已注册未审核  1待审核 2审核通过  -1审核未通过
	public static final int	STATUS_UNAUDIT = 0;
	public static final int	STATUS_WAIT_AUDIT = 1;
	public static final int	STATUS_AUDIT_PASS = 2;
	public static final int	STATUS_AUDIT_UNPASS = -1;
	
	//用户类别  0普通用户 1运营人员
	public static final int	TYPE_USER = 0;
	public static final int	TYPE_OPERATOR = 1;
	
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
   	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
   	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	
   	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
   	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
   	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
   	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
   	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	
   	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
   	public int getEnable() {
		return enable;
	}

	public void setEnable(int enable) {
		this.enable = enable;
	}
	
   	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
   	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}
	
   	public int getUpdator() {
		return updator;
	}

	public void setUpdator(int updator) {
		this.updator = updator;
	}
	
   	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
   	public int getDel() {
		return del;
	}

	public void setDel(int del) {
		this.del = del;
	}

	@Override
	public String toString() {
		return String
				.format("User [id=%s, type=%s, username=%s, name=%s, pwd=%s, salt=%s, mobile=%s, email=%s, nickName=%s, gender=%s, status=%s, enable=%s, avatar=%s, registerTime=%s, updator=%s, updateTime=%s, del=%s]",
						id, type, username, name, pwd, salt, mobile, email,
						nickName, gender, status, enable, avatar, registerTime,
						updator, updateTime, del);
	}

}
