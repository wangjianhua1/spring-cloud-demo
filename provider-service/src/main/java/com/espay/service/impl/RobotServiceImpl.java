package com.espay.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.espay.mapper.one.FrspUserOneMapper;
import com.espay.pojo.entity.FrspUser;
import com.espay.service.RobotService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RobotServiceImpl extends ServiceImpl<FrspUserOneMapper, FrspUser> implements RobotService {
    private static final Log logger = LogFactory.getLog(RobotServiceImpl.class);


}
