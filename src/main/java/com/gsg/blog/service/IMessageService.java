package com.gsg.blog.service;

import com.gsg.blog.model.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gsg.blog.vo.MessageVO;

import java.util.List;

/**
 * <p>
 * 留言弹幕表 服务类
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-27
 */
public interface IMessageService extends IService<Message> {

    void insertMessage(Message message);

    List<MessageVO> getMessageList();

}
