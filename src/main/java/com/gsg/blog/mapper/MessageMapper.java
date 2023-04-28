package com.gsg.blog.mapper;

import com.gsg.blog.model.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gsg.blog.vo.MessageVO;

import java.util.List;

/**
 * <p>
 * 留言弹幕表 Mapper 接口
 * </p>
 *
 * @author shuaigang
 * @since 2023-04-27
 */
public interface MessageMapper extends BaseMapper<Message> {

    /**
     * 获取留言列表
     * @return  list
     */
    List<MessageVO> getMessageList();

}
