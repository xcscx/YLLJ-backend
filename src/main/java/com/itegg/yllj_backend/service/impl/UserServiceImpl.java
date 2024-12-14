package com.itegg.yllj_backend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itegg.yllj_backend.exception.BusinessException;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.enums.UserRoleEnum;
import com.itegg.yllj_backend.service.UserService;
import com.itegg.yllj_backend.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public long userRegister(UserRegisterRequest req) {
        // 校验数据是否合理
        if(StrUtil.hasBlank(req.getUserAccount(), req.getUserPassword(), req.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if(ObjectUtil.notEqual(req.getUserPassword(), req.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码输入不一致");
        }
        // 校验数据是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", req.getUserAccount());
        long count = this.baseMapper.selectCount(queryWrapper);
        if(count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        // 数据加密 + 默认用户名称设置（年后两位 + 月 + 六位随机数）
        String encryptPassword = getEncryptPassword(req.getUserPassword());
        LocalDate currentDate = LocalDate.now();
        String year = String.valueOf(currentDate.getYear()).substring(2);
        String month = String.format("%02d", currentDate.getMonthValue());
        String randomPart = RandomUtil.randomNumbers(6);
        // 插入数据
        User user = new User();
        user.setUserAccount(req.getUserAccount());
        user.setUserPassword(encryptPassword);
        user.setUserName("默认用户" + year + month + randomPart);
        user.setUserRole(UserRoleEnum.USER.getCode());
        boolean saveResult = this.save(user);
        if(!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "系统异常，数据注册失败");
        }
        return user.getId();
    }


    /**
     * 用户密码加密
     * @param userPassword 用户明文密码
     * @return 密文密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
    //TODO: 盐值转换为各个用户独有各自的盐值
        final String SALT = "itegg";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

}




