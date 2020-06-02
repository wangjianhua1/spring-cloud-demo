package com.wjh.demo.xieyi;

import io.netty.channel.ChannelHandlerContext;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public final class MessageSender implements Runnable {

    private static final AtomicLong counter = new AtomicLong(1);
    private volatile ChannelHandlerContext ctx;

    public MessageSender(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // 模拟随机发送消息的过程
                TimeUnit.SECONDS.sleep(new Random().nextInt(3));
                Message message = new Message();
                message.setMessageType(MessageTypeEnum.REQUEST);
                message.setBody("this is my " + counter.getAndIncrement() + " message.");
                message.addAttachment("name", "xufeng");
                ctx.writeAndFlush(message);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

