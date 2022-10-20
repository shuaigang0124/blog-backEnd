package com.gsg.blog.controller;

import com.gsg.blog.dto.MessageDTO;
import com.gsg.blog.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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

    // 目标索引
    String indexName = "message";

    @PostMapping("/insertMsg")
    public Result<?> insertMsg(@RequestBody Request<MessageDTO> request) {

        MessageDTO messageDTO = request.getCustomData();

        if (StringUtils.isEmpty(messageDTO.getUserId())) {
            messageDTO.setUserName("游客");
        }

        messageDTO.setId("MSG" + PKGenerator.generate())
                .setDeleted(0)
                .setGmtCreate(DateFormateUtils.asLocalDateTime(new Date()))
                .setGmtModified(DateFormateUtils.asLocalDateTime(new Date()));
        // 存数据库
        String message = eSearchUtils.doc.createOrUpdate(indexName, messageDTO.getId(), messageDTO);

        return Result.ok(BaseUtil.encode(R.ok(message)));
    }

    @PostMapping("/getMsg")
    public Result<?> getMsg() {
        return Result.ok(BaseUtil.encode(R.ok(eSearchUtils.doc.query(indexName,null,null))));
    }
}
