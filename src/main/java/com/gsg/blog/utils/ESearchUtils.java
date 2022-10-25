package com.gsg.blog.utils;

import cn.hutool.core.date.DateUtil;
import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldSort;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.ExistsRequest;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import co.elastic.clients.elasticsearch.core.bulk.DeleteOperation;
import co.elastic.clients.elasticsearch.core.bulk.IndexOperation;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.*;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.alibaba.fastjson.JSON;
import com.gsg.blog.dto.EsPage;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>ES操作工具类</p>
 * -----------------------------
 * 调用异步客户端就是异步方法；
 * 异步方法也要关闭transport；
 * 如果连接太多，又没有及时关闭，会报异常
 * <p>
 * 默认：异步方法
 * 索引名称/Id：一律转为小写
 * 文档Id：可以为大写，无须转换
 * -----------------------------
 *
 * @author 土味儿
 * Date 2022/10/17
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("all")
@Component
public class ESearchUtils {
    public Index index = new Index();
    public Doc doc = new Doc();

    // ===================== 索引操作（封装在Index内部类中） ============================

    public class Index {
        /**
         * 创建索引（同步）
         *
         * @param indexName
         * @return true：成功，false：失败
         */
        @SneakyThrows
        public Boolean createSync(String indexName) {
            // 索引名称转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            //String iName = indexName;

            // 获取【索引客户端对象】
            ElasticsearchIndicesClient indexClient = getEsClient().indices();

            /**
             * ===== 判断目标索引是否存在（等价于下面的Lambda写法）=====
             * ---- 1、构建【存在请求对象】
             * ExistsRequest existsRequest = new ExistsRequest.Builder().index(indexName).build();
             * ---- 2、判断目标索引是否存在
             * boolean flag = indexClient.exists(existsRequest).value();
             */
            boolean flag = indexClient.exists(req -> req.index(iName)).value();

            //CreateIndexResponse createIndexResponse = null;
            boolean result = false;
            if (flag) {
                // 目标索引已存在
                //System.out.println("索引【" + indexName + "】已存在！");
                log.info("索引【" + iName + "】已存在！");
            } else {
                // 不存在
                // 获取【创建索引请求对象】
                //CreateIndexRequest createIndexRequest = new CreateIndexRequest.Builder().index(indexName).build();
                // 创建索引，得到【创建索引响应对象】
                //CreateIndexResponse createIndexResponse = indexClient.create(createIndexRequest);
                //createIndexResponse = indexClient.create(req -> req.index(indexName));
                result = indexClient.create(req -> req.index(iName)).acknowledged();

                //System.out.println("创建索引响应对象：" + createIndexResponse);
                if (result) {
                    log.info("索引【" + iName + "】创建成功！");
                } else {
                    log.info("索引【" + iName + "】创建失败！");
                }
            }

            // 关闭transport
            close();

            return result;
        }

