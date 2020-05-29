package com.wjh.demo.handle;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class StudentHandler {
    public Mono<ServerResponse> helloStudent(ServerRequest request) {
        return ServerResponse//响应对象封装
                .ok()//响应码
                .contentType(MediaType.TEXT_PLAIN)//响应类型
                .body(BodyInserters.fromObject("这是学生控制类"));//响应体
    }
}
