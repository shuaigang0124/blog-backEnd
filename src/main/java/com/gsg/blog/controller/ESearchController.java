package com.gsg.blog.controller;

import com.alibaba.fastjson.JSON;
import com.gsg.blog.model.User;
import com.gsg.blog.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

/**
 * @author shuaigang
 * @date 2022/9/26 10:30
 */
@RestController
@RequestMapping("/gsg")
public class ESearchController {

//    @Autowired
//    RestHighLevelClient restHighLevelClient;

    @PostMapping("/test")
    public Result<?> test() throws IOException {
        User user = new User();
        user.setUserName("帅刚")
                .setSex(1)
                .setAddress("杭州")
                .setAvatar("/state/image/1.png")
                .setBirthday(DateFormateUtils.getUtcCurrentLocalDate())
                .setEmail("gsg@gsg.com")
                .setPassword("123456")
                .setPhone("15988888888")
                .setRole("admin")
                .setGmtCreate(DateFormateUtils.getUtcCurrentLocalDateTime())
                .setGmtModified(DateFormateUtils.getUtcCurrentLocalDateTime());
//        ESearchUtils.insert("shuaigang", "test-0000001", JSON.toJSONString(user));
//        try{
//            BulkRequest bulkRequest = new BulkRequest();
//            bulkRequest.add(new IndexRequest("test02", null, "")
//                    .source(JSON.toJSONString(user), XContentType.JSON));
//            restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
//        }catch (Exception exception){
//            // 版本异常，不抛出
//            if(!(exception.getMessage()).contains("OK")){
//                throw exception;
//            }
//        }
        return Result.ok(BaseUtil.encode(R.ok("ok")));
    }


}
