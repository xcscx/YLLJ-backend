package com.itegg.yllj_backend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itegg.yllj_backend.exception.BusinessException;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.model.dto.user.UserLoginRequest;
import com.itegg.yllj_backend.model.dto.user.UserRegisterRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.enums.UserRoleEnum;
import com.itegg.yllj_backend.model.vo.LoginUserVO;
import com.itegg.yllj_backend.service.UserService;
import com.itegg.yllj_backend.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

import static com.itegg.yllj_backend.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户 service层
 */
@Service
@Slf4j
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

    @Override
    public LoginUserVO userLogin(UserLoginRequest req, HttpServletRequest request) {
        // 校验参数是否合理
        if(StrUtil.hasBlank(req.getUserAccount(), req.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 加密密码
        String encryptPassword = getEncryptPassword(req.getUserPassword());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", req.getUserAccount());
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if(ObjectUtil.isNull(user)) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        Object userOjb = request.getSession().getAttribute(USER_LOGIN_STATE);
        if(ObjectUtil.isNull(userOjb)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 用户密码加密
     * @param userPassword 用户明文密码
     * @return 密文密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
    // TODO: 盐值转换为各个用户独有各自的盐值
        final String SALT = "itegg";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    /**
     * 获取当前登录用户
     * @param request http请求
     * @return 用户信息
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断是否登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if(ObjectUtil.isNull(currentUser) || ObjectUtil.isNull(currentUser.getId())) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取用户脱敏信息
     * @param user 用户信息
     * @return 用户脱敏信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if(ObjectUtil.isNull(user)) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

}