        /**
         * 创建索引（异步）
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Boolean create(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            // 异步索引客户端
            ElasticsearchIndicesAsyncClient indexClient = getEsAsyncClient().indices();

            // 查询索引是否存在；get()方法阻塞
            boolean isExist = indexClient.exists(
                    existsRequest -> existsRequest.index(iName)
            ).get().value();

            // 创建索引
            boolean result = false;
            if (isExist) {
                log.info("索引【" + iName + "】已存在！");
            } else {
                // 当前索引不存在，创建索引
                result = indexClient.create(
                        createIndexRequest -> createIndexRequest.index(iName)
                ).get().acknowledged();

                if (result) {
                    log.info("索引【" + iName + "】创建成功！");
                } else {
                    log.info("索引【" + iName + "】创建失败！");
                }
            }

            // 关闭transport
            close();

            return result;
        }

        /**
         * 查询索引（同步）
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Map<String, IndexState> query(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            // 获取【索引客户端对象】
            ElasticsearchIndicesClient indexClient = getEsClient().indices();

            // 查询结果；得到【查询索引响应对象】
            GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index(iName).build();
            //GetIndexRequest getIndexRequest = new GetIndexRequest.Builder().index("*").build();

            GetIndexResponse getIndexResponse = indexClient.get(getIndexRequest);
            //GetIndexResponse getIndexResponse = indexClient.get(req -> req.index(iName));

            // 关闭transport
            close();

            return getIndexResponse.result();
        }

        /**
         * 查询索引（异步）
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Map<String, IndexState> queryAsync(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            //getEsAsyncClient().indices().get()
            Map<String, IndexState> result = getEsAsyncClient().indices().get(req -> req.index(iName)).get().result();

            // 关闭transport
            close();

            return result;
        }

        /**
         * 查询索引是否存在
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Boolean isExist(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            // 请求对象
            co.elastic.clients.elasticsearch.indices.ExistsRequest existsRequest =
                    new co.elastic.clients.elasticsearch.indices.ExistsRequest.Builder().index(iName).build();
            // 异步查询
            boolean b = getEsAsyncClient().indices().exists(existsRequest).get().value();

            // 关闭transport
            close();

            return b;
        }

        /**
         * 查询全部索引
         *
         * @return 索引名称 Set 集合
         */
        @SneakyThrows
        public Set<String> all() {
            GetIndexResponse getIndexResponse = getEsClient().indices().get(req -> req.index("*"));

            close();

            return getIndexResponse.result().keySet();
        }

        /**
         * 删除索引
         *
         * @param indexName
         * @return
         */
        @SneakyThrows
        public Boolean del(String indexName) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);
            // 获取【索引客户端对象】
            //ElasticsearchIndicesClient indexClient = getEsClient().indices();
            // 【删除索引响应对象】
            //DeleteIndexResponse deleteIndexResponse = getEsClient().indices().delete(req -> req.index(iName));
            DeleteIndexResponse deleteIndexResponse = getEsAsyncClient().indices().delete(req -> req.index(iName)).get();

            // 关闭transport
            close();

