package com.wjh.demo.handle;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * 处理器类
 * 相当于SpringMVC中的service类，主要就是根据不同的请求url执行不同的业务逻辑
 * 编写的处理器类必须被容器所管理Mono
 */
@Component
public class CityHandler {

    public Mono<ServerResponse> helloCity(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("Hello city!"));
    }

    public Mono<ServerResponse> test(ServerRequest request){
        return ServerResponse.ok().contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("测试demo"));
    }
}
