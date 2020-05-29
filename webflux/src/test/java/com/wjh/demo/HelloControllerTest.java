package com.wjh.demo;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import org.springframework.web.reactive.socket.client.WebSocketClient;
import reactor.core.publisher.Flux;

import java.net.URI;
import java.time.Duration;
import java.util.Objects;

public class HelloControllerTest {

    public static void main(String[] args) {
        SSETest();
        testWSClient();
    }

    /**
     * 测试代码中，我们使用 WebClient 访问 SSE 在发送请求部分与访问 REST API 是相同的，不同的地方在于对 HTTP 响应的处理。由于 SSE 服务的响应是一个消息流，我们需要使用 flatMapMany 把 Mono转换成一个 Flux对象，通过方法BodyExtractors.toFlux来完成，其中的参数 new ParameterizedTypeReference
     * ————————————————
     * 版权声明：本文为CSDN博主「mickjoust」的原创文章，遵循 CC 4.0 BY-SA 版权协议，转载请附上原文出处链接及本声明。
     * 原文链接：https://blog.csdn.net/mickjoust/article/details/80241104
     */
    public static void SSETest() {
        final WebClient client = WebClient.create();
        client.get()
                .uri("http://localhost:8007/hello/randomNumbers")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .flatMapMany(response -> response.body(BodyExtractors.toFlux(new ParameterizedTypeReference<ServerSentEvent<String>>() {
                })))
                .filter(sse -> Objects.nonNull(sse.data()))
                .map(ServerSentEvent::data)
                .buffer(11)
                .doOnNext(System.out::println)
                .blockFirst();
    }

    /**
     * 注意，访问 WebSocket 不能使用 WebClient，而应该使用专门的 WebSocketClient 客户端。
     * Spring Boot 的 WebFlux 模板中默认使用的是 Reactor Netty 库。
     * Reactor Netty 库提供了 WebSocketClient 的实现。
     * WebSocketClient 的 execute 方法与 WebSocket 服务器建立连接，并执行给定的 WebSocketHandler 对象。
     * 在 WebSocketHandler 的实现中，首先通过 WebSocketSession 的 send 方法来发送字符串 Hello 到服务器端，
     * 然后通过 receive 方法来等待服务器端的响应并输出。方法 take(1)的作用是表明客户端只获取服务器端发送的第一条消息。
     */
    public static void testWSClient() {
        final WebSocketClient client = new ReactorNettyWebSocketClient();
        client.execute(URI.create("ws://localhost:8080/echo"), session ->
                session.send(Flux.just(session.textMessage("Hello")))
                        .thenMany(session.receive().take(1).map(WebSocketMessage::getPayloadAsText))
                        .doOnNext(System.out::println)
                        .then())
                .block(Duration.ofMillis(5000));
    }


}
