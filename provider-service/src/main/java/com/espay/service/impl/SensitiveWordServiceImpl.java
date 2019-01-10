package com.espay.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.espay.mapper.two.SensitiveWordMapper;
import com.espay.pojo.entity.SensitiveWord;
import com.espay.service.SensitiveWordService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class SensitiveWordServiceImpl extends ServiceImpl<SensitiveWordMapper, SensitiveWord> implements SensitiveWordService {
    private static final Log logger = LogFactory.getLog(SensitiveWordServiceImpl.class);


}
