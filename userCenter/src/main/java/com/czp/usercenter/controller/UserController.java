package com.czp.usercenter.controller;/*
    author 陈振平
    version 1.0
*/

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.czp.usercenter.domain.RegisterUser;
import com.czp.usercenter.domain.Team;
import com.czp.usercenter.domain.User;
import com.czp.usercenter.domain.UserTeam;
import com.czp.usercenter.exception.BusinessException;
import com.czp.usercenter.mapper.UserMapper;
import com.czp.usercenter.service.TeamService;
import com.czp.usercenter.service.UserService;
import com.czp.usercenter.service.UserTeamService;
import com.mysql.cj.PreparedQuery;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.management.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.beans.Transient;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.czp.usercenter.domain.UserConstant.USER_LOGIN_STATE;
@Slf4j
@RestController
@RequestMapping("api")
@Api(value = "车辆过卡口数据查询", tags = "车辆过卡口数据查询")
public class UserController {
    @Autowired
    UserMapper mapper;
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    TeamService teamService;
    @Autowired
    UserTeamService userTeamService;
    @Autowired
    UserService service;
    @PostMapping("/register")
    @ApiImplicitParam(name = "user",value = "账号密码",required = true)
    @ApiOperation(value = "用户注册")
    public String RegisterTest(@RequestBody User user){
        long l = service.userRegister(user.getUserAccount(), user.getUserPassword(), user.getCheckPassword(),user.getPlanetCode());
        System.out.println(l+"测试");
        String state="";
        if(l==-1){
            state="注册失败";
        }else{
            state="注册成功";
        }
        return state;
    }

