package com.itegg.yllj_backend.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itegg.yllj_backend.exception.ErrorCode;
import com.itegg.yllj_backend.exception.ThrowUtils;
import com.itegg.yllj_backend.manager.FileManager;
import com.itegg.yllj_backend.model.dto.file.UploadPictureResult;
import com.itegg.yllj_backend.model.dto.picture.PictureQueryRequest;
import com.itegg.yllj_backend.model.dto.picture.PictureUploadRequest;
import com.itegg.yllj_backend.model.entity.Picture;
import com.itegg.yllj_backend.model.entity.User;
import com.itegg.yllj_backend.model.vo.PictureVO;
import com.itegg.yllj_backend.model.vo.UserVO;
import com.itegg.yllj_backend.service.PictureService;
import com.itegg.yllj_backend.mapper.PictureMapper;
import com.itegg.yllj_backend.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture>
    implements PictureService{

    @Resource
    private FileManager fileManager;

    @Resource
    private UserService userService;

    @Override
    public void validPicture(Picture picture) {
        ThrowUtils.throwIf(ObjectUtil.isNull(picture), ErrorCode.PARAMS_ERROR);
        // 从对象中取值
        Long id = picture.getId();
        String url = picture.getUrl();
        String introduction = picture.getIntroduction();
        // 修改数据时 id不为空 有参数则校验
        ThrowUtils.throwIf(ObjectUtil.isNull(id), ErrorCode.PARAMS_ERROR, "id能为空");
        if(StrUtil.isNotBlank(url)) {
            ThrowUtils.throwIf(url.length() > 1000, ErrorCode.PARAMS_ERROR, "url过长");
        }
        if(StrUtil.isNotBlank(introduction)) {
            ThrowUtils.throwIf(introduction.length() > 800, ErrorCode.PARAMS_ERROR, "简介过长");
        }
    }

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

    @Override
    public PictureVO getPictureVO(Picture picture, HttpServletRequest request) {
        // 对象转封装类
        PictureVO pictureVO = PictureVO.objToVo(picture);
        // 关联查询用户信息
        Long userId = picture.getUserId();
        if(ObjectUtil.isNotNull(userId) && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            pictureVO.setUser(userVO);
        }
        return pictureVO;
    }

    @Override
    public Page<PictureVO> getPictureVOPage(Page<Picture> picturePage, HttpServletRequest request) {
        List<Picture> pictureList = picturePage.getRecords();
        Page<PictureVO> pictureVOPage = new Page<>(picturePage.getCurrent(), picturePage.getSize(), picturePage.getTotal());
        if(CollUtil.isNotEmpty(pictureList)) {
            return pictureVOPage;
        }
        // 对象列表转换 -> 封装对象列表
        List<PictureVO> pictureVOList = pictureList.stream().map(PictureVO::objToVo).collect(Collectors.toList());;
        // 关联插叙用户信息
        Set<Long> userIdset = pictureList.stream().map(Picture::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdset).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        pictureVOList.forEach(pictureVO -> {
            Long userId = pictureVO.getUserId();
            User user = null;
            if(userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            pictureVO.setUser(userService.getUserVO(user));
        });
        pictureVOPage.setRecords(pictureVOList);
        return pictureVOPage;
    }

    @Override
    public QueryWrapper<Picture> getQueryWrapper(PictureQueryRequest req) {
        QueryWrapper<Picture> queryWrapper = new QueryWrapper<>();
        if(ObjectUtil.isNull(req)) {
            return queryWrapper;
        }
        // 请求参数中取值
        String searchText = req.getSearchText();
        List<String> tags = req.getTags();
        String sortField = req.getSortField();
        String sortOrder = req.getSortOrder();
        // 多字段搜素
        if(StrUtil.isNotBlank(searchText)) {
            queryWrapper.and(qw -> qw.like("name", searchText)
                    .or()
                    .like("introduction", searchText)
            );
        }
        // 搜素条件拼接
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getId()), "id", req.getId());
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getUserId()), "userId", req.getUserId());
        queryWrapper.like(StrUtil.isNotBlank(req.getName()), "name", req.getName());
        queryWrapper.like(StrUtil.isNotBlank(req.getIntroduction()), "introduction", req.getIntroduction());
        queryWrapper.like(StrUtil.isNotBlank(req.getPicFormat()), "picFormat", req.getPicFormat());
        queryWrapper.eq(StrUtil.isNotBlank(req.getCategory()), "category", req.getCategory());
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getPicWidth()), "picWidth", req.getPicWidth());
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getPicHeight()), "picHeight", req.getPicHeight());
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getPicSize()), "picSize", req.getPicSize());
        queryWrapper.eq(ObjectUtil.isNotEmpty(req.getPicScale()), "picScale", req.getPicScale());
        // JSON数组查询
        if(CollUtil.isNotEmpty(tags)) {
            for(String tag : tags) {
                queryWrapper.like("tags", "\"" + tag + "\"");
            }
        }
        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

}




