package com.polar.elasticsearch.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.polar.elasticsearch.bean.User;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.MaxAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;

import java.io.IOException;
import java.util.Arrays;

public class ESClient {
    public static void main(String[] args) throws IOException {

        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));

//        //创建索引
//        CreateIndexRequest request = new CreateIndexRequest("user");
//        CreateIndexResponse indexResponse = client.indices().create(request, RequestOptions.DEFAULT);
//        boolean acknowledged = indexResponse.isAcknowledged();//响应状态

//        //索引查询
//        GetIndexRequest request = new GetIndexRequest("user");
//        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
//        System.out.println(response.getMappings());

//        //数据插入
//        IndexRequest request = new IndexRequest();
//        request.index("user").id("1000");
//        User user = new User("zhangsan","1",30);
//
//        ObjectMapper mapper = new ObjectMapper();
//        String userJson = mapper.writeValueAsString(user);
//        request.source(userJson, XContentType.JSON);
//
//        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
//        System.out.println(response.getResult());

        //数据字段更新
//        UpdateRequest request = new UpdateRequest();
//        request.index("user").id("1000");
//        request.doc(XContentType.JSON, "sex", "男");
//        UpdateResponse response = client.update(request, RequestOptions.DEFAULT);

        //数据查询
//        GetRequest request = new GetRequest();
//        request.index("user").id("1000");
//        GetResponse response = client.get(request, RequestOptions.DEFAULT);
//        System.out.println(response.getSourceAsString());

//        //批量插入数据
//        BulkRequest request = new BulkRequest("user");
//        Gson gson = new Gson();
//        User user = new User();
//        user.setName("lisi");
//        user.setSex("男");
//        for (int i = 0; i < 10; i++) {
//            user.setAge(i);
//            request.add(new IndexRequest().id(String.valueOf(i)).source(gson.toJson(user), XContentType.JSON));
//        }
//        BulkResponse responses = client.bulk(request, RequestOptions.DEFAULT);


        //数据查询
        SearchRequest request = new SearchRequest();
        request.indices("user");

        //全部查询
//        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));
//        request.source(new SearchSourceBuilder().query(QueryBuilders.termQuery("age", 1)));//条件查询
//        SearchSourceBuilder builder = new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()).from(0).size(3);
//        builder.sort("age", SortOrder.DESC);//降序排序
//        request.source(builder);//分页查询

        //组合查询
        SearchSourceBuilder builder = new SearchSourceBuilder();
        //多条件组合查询
//        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
//        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "zhangsan"));
//        builder.query(boolQueryBuilder);

        //高亮查询
//        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "lisi");
//        HighlightBuilder highlightBuilder = new HighlightBuilder();
//        highlightBuilder.preTags("<font color = 'red'>");
//        highlightBuilder.postTags("</font>");
//        highlightBuilder.field("name");
//        builder.highlighter(highlightBuilder);
//        builder.query(termQueryBuilder);

        //聚合查询
//        MaxAggregationBuilder aggregationBuilder = AggregationBuilders.max("maxAge").field("age");
//        builder.aggregation(aggregationBuilder);

        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("ageGroup").field("age");
        builder.aggregation(aggregationBuilder);
        request.source(builder);


        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
        System.out.println(searchResponse.getHits().getTotalHits());
        for (SearchHit hit : searchResponse.getHits()) {
            System.out.println(hit.getSourceAsString());
        }

        client.close();
    }
}
