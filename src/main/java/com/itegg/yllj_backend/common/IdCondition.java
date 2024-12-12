package com.itegg.yllj_backend.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用id请求参数
 */
@Data
public class IdCondition implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;
}