    @PostMapping("login")
    public User UserLogin(@RequestBody User user, HttpServletRequest request){
        User user1 = service.userLogin(user.getUserAccount(), user.getUserPassword(), request);
        return user1;
    }
    @PostMapping("UpdateUser")
    public String UpdateUser(@RequestBody User users,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(USER_LOGIN_STATE);
        users.setId(user.getId());
        System.out.println(users+"users的更新");
        mapper.updateById(users);
        User user1 = mapper.selectById(user.getId());
        session.setAttribute(USER_LOGIN_STATE,user1);
        return "ok";
    }
    @GetMapping("getList")
    public List<User> getListUser(String username,HttpServletRequest request){
        QueryWrapper wrapper=new QueryWrapper();
        if(StringUtils.isNotBlank(username)){
            wrapper.like("username",username);
        }
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(USER_LOGIN_STATE);
        if(user.getRole()!=1){
            return new ArrayList<>();
        }
        List<User> list = mapper.selectList(wrapper);
        List<User> collect = list.stream().map(item -> {
            item.setUserPassword(null);
            return item;
        }).collect(Collectors.toList());
        return collect;
    }
    @PostMapping("/delete/{id}")
    public Boolean deleteUser(@PathVariable("id") long id){
        boolean b = service.removeById(id);
        return b;
    }
    @GetMapping("/currentUser")
    public User currentUser(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null){
            log.error("sesssion为空");
            return null;
        }else {
            User attribute = (User) session.getAttribute(USER_LOGIN_STATE);
            System.out.println(attribute);
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id", attribute.getId());
            User user = mapper.selectOne(wrapper);
            user.setUserPassword(null);
            return user;
        }
        }
    @PostMapping("logout")
    public void LogOut(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.removeAttribute(USER_LOGIN_STATE);
    }
    @GetMapping("allList")
    @ApiOperation(value = "分页查询")
    public IPage<User> recommend(Integer pageNum,Integer pageSize,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user =(User)session.getAttribute(USER_LOGIN_STATE);
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String id =String.format("partMatcher:%s",user.getId());
        Page<User> page =(Page<User>)valueOperations.get(id);
        if(page!=null){
            return page;
        }
        QueryWrapper<User> wrapper=new QueryWrapper<>();
        IPage pages=new Page(pageNum,pageSize);
        //先查询出所有的用户
        IPage page1 = service.page(pages, wrapper);
        valueOperations.set(id,page1,30000, TimeUnit.MILLISECONDS);
        return page1;
    }
    @GetMapping("searchByTags")
    public List<User> searchByTags(@RequestBody @RequestParam("tags[]") List<String> tags){
        System.out.println(tags);
        List<User> users = service.tagListUser(tags);
        return users;
    }

    @GetMapping("ImportUser")
    @ApiOperation(value = "自动导入大量数据")
    public void ImportUser(){
        User user=new User();
        for (int i = 0; i < 500; i++) {
            user.setId((long)(i+501));
            user.setUserAccount("yupi"+i+8);
            user.setUserPassword("123456789");
            user.setUserStatus(0);
            user.setEmail("1840854390@qq.com");
            user.setPlanetCode("12"+i+41);
            user.setGender(1);
            user.setPhone("18569023503");
            user.setAvatarUrl("https://himg.bdimg.com/sys/portraitn/item/public.1.e137c1ac.yS1WqOXfSWEasOYJ2-0pvQ");
            user.setProfile("学习java");
            user.setIsDelete(0);
            user.setTags("java,实习");
            user.setUsername("鱼皮"+i+8);
            mapper.insert(user);
        }
    }

    @PostMapping("addTeam")
    @Transactional(rollbackFor = Exception.class)
    public String addTeam(@RequestBody Team team,HttpServletRequest request){
        HttpSession session=request.getSession();
        User user =(User)session.getAttribute(USER_LOGIN_STATE);
        team.setUserId(user.getId());
        teamService.save(team);
        UserTeam userTeam=new UserTeam();
        userTeam.setTeamId(team.getId());
        userTeam.setUserId(user.getId());
        userTeam.setJoinTime(new Date());
        userTeam.setId(null);
        userTeam.setIsDelete(0);
        userTeamService.save(userTeam);
     return "ok";
    }
    @GetMapping("delTeam")
    public String delTeam(int id,HttpServletRequest request) throws BusinessException {
        String s = teamService.delTeamById(id, request);
        return s;
    }
    @PostMapping("updateTeam")
    public String updateTeam(@RequestBody Team team) throws Exception {
        //队伍是加密状态,则需要设置密码
        if(team.getStatus()==2){
            if(StringUtils.isBlank(team.getPassword())){
              //制作一个全局异常处理器
                throw new BusinessException("加密状态需要设置密码");
            }
        }
        teamService.updateById(team);
        return "ok";
    }
    @GetMapping("getTeam")
    public Team getTeam(int id){
        Team byId = teamService.getById(id);
        return byId;
    }
    @GetMapping("list")
    public Page getList(Team team,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user =(User) session.getAttribute(USER_LOGIN_STATE);
        Page page=new Page(team.getPageNum(),team.getPageSize());
        QueryWrapper wrappers=new QueryWrapper();
        wrappers.eq("userId",user.getId());
        List<UserTeam> list1 = userTeamService.list(wrappers);
        QueryWrapper wrapper=new QueryWrapper();
        if(team.getName()!=null) {
            wrapper.like("name", team.getName());
        }
        Page page1 = teamService.page(page,wrapper);
        List<Team> records = page1.getRecords();
        for (Team team1 :records){
         if(team1.getUserId().equals(user.getId())){
                //队长
             team1.setRole(1);
         }else{
               //普通队员
             team1.setRole(0);
         }
        }
        //获取到队伍的id以及对应的队伍人数
        List<Long> list=teamService.countPeopleNumber(records);
        int i=0;
        for (Team record : records) {
            record.setNum(list.get(i));
            i++;
        }
        List<Team> collect = records.stream().filter(index -> {
            for (UserTeam userTeam : list1) {
                if (userTeam.getTeamId().equals(index.getId())) {
                    return false;
                }
            }
            return true;
        }).collect(Collectors.toList());
        page1.setRecords(collect);
        return page1;
    }
    @PostMapping("join")
    public String joinTeam(@RequestBody Team team,HttpServletRequest request) throws BusinessException {
        Boolean result=userTeamService.joinTeam(team,request);
        return "ok";
    }
    @PostMapping("quit")
    public String quitTeam(@RequestBody Team team,HttpServletRequest request) throws BusinessException {
        String s = teamService.quitTeam(team, request);
        return s;
    }
    @GetMapping("MyjoinTeamList")
    public List<Team> joinTeamList(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("userId",user.getId());
        List<UserTeam> list = userTeamService.list(wrapper);
        List<Long> Ids=new ArrayList<>();
        for (UserTeam userTeam:list){
            Ids.add(userTeam.getTeamId());
        }
        QueryWrapper wrapper1=new QueryWrapper();
        wrapper1.in("id",Ids);
        List<Team> listTeam=null;
        if(Ids.size()!=0) {
            listTeam = teamService.list(wrapper1);
        }else{
            Ids=new ArrayList<>();
        }
        List<Long> lists=teamService.countPeopleNumber(listTeam);
        int i=0;
        for (Team record : listTeam) {
            record.setNum(lists.get(i));
            i++;
        }
        return listTeam;
    }
    @GetMapping("MyCreateTeamList")
    public List<Team> MyCreateTeamList(HttpServletRequest request){
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(USER_LOGIN_STATE);
        QueryWrapper wrapper=new QueryWrapper();
        wrapper.eq("userId",user.getId());
        List<Team> list = teamService.list(wrapper);
        List<Long> lists=teamService.countPeopleNumber(list);
        int i=0;
        for (Team record : list) {
            record.setNum(lists.get(i));
            i++;
        }
        return list;
    }
    //最短编辑距离算法
    @GetMapping("/EditDistance")
    public List<User> EditDistance(Integer num,HttpServletRequest request){
        HttpSession session = request.getSession();
        User user=(User)session.getAttribute(USER_LOGIN_STATE);
        List<User> list=service.matchUserList(num,user);
        return list;
    }
}