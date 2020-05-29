package com.wjh.demo;

import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hello")
public class HelloController {
    int i;

    @GetMapping("/test")
    public Mono<String> test() {
        return Mono.just("Hello webflux " + i);
    }

    @GetMapping("/testMvc")
    public String testMVC() {
        i++;
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        return "hello test mvc";
    }

    @GetMapping("/randomNumbers")
    public Flux<ServerSentEvent<Integer>> randomNumbers() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> Tuples.of(seq, ThreadLocalRandom.current().nextInt()))
                .map(data -> ServerSentEvent.<Integer>builder()
                        .event("random")
                        .id(Long.toString(data.getT1()))
                        .data(data.getT2())
                        .build());
    }

    public static void main(String[] args) {
        int[] nums = {1, 1, 2, 2, 6, 2, 3, 2, 3, 4, 2, 3, 2, 3, 3, 1};
        System.out.println(comp(nums));
    }

    public static Integer comp(int[] nums) {
        // 设置初始票数为 0
        int count = 0;
        // 先将要求的众数定义为空
        Integer majorityElement = null;
        // 循环数组
        for (int num : nums) {
            // 当 count 为 0 时,假设当前的数为要求的众数
            if (count == 0) {
                majorityElement = num;
            }
            // 当 num 等于假设的众数时, count 就加 1
            count += (num == majorityElement) ? 1 : -1;
        }
        // 最后返回真正的众数
        return majorityElement;
    }

}
