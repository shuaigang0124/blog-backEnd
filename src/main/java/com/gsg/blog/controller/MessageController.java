package com.gsg.blog.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.gsg.blog.dto.MessageDTO;
import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.User;
import com.gsg.blog.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author shuaigang
 * @date 2022/10/19 15:09
 */
@RestController
@RequestMapping("/gsg/msg")
public class MessageController {

    @Autowired
    ESearchUtils eSearchUtils;

    @Autowired
    UserMapper userMapper;

    // 目标索引
    String indexName = "message";

    @PostMapping("/insertMsg")
    public Result<?> insertMsg(@RequestBody Request<MessageDTO> request) {

        MessageDTO messageDTO = request.getCustomData();

        if (StringUtils.isEmpty(messageDTO.getContent()) || StringUtils.isEmpty(messageDTO.getColor())) {
            throw ServiceException.errorParams("color或content不能为空");
        }

        messageDTO.setId("MSG" + PKGenerator.generate())
                .setDeleted(0)
                .setGmtCreate(new Date())
                .setGmtModified(new Date());
        // 存数据库

        // 存es
        eSearchUtils.doc.createOrUpdate(indexName, messageDTO.getId(), messageDTO);

        return Result.ok(BaseUtil.encode(R.ok(messageDTO)));
    }

    @PostMapping("/getMsg")
    public Result<?> getMsg() {
        List<Object> list = eSearchUtils.doc.query(indexName, null, null, 0);
        for (Object obj : list) {
            Map m = (Map) obj;

            if (ObjectUtil.isNotEmpty(m.get("userId"))) {
                User user = userMapper.getUserById(m.get("userId").toString());
                m.put("userName",user.getUserName());
            } else {
                m.put("userName","游客");
            }
        }
        return Result.ok(BaseUtil.encode(R.ok(list)));
    }
}
