package com.wjh.demo.kafka;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Api作用了类上，作为协议集描述（控制器的描述）
 * @ApiOperation，描述该接口方法
 * @ApiImplicitParams，参数集
 * @ApiImplicitParam，参数的具体描述
 * @ApiResponses，接口的返回对象集
 * @ApiResponse，接口的返回值模型，code：请求返回值，message：请求返回消息，response：返回值中的类型 运行项目，访问地址为：ip:post/项目路径前缀/swagger-ui.html，
 * 如：http://localhost:8080/laboratory/swagger-ui.html
 */
@Slf4j
@RestController
@Api(tags = "测试接口")
public class TestController {

    @Autowired
    private kafkaService kafkaService;

    @ApiOperation("测试Swagger功能")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "topic", value = "kafka topic", defaultValue = "tp-1", required = false, paramType = "query"),
            @ApiImplicitParam(name = "msg", value = "kafka消息内容", defaultValue = "msg-1", required = false, paramType = "query"),
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "请求成功", response = Message.class)
    })
    @GetMapping("/hello")
    public Message hello(@RequestParam(value = "topic", required = false, defaultValue = "111") String topic,
                         @RequestParam(value = "msg", required = false) String msg) {
//        kafkaService.send();
        return new Message(topic, msg);
    }

}
