package com.itegg.yllj_backend.controller;

import com.itegg.yllj_backend.aop.annotation.AuthCheck;
import com.itegg.yllj_backend.common.Result;
import com.itegg.yllj_backend.common.ResultUtils;
import com.itegg.yllj_backend.constant.UserConstant;
import com.itegg.yllj_backend.model.dto.picture.PictureUploadRequest;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.vo.PictureVO;
import com.itegg.yllj_backend.service.PictureService;
import com.itegg.yllj_backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Resource
    private UserService userService;

    @Resource
    private PictureService pictureService;

    /**
     * 上传图片（兼容重新上唇）
     * @param multipartFile 文件
     * @param pictureUploadRequest 图片请求
     * @param request http请求
     * @return
     */
    @PostMapping("/upload")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public Result<PictureVO> uploadPicture(@RequestPart("file")MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        PictureVO pictureVO = pictureService.uploadPicture(multipartFile, pictureUploadRequest, loginUser);
        return ResultUtils.ok(pictureVO);
    }

}
