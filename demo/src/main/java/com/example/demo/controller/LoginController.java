package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseBean;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;

@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public JSONObject login(HttpServletRequest request) {
        String sql = "SELECT * FROM user WHERE uid=?";
        BufferedReader read = null;
        try {
            read = request.getReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder sb = new StringBuilder();
        String line = null;
        while (true) {
            try {
                if (!((line = read.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb.append(line);
        }
        String req = sb.toString();
        JSONObject jsonParam = JSONObject.parseObject(req);
        String uid = jsonParam.getString("uid");
        String password = jsonParam.getString("password");

        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        User user = new User();
        BaseBean bb = new BaseBean();
        try {
            user = jdbctemplate.queryForObject(sql, rowMapper, uid);
        } catch (EmptyResultDataAccessException e) {
            user = null;
        }

        if (user == null) {
            bb.setBase(-1, "No user found");//未注册
        } else if (user.getUid().equals(uid)) {
            if (user.getPassword().equals(password)) {
                bb.setBase(1, "Login success");//登录成功
                bb.setData(user);
            } else {
                bb.setBase(0, "Wrong password");//密码错误
            }
        }
        JSONObject result = (JSONObject) JSONObject.toJSON(bb);
        return result;
    }
}
