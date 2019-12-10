package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.tools.RequestToString;
import com.example.demo.entity.BaseBean;
import com.example.demo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

@RestController
@RequestMapping("/user")
public class UserAlterController {
    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/alter", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public JSONObject useralter(HttpServletRequest request) {
        RequestToString rts = new RequestToString(request);
        String req = rts.getReq();              //http request转字符串
        JSONObject jsonParam = JSONObject.parseObject(req);

        JSONObject result;
        String sql = new String();
        String uid = jsonParam.getString("uid");
        String password = jsonParam.getString("password");
        String sql1 = "SELECT * FROM user WHERE uid=?";
        RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
        User user = new User();
        BaseBean bb = new BaseBean();
        try {
            user = jdbctemplate.queryForObject(sql1, rowMapper, uid);//查找有没有值
            if (!user.getPassword().equals(password)) {
                bb.setBase(0, "密码错误,无权访问");
                result = (JSONObject) JSONObject.toJSON(bb);
                return result;
            }
        } catch (EmptyResultDataAccessException e) {
            bb.setBase(-1, "用户不存在");
            result = (JSONObject) JSONObject.toJSON(bb);
            return result;
        }

        String nickname = jsonParam.getString("nickname");
        if (!nickname.equals("")) {
            sql = "update user set nickname ='" + nickname + "' where uid='" + uid + "'";
        }
        String head = jsonParam.getString("head");
        if (!head.equals("")) {
            byte[] headbytes = Base64.getDecoder().decode(head);
            sql = "update user set head ='" + headbytes + "' where uid='" + uid + "'";
        }
        String newpassword = jsonParam.getString("newpassword");
        if (!newpassword.equals("")) {
            sql = "update user set password ='" + newpassword + "' where uid='" + uid + "'";
        }
        int temp = jdbctemplate.update(sql);
        if (temp > 0) {
            bb.setBase(1, "操作成功");
        } else bb.setBase(-99, "数据库出错");

        result = (JSONObject) JSONObject.toJSON(bb);
        return result;
    }
}
