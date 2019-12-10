package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.tools.RequestToString;
import com.example.demo.entity.BaseBean;
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

@RestController
@RequestMapping("/item")
public class ItemAlterController {

    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/alter", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public JSONObject itemalter(HttpServletRequest request) {
        RequestToString rts = new RequestToString(request);
        String req = rts.getReq();              //http request转字符串
        JSONObject jsonParam = JSONObject.parseObject(req);

        JSONObject result;
        char[] cc = {'1', '1', '1', '1', '1'};
        String uid = jsonParam.getString("uid");
        if (uid.equals("")) cc[0] = '0';
        String buyer = jsonParam.getString("buyer");
        if (buyer.equals("")) cc[1] = '0';
        String itemid = jsonParam.getString("itemid");
        if (itemid.equals("")) cc[2] = '0';
        String collectorid = jsonParam.getString("collectorid");
        if (collectorid.equals("")) cc[3] = '0';
        String comment = jsonParam.getString("comment");
        if (comment.equals("")) cc[4] = '0';

        String sql1 = "SELECT * FROM item WHERE itemid=?";
        RowMapper<Item> rowMapper = new BeanPropertyRowMapper<Item>(Item.class);
        Item item = new Item();
        BaseBean bb = new BaseBean();
        try {
            item = jdbctemplate.queryForObject(sql1, rowMapper, itemid);//查找有没有值
        } catch (EmptyResultDataAccessException e) {
            bb.setBase(-1, "不存在该物品");
            result = (JSONObject) JSONObject.toJSON(bb);
            return result;
        }
        String sql = new String();
        String str = String.valueOf(cc);
        if (str.equals("00110")) {      //收藏
            if (!item.getCollectorid().contains(collectorid + " ")) {     //未收藏
                sql = "update item set collectorid ='" + item.getCollectorid() + collectorid + " " + "' where itemid='" + itemid + "'";
                item.setCollectorid(item.getCollectorid() + " " + collectorid);
            } else {         //已收藏
                String s = item.getCollectorid().replaceAll(collectorid + " ", "");
                sql = "update item set collectorid ='" + s + "' where itemid='" + itemid + "'";
                item.setCollectorid(s);
            }
        }
        if (str.equals("10101")) {      //评论
            sql = "update item set comment ='" + uid + ":" + comment + " " + item.getComment() + "' where itemid='" + itemid + "'";
            item.setComment(uid + ":" + comment + item.getComment());
        }
        if (str.equals("01100")) {      //购买
            if (item.getExist().equals("0")) {
                bb.setBase(0, "已被买走");
                result = (JSONObject) JSONObject.toJSON(bb);
                return result;
            } else {
                sql = "update item set buyer ='" + buyer + "',exist='0' where itemid='" + itemid + "'";
                item.setBuyer(buyer);
                item.setExist("0");
            }
        }
        User releaser = new User();
        RowMapper<User> rowMapper2 = new BeanPropertyRowMapper<User>(User.class);
        int temp = jdbctemplate.update(sql);
        if (temp > 0) {
            String sql2 = "select * from user where uid =?";
            try {
                releaser = jdbctemplate.queryForObject(sql2, rowMapper2, item.getReleaserid());
                releaser.setPassword("");
                item.setReleaser(releaser);
            } catch (EmptyResultDataAccessException e) {
                releaser = null;
            }
            bb.setBase(1, "操作成功");
            bb.setData(item);
        } else bb.setBase(-99, "数据库出错");
        result = (JSONObject) JSONObject.toJSON(bb);
        return result;
    }
}

