package com.example.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.tools.RequestToString;
import com.example.demo.entity.Item;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/item")
public class ItemListController {

    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public String getItemList(HttpServletRequest request) {

        Map<String, Object> item;
        List<Map<String, Object>> list = new ArrayList<>();

        RequestToString rts = new RequestToString(request);
        String req = rts.getReq();              //http request转字符串

        char[] cc = {'1', '1', '1', '1', '1', '1'};
        JSONObject jsonParam = JSONObject.parseObject(req);
        String kind = jsonParam.getString("kind");
        if (kind.equals("")) cc[0] = '0';
        String title = jsonParam.getString("title");
        if (title.equals("")) cc[1] = '0';
        String releaserid = jsonParam.getString("releaserid");
        if (releaserid.equals("")) cc[2] = '0';
        String exist = jsonParam.getString("exist");
        if (exist.equals("")) cc[3] = '0';         //不做要求
        String collectorid = jsonParam.getString("collectorid");
        if (collectorid.equals("")) cc[4] = '0';
        String buyer = jsonParam.getString("buyer");
        if (buyer.equals("")) cc[5] = '0';

        String sql = new String();
        String str = String.valueOf(cc);
        if (str.equals("100100")) {      //大类展示
            sql = "select * from item where kind ='" + kind + "' and exist='" + exist + "'";
        }
        if (str.equals("010100")) {      //搜索展示
            sql = "select * from item where title like '%" + title + "%' and exist='" + exist + "'";
        }
        if (str.equals("000100")) {      //首页展示
            sql = "select * from item where exist='" + exist + "'";
        }
        if (str.equals("000001")) {      //用户购买
            sql = "select * from item where buyer='" + buyer + "'";
        }
        if (str.equals("001000")) {      //用户发布
            sql = "select * from item where releaserid='" + releaserid + "'";
        }
        if (str.equals("000010")) {      //用户收藏
            sql = "select * from item where collectorid like '%" + collectorid + " %'";
        }

        RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);
        List<Item> items = jdbctemplate.query(sql, rowMapper);


        User releaser = new User();
        RowMapper<User> rowMapper2 = new BeanPropertyRowMapper<User>(User.class);
        for (int i = 0; i < items.size(); i++) {
            String sql2 = "select * from user where uid =?";
            try {
                releaser = jdbctemplate.queryForObject(sql2, rowMapper2, items.get(i).getReleaserid());
                releaser.setPassword("");
                Item itemitem = items.get(i);
                itemitem.setReleaser(releaser);
                items.set(i, itemitem);
            } catch (EmptyResultDataAccessException e) {
                releaser = null;
            }
        }
        String result = JSON.toJSONString(items);
        return result;
    }
}
