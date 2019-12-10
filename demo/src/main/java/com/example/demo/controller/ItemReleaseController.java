package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.tools.RequestToString;
import com.example.demo.entity.BaseBean;
import com.example.demo.entity.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item")
public class ItemReleaseController {

    @Autowired
    JdbcTemplate jdbctemplate;

    @RequestMapping(value = "/release", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")

    @ResponseBody
    public JSONObject itemrelease(HttpServletRequest request) {
        RequestToString rts = new RequestToString(request);
        String req = rts.getReq();              //http request转字符串
        Item item = JSONObject.parseObject(req, Item.class);

        BaseBean bb = new BaseBean();

        String sql2 = "INSERT INTO item (itemid,releaserid,price,comment,releasetime,detail,image,exist,kind,title) VALUES (?,?,?,?,?,?,?,?,?,?)";
        int sqlline = jdbctemplate.update(sql2, new Object[]{0, item.getReleaserid(), item.getPrice(), item.getComment(), item.getReleasetime(), item.getDetail(), item.getImage(), item.getExist(), item.getKind(), item.getTitle()});
        if (sqlline > 0) {
            bb.setBase(1, "Release success");
        } else bb.setBase(-1, "Unknown error");

        JSONObject result = (JSONObject) JSONObject.toJSON(bb);
        return result;
    }
}
