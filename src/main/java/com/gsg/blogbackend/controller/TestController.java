package com.gsg.blogbackend.controller;

import com.gsg.blogbackend.utils.BaseUtil;
import com.gsg.blogbackend.utils.R;
import com.gsg.blogbackend.utils.Result;
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
