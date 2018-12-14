package com.hr;

import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.stats.StatsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Author: Ru He
 * Date: Created in 2017/12/16.
 * Description:
 */
public class ElasticsearchTest {

    public static void main(String[] args) {
        //get("mydb","user","hZ4fS2ABNkjeFM63Ocan");
        //update("mydb","user","hZ4fS2ABNkjeFM63Ocan");
        ExecutorService service = Executors.newFixedThreadPool(20);
        for(int i = 0; i < 10; i++){
            service.submit(new Runnable() {
                @Override
                public void run() {
                    create("behavior","user");
                }
            });
        }
//        query();
    }

    private static void query(){
        //QuerySearchRequest request = new QuerySearchRequest();
        SearchRequest request = new SearchRequest("behavior");
        QueryBuilder builder = QueryBuilders.termQuery("accid", "a1");

        StatsAggregationBuilder sab = AggregationBuilders.stats("accid_stats").field("accid");
        TermsAggregationBuilder targetAggs = AggregationBuilders.terms("targetid_stats").field("accid");
        TermsAggregationBuilder typeAggs = AggregationBuilders.terms("type_stats").field("type");
        targetAggs.subAggregation(typeAggs);
        //ValueCountAggregationBuilder vca = AggregationBuilders.count("accid_stats").subAggregations(vca);
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(builder).size(10).from(0);
        sourceBuilder.aggregation(targetAggs);
        //sourceBuilder.aggregation(typeAggs).size(0);
        request.source(sourceBuilder);

        RestHighLevelClient client = getClient();
        try {
            SearchResponse response = client.search(request);

            for(SearchHit hit : response.getHits()){
                Map<String, Object> m = hit.getSourceAsMap();
                System.out.println(m);
            }
            //ParsedValueCount s = response.getAggregations().get("accid_stats");

            ParsedStringTerms s = response.getAggregations().get("targetid_stats");

            for(Terms.Bucket b : s.getBuckets()){
                System.out.println(b.getKey() +  ": " + b.getDocCount());
            }

            //System.out.println(s.getBuckets().size());
            //System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(client != null){
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //Settings settings = Settings.builder().put("").build();
        /*TransportClient client = null;

        try {
            //request.
            client = new PreBuiltTransportClient(Settings.EMPTY)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName("127.0.0.1"), 9200));
            SearchRequestBuilder sr = client.prepareSearch().setQuery(QueryBuilders.matchQuery("accid", "a1"));
            MultiSearchResponse response = client.prepareMultiSearch().add(sr).get();
            for(MultiSearchResponse.Item item : response.getResponses()){
                System.out.println(item.getResponse().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(client != null){
                client.close();
            }
        }*/
    }

    private static void create(String index, String type){
        IndexRequest request = new IndexRequest(index, type);
        RestHighLevelClient client = getClient();
        Random random = new Random();
        String[] accids = new String[]{"a1","a2","a3","a4","a5","a6","a7","a8","a9","a10"};
        String[] targets = new String[]{"t1","t2","t3","t4","t5","t6","t7","t8","t9","t10"};
        for(int i = 0;i < 10000; i++){
            Map<String, Object> content = new HashMap<String, Object>();
            content.put("accid", accids[random.nextInt(10)]);
            content.put("targetid", targets[random.nextInt(10)]);
            content.put("type", random.nextInt(20));
            content.put("num", random.nextInt(3));
            request.source(content);
            try {
                System.out.println(client.index(request).getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        closeClient(client);

        //request.source("{\"user\":{\"id\":4,\"name\":\"heru\"}}", XContentType.JSON);

    }

    private static void get(String index, String type, String id){
        GetRequest request = new GetRequest(index, type, id);
        RestHighLevelClient client = getClient();
        GetResponse response = null;
        try {
            response = client.get(request);
            System.out.println(response.getSourceAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeClient(client);
    }

    private static void update(String index, String type, String id){
        RestHighLevelClient client = getClient();
        UpdateRequest request = new UpdateRequest(index, type, id);
        UpdateResponse response = null;
        String json = "{\"id\":\"1\",\"name\":\"sss\"}";
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", 1);
            params.put("name", "heru");
            Script script = new Script(ScriptType.STORED, null, null, params);
            request.script(script);
            //request.doc("up", "ss");
            request.upsert(json, XContentType.JSON);
            response = client.update(request);
            System.out.println(response.getGetResult().getSource());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //public static

    private static RestHighLevelClient getClient(){
        RestClientBuilder builder = RestClient.builder(
                new HttpHost("127.0.0.1", 9200, "http"));
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

    private static void closeClient(RestHighLevelClient client){
        try{
            if(client != null)
                client.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
