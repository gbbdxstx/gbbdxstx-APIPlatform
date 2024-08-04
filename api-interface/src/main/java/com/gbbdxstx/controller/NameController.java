package com.gbbdxstx.controller;

import com.gbbdxstx.gbbdxstxapiclientsdk.model.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 名称API
 */
@RestController
@RequestMapping("/name")
public class NameController {

    @GetMapping()
    public String getNameByGet(String name, HttpServletRequest request) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/{name}")
    public String getNameByPost(@PathVariable String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        return  "POST 用户名是" + user.getUsername();
    }
}
