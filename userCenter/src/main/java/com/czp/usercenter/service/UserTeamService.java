package com.czp.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czp.usercenter.domain.Team;
import com.czp.usercenter.domain.UserTeam;
import com.czp.usercenter.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;

/**
* @author 18408
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service
* @createDate 2024-04-01 21:12:34
*/
public interface UserTeamService extends IService<UserTeam> {

    Boolean joinTeam(Team team, HttpServletRequest request) throws BusinessException;
}
