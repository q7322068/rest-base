-- 初始化管理员用户 admin admin2017
insert into user(id,username,name,salt,pwd,enable,del,type,register_time,update_time) VALUES(1,'admin','admin','Jm1tRIWU2o42!OVb','6f4dd2230a5d6a68e5c13ca7b5a681eb',1,0,1,NOW(),NOW());

-- 初始化角色
insert into role(id,name,code,remark) VALUES(1,'普通用户','user','系统角色，请勿删除');
insert into role(id,name,code,remark) VALUES(2,'操作员','operator','系统角色，请勿删除');
insert into role(id,name,code,remark) VALUES(3,'管理员','admin','系统角色，请勿删除');

-- 添加用户角色关系
insert into user_role(user_id,role_id) VALUES(1,3);

-- 初始权限
insert into permission(id,type,name,code,url,weight,pid) VALUES(1,2,'用户-查询','user:get','/user/get',4,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(2,2,'用户-当前用户','user:current','/user/current',5,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(3,2,'用户-修改密码','user:reset-pwd','/user/reset-pwd',7,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(4,2,'用户审核-列表','admin:user:list','/admin/user/list',46,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(5,2,'用户审核-保存','admin:user:save','/admin/user/save',47,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(6,2,'用户审核-删除','admin:user:delete','/admin/user/delete',48,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(7,2,'用户审核-列表','admin:user:role:list','/admin/user/role/list',51,3);
insert into permission(id,type,name,code,url,weight,pid) VALUES(8,2,'用户审核-设置角色','admin:user:role:set','/admin/user/role/set',52,3);

-- 添加角色[admin]的权限
insert into role_permission(role_id,permission_id) VALUES(3,1);
insert into role_permission(role_id,permission_id) VALUES(3,2);
insert into role_permission(role_id,permission_id) VALUES(3,3);
insert into role_permission(role_id,permission_id) VALUES(3,4);
insert into role_permission(role_id,permission_id) VALUES(3,5);
insert into role_permission(role_id,permission_id) VALUES(3,6);
insert into role_permission(role_id,permission_id) VALUES(3,7);
insert into role_permission(role_id,permission_id) VALUES(3,8);





