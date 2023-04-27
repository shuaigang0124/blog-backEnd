package com.gsg.blog.service.impl;

import com.gsg.blog.ex.ServiceException;
import com.gsg.blog.model.Message;
import com.gsg.blog.mapper.MessageMapper;
import com.gsg.blog.service.IMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gsg.blog.vo.MessageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 留言弹幕表 服务实现类
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-27
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements IMessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void insertMessage(Message message) {
        int i = messageMapper.insert(message);
        if (i != 1) {
            throw ServiceException.busy();
        }
    }

    @Override
    public List<MessageVO> getMessageList() {
        return messageMapper.getMessageList();
    }
}
