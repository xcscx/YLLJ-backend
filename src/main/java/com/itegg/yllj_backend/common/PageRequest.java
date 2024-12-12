package com.itegg.yllj_backend.common;

import lombok.Data;

/**
 * 通用分页请求参数
 */
@Data
public class PageRequest {

    /**
     * 当前页号
     */
    private int current = 1;;

    /**
     * 页面行数
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序方式（默认升序）
     */
    private String sortOrder = "desc";

}
