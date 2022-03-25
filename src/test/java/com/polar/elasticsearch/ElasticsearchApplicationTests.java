package com.polar.elasticsearch;

import com.google.gson.Gson;
import com.polar.elasticsearch.bean.Article;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class ElasticsearchApplicationTests {

    @Test
    public void createIndex() throws IOException {
//        RestClient restClient = RestClient.builder(new HttpHost("localhost", 9200, "http")).build();
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        CreateIndexRequest request = new CreateIndexRequest("article");
        request.settings(Settings.builder()
                .put("index.number_of_shards",3)
                .put("index.number_of_replicas",0)
        );
        //同步
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(request, RequestOptions.DEFAULT);

        //异步
        restHighLevelClient.indices().createAsync(request, RequestOptions.DEFAULT, new ActionListener<CreateIndexResponse>() {
            @Override
            public void onResponse(CreateIndexResponse createIndexResponse) {

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
        createIndexResponse.isAcknowledged();
        restHighLevelClient.close();

    }

    @Test
    public void getIndex() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        GetIndexRequest getIndexRequest = new GetIndexRequest("article", "animals");
        GetIndexResponse response = client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
        String[] indices = response.getIndices();
        for (String index : indices) {
            System.out.println("index name : " + index);
        }
        client.close();

    }

    @Test
    public void batchInsert() throws IOException {
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("localhost", 9200, "http")));
        Gson gson = new Gson();
        BulkRequest request = new BulkRequest("article");
        Article article = new Article();
        article.setTitle("haoxiaoxi");
        article.setContent("hello world");
        for (int i = 0; i < 10; i++) {
            article.setAuthor("author" + i);
            request.add(new IndexRequest()
                    .id(Integer.toString(i))
                    .source(gson.toJson(article), XContentType.JSON));
        }
        BulkResponse response = client.bulk(request, RequestOptions.DEFAULT);
        System.out.println("数量" + response.getItems().length);
        client.close();

    }
//    @Test
//    void esCRUD() throws IOException {
//        Settings settings = Settings.builder().put("cluster.name", "elasticsearch").build();
//        TransportClient client = new PreBuiltTransportClient(settings).addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
//        create(client);
//    }

//    private void create(TransportClient client) throws IOException {
//        Article article = new Article();
//        article.setId(1);
//        article.setAuthor("zhangsan");
//        article.setTitle("haoxiaoxi");
//        article.setContent("hello world");
//        IndexResponse response = client.prepareIndex("article", "_doc", String.valueOf(article.getId()))
//                .setSource(XContentFactory.jsonBuilder()
//                        .startObject()
//                        .field("author", article.getAuthor())
//                        .field("title", article.getTitle())
//                        .field("content", article.getContent()).endObject())
//                .get();
//        System.out.println(response.getResult());
//    }


}
