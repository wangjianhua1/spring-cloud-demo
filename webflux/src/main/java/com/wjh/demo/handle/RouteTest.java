package com.wjh.demo.handle;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.function.BiPredicate;

/**
 * 路由器类
 * 相当于DispatherServlet，主要就是将不同的请求url和对应的处理器类进行匹配
 * <p>
 * 技巧01：路由器类相当于一个Java配置类，所以必须添加 @Configuration 注解，并且路由器类中的路由方法返回的对象必须被容器管理，所以必须添加@Bean注解
 * <p>
 * 技巧02：RouterFunctions的route方法接收两个参数，并返回 RouterFunctions 类型
 * <p>
 * 参数一 ：请求断言
 * <p>
 * 参数二 ：处理函数接口【可用lambda表达式代替】
 * Mono：实现发布者，并返回 0 或 1 个元素，即单对象
 * Flux：实现发布者，并返回 N 个元素，即 List 列表对象。
 */
@Configuration
public class RouteTest {

    @Bean
    public RouterFunction<ServerResponse> cityRouter(CityHandler cityHandler) {
        return RouterFunctions.route(RequestPredicates.GET("/city")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), cityHandler::helloCity);
    }

    @Bean
    public RouterFunction<ServerResponse> studentRouter(StudentHandler studentHandler) {
        Mono<String> stringMono = Mono.fromDirect(Mono.just(""));
        return RouterFunctions.route(RequestPredicates.GET("/student")
                .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), studentHandler::helloStudent);
    }

}
