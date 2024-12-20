package com.itegg.yllj_backend.service;

import com.itegg.yllj_backend.model.dto.picture.PictureUploadRequest;
import com.itegg.yllj_backend.model.entity.Picture;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.vo.PictureVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 图片模块 service层
 * @author xtx
 */
public interface PictureService extends IService<Picture> {

    /**
     * 上传图片
     * @param multipartFile 上传图片文件
     * @param pictureUploadRequest 图片上传请求
     * @param loginUser 登录用户
     * @return
     */
    PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser);

}
