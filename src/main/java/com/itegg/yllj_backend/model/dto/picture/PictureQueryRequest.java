package com.itegg.yllj_backend.model.dto.picture;

import com.itegg.yllj_backend.common.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 图片查询请求
 */
@Data
public class PictureQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 文件体积
     */
    private Long picSize;

    /**
     * 图片宽度
     */
    private Integer picWidth;

    /**
     * 图片高度
     */
    private Integer picHeight;

    /**
     * 图片比例
     */
    private Double picScale;

    /**
     * 图片格式
     */
    private String picFormat;

    /**
     * 搜素词 (名称，简介等)
     */
    private String searchText;

    /**
     * 用户id
     */
    private Long userId;
}