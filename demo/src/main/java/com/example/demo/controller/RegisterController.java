package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.entity.BaseBean;
import com.example.demo.entity.User;
import com.fasterxml.jackson.databind.ser.Serializers;
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
public class RegisterController {

    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/register", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public JSONObject register(HttpServletRequest request) {
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
        User ruser = JSONObject.parseObject(req, User.class);


        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        User user = new User();
        BaseBean bb = new BaseBean();
        bb.setBase(-1, "The id has been used");       //初始假设已被注册
        try {
            user = jdbctemplate.queryForObject(sql, rowMapper, ruser.getUid());//查找有没有值
        } catch (EmptyResultDataAccessException e) {
            String sql2 = "INSERT INTO user (nickname,uid,password,head,state) VALUES (?,?,?,?,?)";
            int sqlline = jdbctemplate.update(sql2, new Object[]{ruser.getNickname(), ruser.getUid(), ruser.getPassword(), ruser.getHead(), ruser.getState()});
            if (sqlline > 0) {
                bb.setBase(1, "Register success");
            } else bb.setBase(-1, "Unknown error");
        }
        JSONObject result = (JSONObject) JSONObject.toJSON(bb);
        return result;
    }
}
