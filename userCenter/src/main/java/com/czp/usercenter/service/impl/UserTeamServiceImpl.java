package com.czp.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czp.usercenter.domain.Team;
import com.czp.usercenter.domain.User;
import com.czp.usercenter.domain.UserTeam;
import com.czp.usercenter.exception.BusinessException;
import com.czp.usercenter.mapper.UserTeamMapper;
import com.czp.usercenter.service.TeamService;
import com.czp.usercenter.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Date;

import static com.czp.usercenter.domain.UserConstant.USER_LOGIN_STATE;

/**
* @author 18408
* @description 针对表【user_team(用户队伍关系)】的数据库操作Service实现
* @createDate 2024-04-01 21:12:34
*/
@Service
public class UserTeamServiceImpl extends ServiceImpl<UserTeamMapper, UserTeam>
    implements UserTeamService {
    @Autowired
    TeamService teamService;

    @Override
    public Boolean joinTeam(Team team, HttpServletRequest request) throws BusinessException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        UserTeam userTeam=new UserTeam();
        userTeam.setUserId(user.getId());
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        //用户最多加入五个队伍
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("userId",user.getId());
        long count = this.count(wrapper);
        if(count>5){
            throw new BusinessException("用户最多加入五个队伍");
        }
        //不能重复加入已加入队伍
        QueryWrapper wrapper1=new QueryWrapper();
        wrapper1.eq("userId",user.getId());
        wrapper1.eq("teamId",team.getId());
        long count1 = this.count(wrapper1);
        if(count1>0){
            throw new BusinessException("已加入此队伍,不能重复加入");
        }
        //如果加入加密队伍需要密码匹配
        Team byId = teamService.getById(team.getId());
        if(team.getStatus()==2){
            if(!team.getPassword().equals(byId.getPassword())){
                throw new BusinessException("密码错误");
            }
        }
        QueryWrapper wrapper2=new QueryWrapper();
        wrapper2.eq("teamId",team.getId());
        long count2 = this.count(wrapper2);
        //只能加入未满的队伍
        if(count2>byId.getMaxNum()){
            throw new BusinessException("队伍人数已满");
        }
        this.save(userTeam);
        return null;
    }
}




