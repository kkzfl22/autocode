-- 用户信息
drop table IF EXISTS  para_user_info;


create table para_user_info
(
	USER_ID                       	VARCHAR(40)         not null                                ,
	USER_NAME                     	VARCHAR(40)         not null                                ,
	USER_PASSWORD                 	VARCHAR(60)         not null                                ,
	USER_PHONE                    	VARCHAR(60)                                                 ,
	USER_SEX                      	INT(2)                                                      ,
  primary key (USER_ID)
);



-- 角色信息
drop table IF EXISTS  para_role_info;


create table para_role_info
(
	ROLE_ID                       	VARCHAR(40)         not null                                ,
	ROLE_NAME                     	VARCHAR(40)         not null                                ,
	ROLE_REMARK                   	VARCHAR(60)                                                 ,
  primary key (ROLE_ID)
);



-- 用户角色信息
drop table IF EXISTS  para_user_role;


create table para_user_role
(
	USER_ID                       	VARCHAR(40)         not null                                ,
	ROLE_ID                       	VARCHAR(40)         not null                                ,
  primary key (USER_ID,ROLE_ID)
);



-- 组织信息
drop table IF EXISTS  para_org_info;


create table para_org_info
(
	ORG_ID                        	VARCHAR(40)         not null                                ,
	ORG_NAME                      	VARCHAR(40)         not null                                ,
	ORG_PARENT                    	VARCHAR(40)         not null                                ,
  primary key (ORG_ID)
);



-- 用户组织信息
drop table IF EXISTS  para_user_org;


create table para_user_org
(
	USER_ID                       	VARCHAR(40)         not null                                ,
	ORG_ID                        	VARCHAR(40)         not null                                ,
  primary key (USER_ID,ORG_ID)
);



