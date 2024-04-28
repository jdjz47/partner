package com.czp.usercenter.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.czp.usercenter.domain.Team;
import com.czp.usercenter.exception.BusinessException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


/**
* @author 18408
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-04-01 21:05:11
*/
public interface TeamService extends IService<Team> {

    List<Team> ListTeam(Team team);

    String quitTeam(Team team, HttpServletRequest request) throws BusinessException;

    String delTeamById(int id,HttpServletRequest request) throws BusinessException;

    List<Long> countPeopleNumber(List<Team> list);
}
