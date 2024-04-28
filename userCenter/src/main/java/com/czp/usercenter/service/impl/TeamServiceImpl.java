package com.czp.usercenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.czp.usercenter.domain.Team;
import com.czp.usercenter.domain.User;
import com.czp.usercenter.domain.UserTeam;
import com.czp.usercenter.exception.BusinessException;
import com.czp.usercenter.mapper.TeamMapper;
import com.czp.usercenter.service.TeamService;
import com.czp.usercenter.service.UserService;
import com.czp.usercenter.service.UserTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.czp.usercenter.domain.UserConstant.USER_LOGIN_STATE;

/**
* @author 18408
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-04-01 21:05:11
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService {
    @Autowired
    UserService service;
    @Autowired
    UserTeamService userTeamService;
    @Override
    public List<Team> ListTeam(Team team) {
        QueryWrapper wrapper=new QueryWrapper();
        String name = team.getName();
        if(name!=null){
            wrapper.like("name",name);
        }
        String description = team.getDescription();
        if(description!=null){
            wrapper.like("description",description);
        }
        Integer maxNum = team.getMaxNum();
        if(maxNum>0){
            wrapper.le("maxNum",maxNum);
        }
        List<Team> teamList = this.list(wrapper);
        //队伍以及队伍的创建人
        for(Team current: teamList){
            User user = service.getById(current.getUserId());
            current.setUser(user);
        }
        return teamList;
    }

    @Override
    public String quitTeam(Team team, HttpServletRequest request) throws BusinessException {
        //开始退出队伍
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        //用户是否属于该队伍
        QueryWrapper wrapper2=new QueryWrapper();
        wrapper2.eq("teamId",team.getId());
        wrapper2.eq("userId",user.getId());
        long count1 = userTeamService.count(wrapper2);
        if(count1==0){
            throw new BusinessException("用户并不存在");
        }
        //队伍人数是否大于一
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("teamId",team.getId());
        long count = userTeamService.count(wrapper);
        if(count==1){
            //队伍解散
            userTeamService.remove(wrapper);
            this.removeById(team.getId());
        }else{
            //判断是否为队长则顺位
            Team byId = this.getById(team.getId());
            Long id = user.getId();
            if(byId.getUserId().equals(id)){
                //队长退出则将队伍队伍转交给下一个人
                QueryWrapper wrapper1=new QueryWrapper();
                wrapper1.eq("teamId",team.getId());
                wrapper1.last("order by id asc limit 2");
                List list = userTeamService.list(wrapper1);
                UserTeam userTeam =(UserTeam)list.get(1);
                Team t1=team;
                t1.setUserId(userTeam.getUserId());
                this.updateById(t1);
                QueryWrapper wrapper3=new QueryWrapper();
                wrapper3.eq("userId",user.getId());
                wrapper3.eq("teamId",team.getId());
                userTeamService.remove(wrapper3);
            }else{
                //普通队员退出
                QueryWrapper wrapper1=new QueryWrapper();
                wrapper1.eq("userId",user.getId());
                userTeamService.remove(wrapper1);
            }
        }
        return "用户退出成功";
    }

    @Override
    @Transactional
    public String delTeamById(int id,HttpServletRequest request) throws BusinessException {
        //队长解散队伍
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(USER_LOGIN_STATE);
        Team byId = this.getById(id);
        //判断是否为队长
        if(!byId.getUserId().equals(user.getId())){
            throw new BusinessException("非队长不能解散队伍");
        }
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("teamId",id);
        userTeamService.remove(wrapper);
        this.removeById(id);
        return "成功解散队伍";
    }

    @Override
    public List<Long> countPeopleNumber(List<Team> list) {
        QueryWrapper wrapper=new QueryWrapper();
        List<Long> listId=new ArrayList<>();
        for (Team team : list) {
            wrapper.in("teamId", team.getId());
            long count = userTeamService.count(wrapper);
            listId.add(count);
            wrapper.clear();
        }
        return listId;
    }
}