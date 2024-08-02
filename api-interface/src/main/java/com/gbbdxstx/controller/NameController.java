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
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/{name}")
    public String getNameByPost(@PathVariable String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/user")
    public String getUsernameByPost(@RequestBody User user, HttpServletRequest request) {
        /*String accessKey = request.getHeader("accessKey");
        String nonce = request.getHeader("nonce");
        String encodedBody = request.getHeader("body");
        String body = URLDecoder.decode(encodedBody, StandardCharsets.UTF_8);
        String timestamp = request.getHeader("timestamp");
        String sign = request.getHeader("sign");
        // todo 实际情况去数据库中查询是否分配给用户
        if (!accessKey.equals("1")) {
            throw new RuntimeException("无权限");
        }
        // todo 随机数nonce 利用hashmap或redis存进行校验
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // todo 时间和当前时间不能超过 5 分钟
        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }
        // todo 实际情况中将分配给用户的secretKey从数据库查出来用于生成sign
        String serverSign = SignUtil.getSign(body, "2");
        if (!sign.equals(serverSign)) {
            throw new RuntimeException("无权限");
        }*/
        String result =  "POST 用户名是" + user.getUsername();
        return result;
    }
}
