package com.wjh.demo.xieyi;

public interface Resolver {
    public boolean support(Message message);

    public Message resolve(Message message);
}
