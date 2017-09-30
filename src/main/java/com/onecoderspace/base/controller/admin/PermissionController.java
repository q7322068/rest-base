package com.onecoderspace.base.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.onecoderspace.base.domain.Permission;
import com.onecoderspace.base.service.PermissionService;
import com.onecoderspace.base.util.Return;


@Api(value = "运营接口  权限", tags = { "运营接口  权限" })
@RestController
@RequestMapping("/admin/permission")
public class PermissionController {
	
	@ApiOperation(value = "获取所有权限", notes = "获取所有权限")
	@RequiresPermissions(value={"admin:permission:list"})
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Iterable<Permission> list(){
		Iterable<Permission> list = this.permissionService.findAll(new Sort(Direction.ASC, "weight","id"));
		return list;
	}	
	
	@ApiOperation(value = "新增/修改  权限", notes = "保存  新增/修改的  权限")
	@ApiImplicitParam(paramType = "query", name = "permission", value = "菜单操作权限实体", required = true, dataType = "Permission")
	@RequiresPermissions(value={"admin:permission:save"})
	@RequestMapping(value="/save",method=RequestMethod.GET)
	public Return save(Permission permission){
		this.permissionService.save(permission);
		return Return.success();
	}
	
	
	@ApiOperation(value = "删除  权限", notes = "根据id删除权限")
	@ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "int")
	@RequiresPermissions(value={"admin:permission:delete"})
	@RequestMapping(value="/delete",method=RequestMethod.GET)
	public Return delete(int id){
		this.permissionService.del(id);
		return Return.success();
	}

	@ApiOperation(value = "获取权限", notes = "根据id查询权限")
	@ApiImplicitParam(paramType = "query", name = "id", value = "权限id", required = true, dataType = "int")
	@RequiresPermissions(value={"admin:permission:get"})
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public Permission get(int id){
		Permission entity = permissionService.findById(id);
		return entity;
	}	
	
	@Autowired
	private PermissionService permissionService;
	
}