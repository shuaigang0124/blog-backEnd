package com.gsg.blog.controller;

import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.Message;
import com.gsg.blog.service.IMessageService;
import com.gsg.blog.utils.*;
import com.gsg.blog.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @Autowired
    private IMessageService messageService;

    /** 目标索引 */
    String indexName = "message";

    @PostMapping("/insertMsg")
    public Result<?> insertMsg(@Valid @RequestBody Message message) {

        if (message.getContent().length() > Constants.CONTENT_MAX) {
            throw ServiceException.errorParams("内容不得超过100个字符！");
        }
        message.setId("MSG" + PkGenerator.generate())
                .setDeleted(0);
        // 存数据库
        messageService.insertMessage(message);
        // 存es
//        eSearchUtils.doc.createOrUpdate(indexName, messageDTO.getId(), messageDTO);

        return Result.ok(BaseUtil.encode(R.ok("留言成功")));
    }

    @PostMapping("/getMsg")
    public Result<?> getMsg() {
//        List<Object> list = eSearchUtils.doc.query(indexName, null, null, 0, null);
//        String userId = null;
//        String userName = null;
//        for (Object obj : list) {
//            Map<String, Object> m = (Map) obj;
//            if (ObjectUtil.isNotEmpty(m.get("userId"))) {
//                if (!m.get("userId").toString().equals(userId)) {
//                    List<Object> user = eSearchUtils.doc.queryById("user", m.get("userId").toString());
//                    Map<?, ?> u = (Map) user.get(0);
//                    m.put("userName", u.get("userName"));
//                    userId = m.get("userId").toString();
//                    userName = u.get("userName").toString();
//                } else {
//                    m.put("userName", userName);
//                }
//            } else {
//                m.put("userName","游客");
//            }
//        }
//
        List<MessageVO> list = messageService.getMessageList();
        return Result.ok(BaseUtil.encode(R.ok(list)));
    }
}
