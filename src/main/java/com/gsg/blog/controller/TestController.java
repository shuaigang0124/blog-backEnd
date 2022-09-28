package com.gsg.blog.controller;

import com.gsg.blog.utils.BaseUtil;
import com.gsg.blog.utils.R;
import com.gsg.blog.utils.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shuaigang
 * @date 2022/9/26 10:30
 */
@RestController
@RequestMapping("/gsg")
public class TestController {

    @PostMapping("/test")
    public Result<?> test() {
        return Result.ok(BaseUtil.encode(R.ok("1111")));
    }
}
