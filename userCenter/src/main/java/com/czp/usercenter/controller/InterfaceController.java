package com.czp.usercenter.controller;/*
    author 陈振平
    version 1.0
*/

import com.czp.usercenter.domain.InterfaceInfo;
import com.czp.usercenter.service.InterfaceInfoService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Api(value = "接口信息", tags = "接口信息")
public class InterfaceController {
    @Autowired
    InterfaceInfoService service;
    @PostMapping("add")
    public String addInterfaceInfo(@RequestBody InterfaceInfo info){
        service.save(info);
        return "ok";
    }
    @PostMapping("update")
    public String update(@RequestBody InterfaceInfo info){
        service.updateById(info);
        return "ok";
    }
    @GetMapping("del/{id}")
    public String delete(@PathVariable(name = "id") Long id){
        service.removeById(id);
        return "ok";
    }
    @GetMapping("getAll")
    public List<InterfaceInfo> getAll(){
        return service.list();
    }
}
