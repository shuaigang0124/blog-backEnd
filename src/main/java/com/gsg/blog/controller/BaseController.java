package com.gsg.blog.controller;

import com.gsg.blog.vo.PageVo;
import lombok.extern.slf4j.Slf4j;

/**
 * TODO 公共的一些Controller层方法可以写在这里便于使用
 *
 * @author shauigang
 * @date 2023年3月6日  16点13分
 **/
@Slf4j
public abstract class BaseController {

    /**
     * TODO 处理page返回参数
     *
     * @author shuaigang
     **/
    protected PageVo pageVo(Integer index, Integer size, Integer pageSize, Integer total, Object data) {
        PageVo pageVo = new PageVo();
        pageVo.setPageNum(index)
                .setPageSize(pageSize)
                .setPages(total == 0 ? 0 : total / size + (total % size == 0 ? 0 : 1))
                .setTotal(total)
                .setList(data);
        return pageVo;
    }

}