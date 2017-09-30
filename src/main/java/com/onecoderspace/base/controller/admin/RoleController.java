package com.onecoderspace.base.controller.admin;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.onecoderspace.base.domain.Role;
import com.onecoderspace.base.domain.RolePermission;
import com.onecoderspace.base.service.RolePermissionService;
import com.onecoderspace.base.service.RoleService;
import com.onecoderspace.base.util.Return;

@Api(value="角色管理",tags={"运营接口  角色"})
@RestController
@RequestMapping("/admin/role")
public class RoleController {

	@ApiOperation(value="分页查询",notes="分页查询")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="page",value="分页，页码从0开始",required=true,dataType="int"),
		@ApiImplicitParam(paramType="query",name="size",value="每一页大小",required=true,dataType="int")}
	)
	@RequiresPermissions(value={"admin:permission:list"})
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public Page<Role> list(String name, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		Page<Role> rs = this.roleService.listByPage(name, new PageRequest(page, size, new Sort(Direction.DESC, "id")));
		return rs;
	}

	@ApiOperation(value="新增&修改",notes="新增&修改")
	@ApiImplicitParam(paramType="query",name="role",value="角色",dataType="Role")
	@RequiresPermissions(value={"admin:permission:save"})
	@RequestMapping(value="/save",method=RequestMethod.GET)
	public Return save(Role role) {
		this.roleService.save(role);
		return Return.success();
	}

	@ApiOperation(value="查询",notes="根据id查询")
	@ApiImplicitParam(paramType="query",name="id",value="角色id",required=true,dataType="int")
	@RequiresPermissions(value={"admin:permission:get"})
	@RequestMapping(value="/get",method=RequestMethod.GET)
	public Role get(int id) {
		Role entity = roleService.findById(id);
		return entity;
	}
	
	@ApiOperation(value="删除",notes="根据id删除")
	@ApiImplicitParam(paramType="query",name="id",value="角色id",required=true,dataType="int")
	@RequiresPermissions(value={"admin:permission:delete"})
	@RequestMapping("/delete")
	public Return delete(int id){
		this.roleService.del(id);
		return Return.success();
	}

	@ApiOperation(value = "角色拥有权限", notes = "根据id查询用户拥有的权限")
	@ApiImplicitParam(paramType="query",name="roleId",value="角色id",required=true,dataType="int")
	@RequiresPermissions(value={"admin:permission:permission:list"})
	@RequestMapping(value="/permission/list",method=RequestMethod.GET)
	public List<RolePermission> permissionList(int roleId) {
		List<RolePermission> rolePermissions = rolePermissionService.findByRoleId(roleId);
		return rolePermissions;
	}

	@ApiOperation(value = "设置角色拥有的权限", notes = "根据角色id设置权限")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "roleId", value = "角色id", required = true, dataType = "int"),
			@ApiImplicitParam(paramType = "query", name = "permissionIds", value = "权限ID，多个权限ID用英文逗号隔开", required = true, dataType = "int") })
	@RequiresPermissions(value = { "admin:permission:permission:set" })
	@RequestMapping(value = "/permission/set", method = RequestMethod.POST)
	public Return permissionSet(int roleId, String permissionIds) {
		return roleService.doSetPermissions(roleId, permissionIds);
	}

	@Autowired
	private RoleService roleService;

	@Autowired
	private RolePermissionService rolePermissionService;
}