package com.itegg.yllj_backend.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.exception.ThrowUtils;
import com.itegg.yllj_backend.manager.FileManager;
import com.itegg.yllj_backend.model.dto.file.UploadPictureResult;
import com.itegg.yllj_backend.model.dto.picture.PictureUploadRequest;
import com.itegg.yllj_backend.model.entity.Picture;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.vo.PictureVO;
import com.itegg.yllj_backend.service.PictureService;
import com.itegg.yllj_backend.mapper.PictureMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;

/**
 *
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

    @Resource
    private FileManager fileManager;

    @Override
    public PictureVO uploadPicture(MultipartFile multipartFile, PictureUploadRequest pictureUploadRequest, User loginUser) {
        // 校验参数
        ThrowUtils.throwIf(ObjectUtil.isNull(loginUser), ErrorCode.NO_AUTH_ERROR);
        // 判断新增/删除
        Long pictureId = null;
        if(ObjectUtil.isNotNull(pictureUploadRequest)) {
            pictureId = pictureUploadRequest.getId();
        }
        // 如果是更新（传输了请求id）判断id是否存在
        if(ObjectUtil.isNotNull(pictureId)) {
            boolean exists = this.lambdaQuery()
                    .eq(Picture::getId, pictureId)
                    .exists();
            ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "图片不存在");
        }
        // 上传图片，获取信息 - 依据用户划分目录 - 公共图库部分为public
        String uploadPathPrefix = String.format("public/%s", loginUser.getId());
        UploadPictureResult uploadPictureResult = fileManager.uploadPictureResult(multipartFile, uploadPathPrefix);
        // 构造入库图片信息
        Picture picture = new Picture();
        picture.setUrl(uploadPictureResult.getUrl());
        picture.setName(uploadPictureResult.getPicName());
        picture.setPicSize(uploadPictureResult.getPicSize());
        picture.setPicWidth(uploadPictureResult.getPicWidth());
        picture.setPicHeight(uploadPictureResult.getPicHeight());
        picture.setPicScale(uploadPictureResult.getPicScale());
        picture.setPicFormat(uploadPictureResult.getPicFormat());
        picture.setUserId(loginUser.getId());
        // 操作数据库
        if(ObjectUtil.isNotNull(pictureId)) {
            // id为空,进行新增操作
            picture.setId(pictureId);
            picture.setEditTime(new Date());
        }
        boolean result = this.saveOrUpdate(picture);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "图片上传失败，数据库未保存");
        return PictureVO.objToVo(picture);
    }

}




