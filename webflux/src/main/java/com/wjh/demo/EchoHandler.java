package com.wjh.demo;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class EchoHandler implements WebSocketHandler {
    @Override
    public List<String> getSubProtocols() {
        return null;
    }

    @Override
    public Mono<Void> handle(WebSocketSession webSocketSession) {
        return webSocketSession.close();
    }
}