            return deleteIndexResponse.acknowledged();
        }
    }

    // ===================== 文档异步操作（封装在Doc内部类中） ============================

    public class Doc {
        /**
         * 创建/更新文档（异步）
         * 存在：
         *
         * @param indexName  索引名称
         * @param documentId 文档ID
         * @param esDocument 文档内容
         * @return 不存在：created、存在：updated
         */
        @SneakyThrows
        public String createOrUpdate(String indexName, String docId, Object obj) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 可创建/可更新
            IndexRequest<Object> indexRequest = new IndexRequest.Builder<Object>()
                    .index(iName)
                    .id(docId)
                    .document(obj)
                    .build();

            // 不存在：created、存在：updated
            String s = getEsAsyncClient().index(indexRequest).get().result().jsonValue();

            // 关闭transport
            close();

            return s;
        }

        /**
         * 批量创建/更新文档（异步）
         * 存在就更新，不存在就创建
         *
         * @param indexName 索引名称
         * @param userMap   文档Map，格式：(文档id : 文档)
         * @return 成功操作的数量
         */
        @SneakyThrows
        public Integer createOrUpdateBth(String indexName, Map<String, Object> docMap) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 批量操作对象集合
            List<BulkOperation> bs = new ArrayList<>();

            // 构建【批量操作对象】，并装入list集合中
            docMap.entrySet().stream().forEach(docEntry -> {
                // 操作对象（可新建/可更新）
                IndexOperation<Object> idxOpe = new IndexOperation.Builder<Object>()
                        // 文档id
                        .id(docEntry.getKey())
                        // 文档内容
                        .document(docEntry.getValue())
                        .build();

                // 构建【批量操作对象】
                BulkOperation opt = new BulkOperation.Builder().index(idxOpe).build();
                // 装入list集合
                bs.add(opt);
            });

            // 构建【批理请求对象】
            BulkRequest bulkRequest = new BulkRequest.Builder()
                    // 索引
                    .index(iName)
                    // 批量操作对象集合
                    .operations(bs)
                    .build();

            // 批量操作
            BulkResponse bulkResponse = getEsAsyncClient().bulk(bulkRequest).get();

            int i = bulkResponse.items().size();

            //log.info("成功处理 {} 份文档！", i);

            // 关闭transport
            close();

            return i;
        }

        /**
         * 检测文档是否存在
         *
         * @param indexName
         * @param documentId
         * @return
         */
        @SneakyThrows
        public Boolean isExist(String indexName, String docId) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            ExistsRequest existsRequest = new ExistsRequest.Builder()
                    .index(iName)
                    .id(docId)
                    .build();
            boolean b = getEsAsyncClient().exists(existsRequest).get().value();

            // 关闭transport
            close();

            return b;
        }

        /**
         * 查询距离当前最近的文档的时间值
         * ----------------------------
         * -1：索存不存在；
         * 0：该类型的文档不存在
         * >1：该类型的文档中最近的时间（秒值）
         * ----------------------------
         *
         * @param indexName 索引名称
         * @param docType   文档类型
         * @return
         */
        @SneakyThrows
        public Long lastTime(String indexName, Integer docType) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 先判断索引是否存在；如果不存在，直接返回-1
            if (!index.isExist(iName)) {
                return -1L;
            }

            // 排序字段规则
            FieldSort fs = new FieldSort.Builder()
                    .field("time")
                    .order(SortOrder.Desc)
                    .build();

            // 排序操作项
            SortOptions so = new SortOptions.Builder()
                    .field(fs)
                    .build();

            // 文档类型
            Query byType = TermQuery.of(m -> m
                    // EsDocument的内容字段名
                    .field("type")
                    .value(docType)
            )._toQuery();

            // 查询请求对象
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index(iName)
                    .query(byType)
                    // 可以接收多个值
                    .sort(so)
                    .size(1).build();

            // 异步查询
            SearchResponse<Object> response = getEsAsyncClient().search(searchRequest, Object.class).get();

            // 结果集
            List<Hit<Object>> hits = response.hits().hits();
            // 判断该类型的文档是否存在
            if (hits.size() < 1) {
                // 关闭transport
                close();
                return 0L;
            }

            // 时间最近的文档
            Object doc = hits.stream().findFirst().get().source();

            // 关闭transport
            close();
            // 返回时间值（秒）
//            return doc.getTime();
            return Long.valueOf(JSON.parseObject(doc.toString()).getString("time"));
        }

        @SneakyThrows
        public List<Object> queryById(String indexName, String id) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // ---------------- lambda表达式写法（嵌套搜索查询）------------------

            SearchResponse<Object> response = null;

            // 异步
            response = getEsAsyncClient().search(s -> s
                            .index(iName)
                            .query(q -> q
                                    // boolean 嵌套搜索；must需同时满足，should一个满足即可
                                    .bool(b -> b
                                            .must(MatchQuery.of(m -> m
                                                    // 标题字段名
                                                    .field("id")
                                                    .query(id))._toQuery())
                                    )
                            ).size(1),
                    Object.class
            ).get();

            List<Hit<Object>> hits = response.hits().hits();
            List<Object> docs = hits.stream().map(hit -> hit.source()).collect(Collectors.toList());

            // 关闭transport
            close();
            return docs;
        }

        /**
         * 查所有文档（包含关键字查询）
         * （只要标题和内容中有一个匹配即可）
         *
         * @param indexName 索引名称
         * @param keyword   关键字
         * @return List 集合
         */
        @SneakyThrows
        public List<Object> query(String indexName, String keyword, String[] query, Integer from) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // ---------------- lambda表达式写法（嵌套搜索查询）------------------

            MatchQuery of = null;
            SearchResponse<Object> response = null;
            Integer size = 10000;
