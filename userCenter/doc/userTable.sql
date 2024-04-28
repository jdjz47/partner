create table if not exists yupi.user
(
    id           bigint auto_increment comment '用户id'
    primary key,
    userAccount  varchar(256)                       null comment '用户账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '用户性别',
    userPassword varchar(512)                       null comment '密码',
    phone        varchar(128)                       null comment '手机号码',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null,
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建日期',
    updateTime   datetime default CURRENT_TIMESTAMP null,
    isDelete     tinyint  default 0                 null,
    username     varchar(256)                       null comment '用户姓名',
    role         int                                null comment '用户的角色',
    plantCode    int                                null comment '星球编号'
    )
    comment '用户表格';
