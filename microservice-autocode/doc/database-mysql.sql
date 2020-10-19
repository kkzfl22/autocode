drop table IF EXISTS  para_user_info;


create table para_user_info
(
	USER_ID                       	VARCHAR(40)         not null                                                    comment '用户的id'     ,
	USER_NAME                     	VARCHAR(40)         not null                                                    comment '用户名'       ,
	USER_PASSWORD                 	VARCHAR(60)         not null                                                    comment '用户密码'      ,
	USER_PHONE                    	VARCHAR(60)                                                                     comment '手机号码'      ,
	USER_SEX                      	INT(2)                                                                          comment '性别'        ,
	  primary key (USER_ID)
) ENGINE=InnoDB 	DEFAULT CHARSET=utf8 COMMENT='用户信息';



drop table IF EXISTS  para_role_info;


create table para_role_info
(
	ROLE_ID                       	VARCHAR(40)         not null                                                    comment '角色的id'     ,
	ROLE_NAME                     	VARCHAR(40)         not null                                                    comment '角色的名称'     ,
	ROLE_REMARK                   	VARCHAR(60)                                                                     comment '角色的备注'     ,
	  primary key (ROLE_ID)
) ENGINE=InnoDB 	DEFAULT CHARSET=utf8 COMMENT='角色信息';



drop table IF EXISTS  para_user_role;


create table para_user_role
(
	USER_ID                       	VARCHAR(40)         not null                                                    comment '用户的id'     ,
	ROLE_ID                       	VARCHAR(40)         not null                                                    comment '角色的id'     ,
	  primary key (USER_ID,ROLE_ID)
) ENGINE=InnoDB 	DEFAULT CHARSET=utf8 COMMENT='用户角色信息';



drop table IF EXISTS  para_org_info;


create table para_org_info
(
	ORG_ID                        	VARCHAR(40)         not null                                                    comment '组织的id'     ,
	ORG_NAME                      	VARCHAR(40)         not null                                                    comment '组织的名称'     ,
	ORG_PARENT                    	VARCHAR(40)         not null                                                    comment '组织的父级'     ,
	  primary key (ORG_ID)
) ENGINE=InnoDB 	DEFAULT CHARSET=utf8 COMMENT='组织信息';



drop table IF EXISTS  para_user_org;


create table para_user_org
(
	USER_ID                       	VARCHAR(40)         not null                                                    comment '用户的id'     ,
	ORG_ID                        	VARCHAR(40)         not null                                                    comment '组织的id'     ,
	  primary key (USER_ID,ORG_ID)
) ENGINE=InnoDB 	DEFAULT CHARSET=utf8 COMMENT='用户组织信息';