//            if (ObjectUtils.isNotEmpty(query) && StringUtils.isNotEmpty(keyword)) {
//                for (String q : query) {
//                    of = MatchQuery.of(m -> m
//                            // 标题字段名
//                            .field(q)
//                            .query(keyword)
//                    );
//                }
//
//                // 查找
//                Query byQuery = of._toQuery();
//
//                // 异步
//                response = getEsAsyncClient().search(s -> s
//                                .index(iName)
//                                .query(q -> q
//                                                // boolean 嵌套搜索；must需同时满足，should一个满足即可
//                                                .bool(b -> b
//                                                                //.must(byQuery )
//                                                                //.must(byContent)
//                                                                .should(byQuery)
////                                            .should(byContent)
//                                                )
//                                ),
//                        Object.class
//                ).get();
//            } else {
//                response = getEsAsyncClient().search(s -> s
//                                .index(iName)
//                                .query(q -> q
//                                        .matchAll(m -> m))
//                                .size(10000),
//                        Object.class
//                ).get();
//            }
            if (ObjectUtils.isNotEmpty(query) && StringUtils.isNotEmpty(keyword)) {

                for (String q : query) {
                    of = MatchQuery.of(m -> m
                            // 标题字段名
                            .field(q)
                            .query(keyword)
                    );
                }

                // 查找
                Query byQuery = of._toQuery();

                response = getEsAsyncClient()
                        .search(a -> a.index(iName)
                                        .query(q -> q
                                                // boolean 嵌套搜索；must需同时满足，should一个满足即可
                                                .bool(b -> b.should(byQuery))
                                        )
                                        .from(from)
                                        .size(size)
                                        //降序排序
//                                        .sort(b -> b.field(c -> c.field("deleted").order(SortOrder.Desc)))
//                                        .source(c -> c.filter(d -> d
//                                                //不包含字段
//                                                .excludes("id", "age")
//                                                //包含字段
//                                                .includes("name")
//                                        ))
                                , Object.class).get();
            } else {
                response = getEsAsyncClient()
                        .search(a -> a.index(iName)
                                        .from(from)
                                        .size(size)
                                        //降序排序
                                        .sort(b -> b.field(c ->
                                                c.field("deleted")
                                                        .unmappedType(FieldType.Date)
                                                        .order(SortOrder.Desc))
                                        )
                                , Object.class).get();
            }
            List<Hit<Object>> hits = response.hits().hits();
            List<Object> docs = hits.stream().map(hit -> hit.source()).collect(Collectors.toList());

            if (hits.size() >= size) {
                List<Object> list = doc.query(indexName, keyword, query, from + size);
                docs.addAll(list);
            }

            // 关闭transport
            close();
            return docs;
        }

        /**
         * 分页查询文档（包含关键字查询）
         * （只要标题和内容中有一个匹配即可）
         *
         * @param indexName 索引名称
         * @param keyword   关键字
         * @return List 集合
         */
        @SneakyThrows
        public List<Object> queryPage(String indexName, String keyword, String[] query, Page page) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // ---------------- lambda表达式写法（嵌套搜索查询）------------------
            MatchQuery of = null;
            SearchResponse<Object> response = null;

            if (ObjectUtils.isNotEmpty(query) && StringUtils.isNotEmpty(keyword)) {
                for (String q : query) {
                    of = MatchQuery.of(m -> m
                            // 标题字段名
                            .field(q)
                            .query(keyword));
                }

                // 查找
                Query byQuery = of._toQuery();

                response = getEsAsyncClient()
                        .search(a -> a.index(iName)
                                        .query(q -> q
                                                // boolean 嵌套搜索；must需同时满足，should一个满足即可
                                                .bool(b -> b.should(byQuery))
                                        )
                                        .from(page.getFrom())
                                        .size(page.getSize())
                                        //按创建时间降序排序
                                        .sort(b -> b.field(c -> c.field("gmtCreate").order(SortOrder.Desc)))
                                        .source(c -> c.filter(d -> d
                                                        //不包含字段
                                                        .excludes("gmtModified")
                                                //包含字段
//                                                .includes("name")
                                        ))
                                , Object.class).get();
            } else {
                response = getEsAsyncClient()
                        .search(a -> a
                                        .index(iName)
                                        .from(page.getFrom())
                                        .size(page.getSize())
                                        //按创建时间降序排序
                                        .sort(b -> b.field(c -> c.field("gmtCreate").order(SortOrder.Desc)))
                                        .source(c -> c.filter(d -> d
                                                        //不包含字段
                                                        .excludes("gmtModified")
                                                //包含字段
//                                                    .includes("name")
                                        ))
                                , Object.class).get();
            }
            List<Hit<Object>> hits = response.hits().hits();
