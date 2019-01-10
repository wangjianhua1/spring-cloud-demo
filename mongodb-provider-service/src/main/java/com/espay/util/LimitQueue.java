package com.espay.util;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 定长队列
 *
 * @param <E>
 */
public class LimitQueue<E> implements Serializable {
    /**
     * 队列长度
     */
    private int limit;

    private Queue<E> queue = new LinkedList<E>();

    public LimitQueue(int limit) {
        this.limit = limit;
    }

    /**
     * 入队
     *
     * @param e
     * @return
     */
    public boolean offer(E e) {
        if (queue.size() >= limit) {
            queue.poll();
        }
        return queue.offer(e);
    }

    /**
     * 出队并移除元素
     *
     * @return
     */
    public E poll() {
        return queue.poll();
    }

    public Queue<E> getQueue() {
        return queue;
    }

    public int getLimit() {
        return this.limit;
    }

    public int size() {
        return queue.size();
    }

}
