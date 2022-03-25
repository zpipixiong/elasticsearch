package com.polar.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//深度分页解决：一般大厂都是在页面设置做多显示100页，其实数据只有显示几千条而已，第二就是通过滚动查询Scroll Search（官方不推荐使用了，无法保存索引状态）适合非C端业务
//还有一种search_after,需要唯一的一种排序方式，将每次最后的sort的数据返回查询下一批数据

@SpringBootApplication
public class ElasticsearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElasticsearchApplication.class, args);
        System.out.println("111 master modify");
    }

}