//            List<Object> docs = hits.stream().map(hit -> getEsDocVo(hit.source())).collect(Collectors.toList());
            List<Object> docs = hits.stream().map(hit -> hit.source()).collect(Collectors.toList());

            // 关闭transport
            close();
            return docs;
        }

        /**
         * 【分页查找】根据关键字查文档
         * ---------------------------
         * 只要标题和内容中有一个匹配即可
         * 默认当前页：1
         * 默认页面记录数：10
         * 支持高亮
         * ---------------------------
         *
         * @param indexName 索引名称
         * @param keyword   关键字
         * @return List 集合
         */
        @SneakyThrows
        public EsPage page(
                String indexName,
                String keyword,
                String[] fields
        ) {
            return page(indexName, keyword, 1, 30, fields);
        }

        /**
         * 【分页查找】根据关键字查文档
         * ---------------------------
         * 只要标题和内容中有一个匹配即可
         * 支持高亮
         * ---------------------------
         *
         * @param indexName 索引名称
         * @param keyword   关键字
         * @param current   当前页
         * @param pageSize  页面记录数
         * @return EsPage 对象
         */
        @SneakyThrows
        public EsPage page(
                String indexName,
                String keyword,
                Integer current,
                Integer pageSize,
                String[] fields
        ) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            int c = Objects.isNull(current) ? 1 : current;
            int p = Objects.isNull(pageSize) ? 30 : pageSize;

            // 构建EsPage
            EsPage esPage = new EsPage();
            esPage.setKeyword(keyword);
            esPage.setCurrent(c);
            esPage.setPageSize(p);

            // 文档对象集合；实现高亮
            List<Object> docs = new ArrayList<>();
            // 判断关键字
            if (StringUtils.isEmpty(keyword)) {
                // keyword为空，返回空esPage
                esPage.setKeyword("");
                esPage.setTotal(0L);
                esPage.setRecords(docs);
                return esPage;
            }
            String kw = keyword.trim();

            // 判断indexName是否存在
            if (!index.isExist(iName)) {
                // 索引不存在，返回空的EsPage对象
                esPage.setTotal(0L);
                esPage.setRecords(docs);
                return esPage;
            }

        /*MatchQuery matchQuery = new MatchQuery.Builder()
                .field(fieldName)
                .query(fieldValue)
                .build();

        Query query = new Query.Builder()
                .match(matchQuery)
                .build();

        //SearchRequest searchRequest = new SearchRequest.Builder().index(indexName).query(query).build();
        SearchRequest searchRequest = new SearchRequest.Builder().index(indexName).query(query).build();

        SearchResponse<EsDocument> searchResponse = getEsClient().search(searchRequest, EsDocument.class);
        */

            // ---------------- lambda表达式写法（嵌套搜索查询）------------------

            // 多条件查询（从title或content中查询keyword）
            Query byKeyword = MultiMatchQuery.of(m -> m
                    .fields(Arrays.asList(fields))
                    //.fields("title")
                    .query(kw)
            )._toQuery();

            // 起始文档值（从0开始）
            Integer from = (c - 1) * p;

            // 存放高亮的字段，默认与文档字段一致
            HighlightField hf = new HighlightField.Builder().build();

            Highlight highlight = new Highlight.Builder()
                    // 前后缀默认就是em，可省略
                    //.preTags("<em>")
                    //.postTags("</em>")
                    .fields("title", hf)
                    .fields("content", hf)
                    .requireFieldMatch(false)
                    .build();

            // 异步
            SearchResponse<Object> response = getEsAsyncClient().search(s -> s
                            .index(iName)
                            .query(byKeyword)
                            .highlight(highlight)
                            .from(from).size(p),
                    Object.class
            ).get();

            // 构建EsPage
            esPage.setTotal(response.hits().total().value());

            // 查询结果
            List<Hit<Object>> hits = response.hits().hits();

            // 流式遍历查询结果：用高亮字段替换原文档字段
            hits.stream().forEach(hit -> {
                // 原文档
                Object doc = hit.source();
                // 高亮标题字段
                List<String> titles = hit.highlight().get("title");
                if (!CollectionUtils.isEmpty(titles)) {
                    // 替换原标题
                    JSON.parseObject(doc.toString()).put("title", titles.get(0));
//                    doc.setTitle(titles.get(0));
                }
                // 高亮内容字段
                List<String> contents = hit.highlight().get("content");
                if (!CollectionUtils.isEmpty(contents)) {
                    // 替换原内容
                    JSON.parseObject(doc.toString()).put("content", titles.get(0));
//                    doc.setContent(contents.get(0));
                }

                // 原文档转为VO，加入VO对象集合中
                docs.add(getEsDocVo(doc));
            });

            // VO对象集合注入page对象
            esPage.setRecords(docs);

            // 关闭transport
            close();

            // 返回page
            return esPage;
        }

        /**
         * 【分页查找】根据属主、文档类型、关键字查文档
         * 支持高亮
         * ---------------------------
         * 1、公共文档：类型 0；任何人都可以查询，不需要比对属主
         * 2、非公共文档：类型 1、2、3.；有限制查询，只有文档属主可以查询；如：tom的文档，只有tom可以查询
         * 3、关键字：只要标题和内容中有一个匹配即可
         * ---------------------------
         * 查询中文与英文的匹别：
         * 1、中文：单个汉字为一个词；如：中国，可以分为：中、国，有一个比对上就算成功
         * 2、英文：一个单词为一个词；
         * ---------------------------
         * 注意：
         * 属主名称选择时，不要用中文，全部用英文，且有固定格式，不可修改
         * ---------------------------
         *
         * @param indexName 索引名称
         * @param keyword   关键字
         * @param owner     文档属主
         * @param current   当前页
         * @param pageSize  页面记录数
         * @return EsPage 对象
         */
        @SneakyThrows
        public EsPage page(
                String indexName,
                String keyword,
                String owner,
                Integer current,
                Integer pageSize,
                String[] fields
        ) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            int c = Objects.isNull(current) ? 1 : current;
            int p = Objects.isNull(pageSize) ? 30 : pageSize;

            // 构建EsPage
            EsPage esPage = new EsPage();
            esPage.setKeyword(keyword);
            esPage.setCurrent(c);
            esPage.setPageSize(p);

            // 文档VO对象集合；实现高亮
            List<Object> docs = new ArrayList<>();
            // 判断关键字
            if (StringUtils.isEmpty(keyword)) {
                // keyword为空，返回空esPage
                esPage.setKeyword("");
                esPage.setTotal(0L);
                esPage.setRecords(docs);
                return esPage;
            }
            String kw = keyword.trim();
            // 判断indexName是否存在
            if (!index.isExist(iName)) {
                // 索引不存在，返回空的EsPage对象
                esPage.setTotal(0L);
                esPage.setRecords(docs);
                return esPage;
            }

            // ---------------- 查询字段 ---------------
            // 多条件查询（从title或content中查询keyword）lambda表达式写法（嵌套搜索查询
            Query byKeyword = MultiMatchQuery.of(m -> m
                    .fields(Arrays.asList(fields))
                    .query(kw)
            )._toQuery();

            // ----------- 文档类型（范围查找）-----------
            // gt 大于，gte 大于等于，lt 小于，lte 小于等于

            // 文档类型（公共文档）
            Query byType1 = RangeQuery.of(m -> m
                    .field("type")
                    // 类型小于1
                    .lt(JsonData.of(1))
            )._toQuery();

            // 文档类型（非公共文档）
            Query byType2 = RangeQuery.of(m -> m
                    .field("type")
                    // 类型大于0
                    .gt(JsonData.of(0))
            )._toQuery();

            // --------------- 文档属主 ---------------
            // 文档属主（属主名称完全匹配）
            Query byOwner = TermQuery.of(m -> m
                    // EsDocument的内容字段名
                    .field("owner")
                    .value(owner)
            )._toQuery();

            // 起始文档值（从0开始）
            Integer from = (c - 1) * p;

            // --------------- 高亮显示 ---------------

            // 存放高亮的字段，默认与文档字段一致
            HighlightField hf = new HighlightField.Builder().build();

            Highlight highlight = new Highlight.Builder()
                    // 前后缀默认就是em，可省略
                    //.preTags("<em>")
                    //.postTags("</em>")
                    .fields("title", hf)
                    .fields("content", hf)
                    .requireFieldMatch(false)
                    .build();

            // 异步查询
            SearchResponse<Object> response = getEsAsyncClient().search(s -> s
                            .index(iName)
                            .query(q -> q
                                    // 布尔比较：有一个条件满足即可
                                    .bool(b -> b
                                            // 条件一：must：两个子条件都满足时，条件才成立；【公共文档】
                                            .should(sq1 -> sq1.bool(sqb1 -> sqb1.must(byType1, byKeyword)))
                                            // 条件二：must：三个子条件都满足时，条件才成立；【私有文档】
                                            .should(sq2 -> sq2.bool(sqb2 -> sqb2.must(byType2, byOwner, byKeyword)))
                                    )
                            ).highlight(highlight)
                            .from(from).size(p),
                    Object.class
            ).get();

            // 查询结果
            List<Hit<Object>> hits = response.hits().hits();

            // 构建EsPage
            esPage.setTotal(response.hits().total().value());

            // 流式遍历查询结果：用高亮字段替换原文档字段
            hits.stream().forEach(hit -> {
                // 原文档
                Object doc = hit.source();

                // 高亮标题字段
                List<String> titles = hit.highlight().get("title");
                if (!CollectionUtils.isEmpty(titles)) {
                    // 替换原标题
                    JSON.parseObject(doc.toString()).put("title", titles.get(0));
//                    doc.setTitle(titles.get(0));
                }

                // 高亮内容字段
                List<String> contents = hit.highlight().get("content");
                if (!CollectionUtils.isEmpty(contents)) {
                    // 替换原内容
                    JSON.parseObject(doc.toString()).put("content", titles.get(0));
//                    doc.setContent(contents.get(0));
                }

                // 原文档转为VO，加入VO对象集合中
                docs.add(getEsDocVo(doc));
            });

            // VO对象集合注入page对象
            esPage.setRecords(docs);

            // 关闭transport
            close();

            // 返回page
            return esPage;
        }

        /**
         * 批量删除文档
         *
         * @param indexName   索引名称
         * @param documentIds 文档ID集合
         * @return 成功删除数量
         */
        @SneakyThrows
        public Integer del(
                String indexName,
                List<String> docIds
        ) {
            // 转为小写
            String iName = indexName.toLowerCase(Locale.ROOT);

            // 批量操作对象集合
            List<BulkOperation> bs = new ArrayList<>();

            // 构建【批量操作对象】，并装入list集合中
            docIds.stream().forEach(docId -> {
                // 删除操作对象
                DeleteOperation delOpe = new DeleteOperation.Builder().id(docId).build();

                // 构建【批量操作对象】
                BulkOperation opt = new BulkOperation.Builder().delete(delOpe).build();
                // 装入list集合
                bs.add(opt);
            });

            // 构建【批理请求对象】
            BulkRequest bulkRequest = new BulkRequest.Builder()
                    // 索引
                    .index(iName)
                    // 批量操作对象集合
                    .operations(bs)
                    .build();

            // 批量操作
            BulkResponse bulkResponse = getEsAsyncClient().bulk(bulkRequest).get();

            int i = bulkResponse.items().size();

            log.info("成功处理 {} 份文档！", i);

            // 关闭transport
            close();

            return i;
        }

        /**
         * 删除所有文档
         * 实际上删除的是索引
         *
         * @param indexName
         * @return
         */
        public Boolean delAll(String indexName) {
            return index.del(indexName);
        }

        private Object getEsDocVo(Object obj) {

            Map entry = (Map) obj;

            // ------ 时间转换 ------
            // 获取创建时间
            ArrayList<Integer> list = (ArrayList<Integer>) entry.get("gmtCreate");
            if (org.springframework.util.StringUtils.isEmpty(list)) {
                return entry;
            }

            // 获取当前时间与创建时间的时间差（单位：s）
            Date now = new Date();
            Long n = (now.getTime() - DateUtil.parse(list.get(0)
                    + "-" + list.get(1)
                    + "-" + list.get(2)
                    + " " + list.get(3)
                    + ":" + list.get(4)
                    + ":" + list.get(5))
                    .getTime()) / 1000;

            // 秒数
            Long secOfMinute = 60L;
            // Long secOfHour = secOfMinute * 60L;
            Long secOfHour = 3600L;
            // Long secOfDay = secOfHour * 24L;
            Long secOfDay = 86400L;
            // Long secOfWeek = secOfDay * 7L;
            Long secOfWeek = 604800L;
            // Long secOfMonth = secOfDay * 30L;
            Long secOfMonth = 2592000L;
            // Long secOfYear = secOfMonth * 12L;
            Long secOfYear = 31104000L;

            // 时间差字段名
            String releaseTime = "releaseTime";

            if (n > secOfYear) {
                Double floor = Math.floor(n / secOfYear);
                entry.put(releaseTime, floor.intValue() + "年前");
            } else if (n > secOfMonth) {
                Double floor = Math.floor(n / secOfMonth);
                entry.put(releaseTime, floor.intValue() + "个月前");
            } else if (n > secOfWeek) {
                Double floor = Math.floor(n / secOfWeek);
                entry.put(releaseTime, floor.intValue() + "周前");
            } else if (n > secOfDay) {
                Double floor = Math.floor(n / secOfDay);
                entry.put(releaseTime, floor.intValue() + "天前");
            } else if (n > secOfHour) {
                Double floor = Math.floor(n / secOfHour);
                entry.put(releaseTime, floor.intValue() + "小时前");
            } else if (n > secOfMinute) {
                Double floor = Math.floor(n / secOfMinute);
                entry.put(releaseTime, floor.intValue() + "分钟前");
            } else {
                entry.put(releaseTime, "刚刚");
            }

            return entry;
        }
    }

    // ===================== 基础操作（仅供内部调用） ============================

    private ElasticsearchTransport transport;

    @Value("${es.host}")
    private String host;

    @Value("${es.port}")
    private Integer port;

    /**
     * 同步客户端；调用结束后，需调用close()关闭transport
     *
     * @return
     */
    private ElasticsearchClient getEsClient() {
        ElasticsearchClient client = new ElasticsearchClient(getEsTransport());
        return client;
    }

    /**
     * 异步客户端
     *
     * @return
     */
    private ElasticsearchAsyncClient getEsAsyncClient() {
        ElasticsearchAsyncClient asyncClient =
                new ElasticsearchAsyncClient(getEsTransport());
        return asyncClient;
    }

    /**
     * 获取Transport
     *
     * @return
     */
    private ElasticsearchTransport getEsTransport() {
        host = StringUtils.isEmpty(host) ? "localhost" : host;
        port = Objects.isNull(port) ? 9200 : port;

        RestClient restClient = RestClient.builder(
                new HttpHost(host, port)).build();

        // Create the transport with a Jackson mapper
        transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());

        return transport;
    }

    /**
     * 关闭transport
     */
    private void close() {
        if (transport != null) {
            try {
                transport.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

