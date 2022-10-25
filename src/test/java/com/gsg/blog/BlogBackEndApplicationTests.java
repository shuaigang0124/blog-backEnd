package com.gsg.blog;

import cn.hutool.core.date.DateUtil;
import co.elastic.clients.elasticsearch.indices.IndexState;
import com.gsg.blog.dto.EsPage;
import com.gsg.blog.dto.MessageDTO;
import com.gsg.blog.dto.UserDTO;
import com.gsg.blog.mapper.UserMapper;
import com.gsg.blog.model.User;
import com.gsg.blog.utils.DateFormateUtils;
import com.gsg.blog.utils.ESearchUtils;
import com.gsg.blog.utils.PKGenerator;
import com.gsg.blog.utils.Page;
import com.gsg.blog.vo.UserVO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class BlogBackEndApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    ESearchUtils esUtil;

    @Autowired
    UserMapper userMapper;

    // 目标索引
    String indexName = "shuaigang";

    @Test
    public void getAllUserInsertES() {
        List<UserVO> allUser = userMapper.getAllUser();
        Map<String, Object> map = new HashMap<>();
        for (UserVO user : allUser) {
            System.out.println(user.getBirthday());
            map.put(user.getId(),user);
        }
        System.out.println(allUser.size());
        esUtil.doc.createOrUpdateBth("user", map);
    }


    // --------------------------- 工具类方法 ---------------------------------
    // -----创建索引-----
    @Test
    public void testCreateIndexByUtil() {
        esUtil.index.create("user");
    }

    // -----查询索引-----
    @Test
    public void testQueryIndexByUtil() {
        Map<String, IndexState> result = esUtil.index.query("shuaigang");
        System.out.println(result);
    }

    // -----查询全部索引-----
    @Test
    public void testGetAllIndex() {
        Set<String> idxs = esUtil.index.all();
        for (String idx : idxs) {
            System.out.println(idx);
        }
    }

    // -----删除索引-----
    @Test
    public void testDeleteIndexByUtil() {
        boolean b = esUtil.index.del("test");
        System.out.println(b);
    }

    // -----创建或更新文档-----
    @Test
    public void testCreateDocument() {
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setId("MSG" + PKGenerator.generate())
                .setUserId("GSG1")
                .setColor("red")
                .setContent("测试~~~")
                .setGmtCreate(new Date())
                .setGmtModified(new Date())
                .setDeleted(0);
        String res = esUtil.doc.createOrUpdate("message", messageDTO.getId(), messageDTO);
        System.out.println(res);
    }

    // -----批量创建或更新文档-----
    @Test
    public void testBatchCreateDocument() {
        Map<String, Object> userMap = new HashMap<>();
        User user = new User();
        user.setId("test8")
                .setUserName("帅刚")
                .setSex(1)
                .setAddress("帅刚")
                .setAvatar("/state/帅刚/1.png")
                .setBirthday(DateFormateUtils.asLocalDate(new Date()))
                .setEmail("帅刚@gsg.com")
                .setPassword("帅刚")
                .setPhone("15988888888帅刚")
                .setRole("admin帅刚")
                .setGmtCreate(DateFormateUtils.asLocalDateTime(new Date()))
                .setGmtModified(DateFormateUtils.asLocalDateTime(new Date()));
        userMap.put(user.getId(), user);
        User user1 = new User();
        user1.setId("test6")
                .setUserName("帅刚ovo")
                .setSex(0)
                .setAddress("帅刚")
                .setAvatar("/state/imag帅刚e/123.png")
                .setBirthday(DateFormateUtils.asLocalDate(new Date()))
                .setEmail("gsg0124@帅刚gsg.com")
                .setPassword("123帅刚")
                .setPhone("188888888帅刚88")
                .setRole("u帅刚ser")
                .setGmtCreate(DateFormateUtils.asLocalDateTime(new Date()))
                .setGmtModified(DateFormateUtils.asLocalDateTime(new Date()));
//                // 国际时区
//                .setGmtModified(DateFormateUtils.getUtcCurrentLocalDateTime());
        userMap.put(user1.getId(), user1);

        int i = esUtil.doc.createOrUpdateBth(indexName, userMap);
        System.out.println(i);
    }

    // -----根据id查询索引中是否存在文档-----
    @Test
    public void testDocIsExist() {
        //System.out.println(EsClientUtil.docIsExist(indexName, "8001"));
        System.out.println(esUtil.doc.isExist(indexName, "test105"));
    }

    // -----根据ids批量删除文档-----
    @Test
    public void testDeleteDocument() {
        List<String> ids = Arrays.asList(
                "MSG20221025472600104");

        int i = esUtil.doc.del("message", ids);
        System.out.println(i);
    }

    // -----删除文档所有内容及文档-----
    @Test
    public void testDocDelAll() {
        esUtil.doc.delAll("user");
    }

    // -----文章关键字查询,查询所有-----
    @Test
    public void testQueryDocument() {
        String[] query = {"email", "avatar", "userName"};
        Page page = new Page();
        page.setFrom(0);
        page.setSize(10);
        // 查询所有
        List<Object> docs = esUtil.doc.query(indexName, "帅刚", query, 0);
        // 分页查询
//        List<Object> docs = esUtil.doc.queryPage(indexName, null, null, page);

        for (Object doc : docs) {
            System.out.println(doc);
        }
        System.out.println(docs.size());
    }

    // -----分页关键字查询-----
    @Test
    public void testDocPage() {
        //EsPage p = esUtil.doc.page(indexName, "中", 1, 5);
        String[] fields = {"email", "avatar", "userName", "title", "content"};
        EsPage p = esUtil.doc.page(indexName, "1", 1, 20, fields);
//        EsPage p2 = esUtil.doc.page(indexName, "1", "admin67",1, 20,fields);
        for (Object record : p.getRecords()) {
            System.out.println(record);
        }
    }

    // -----查询距离当前最近的文档的时间值-----
    @Test
    public void testDocLastTime() {
        //EsPage p = esUtil.doc.page(indexName, "中", 1, 5);
        //EsPage p = esUtil.doc.page(indexName, "中", "admin",1, 5);
        //EsPage p = esUtil.doc.page(indexName, "中");
        Long lastTime = esUtil.doc.lastTime(indexName, 1);
        System.out.println(lastTime);
    }

}
