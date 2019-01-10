package com.espay.pojo.view;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class BotMessageVO implements Serializable {
    private String info;
    private String username;
    private  String userId;
    private String name;
    private String phone;
    private String email;
    private String answer;
    private String ip;
    private MultipartFile infoVoice;
}
