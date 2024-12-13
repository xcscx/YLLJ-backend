package com.itegg.yllj_backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.service.UserService;
import com.itegg.yllj_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

}




