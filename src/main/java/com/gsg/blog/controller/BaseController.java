package com.gsg.blog.controller;

import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
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
    protected PageVo pageVo(Integer pageNum, Integer pageSize, Integer size, Integer total, Object data) {
        PageVo pageVo = new PageVo();
        pageVo.setPageNum(pageNum)
                .setPageSize(size)
                .setPages(total == 0 ? 0 : total / pageSize + (total % pageSize == 0 ? 0 : 1))
                .setTotal(total)
                .setList(data);
        return pageVo;
    }

    protected Result<?> result(R<?> r) {
        return Result.ok(BaseUtil.encode(r));
    }

}