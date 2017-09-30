create index idx_username on user(username);

create index idx_userId on user_role(user_id);
create index idx_roleId on user_role(role_id);

create index idx_roleId on role_permission(role_id);
create index idx_permissionId on role_permission(permission_id);









