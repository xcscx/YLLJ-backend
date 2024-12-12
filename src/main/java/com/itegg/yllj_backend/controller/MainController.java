package com.itegg.yllj_backend.controller;

import com.itegg.yllj_backend.common.Result;
import com.itegg.yllj_backend.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Result<String> health() {
        return ResultUtils.ok("ok");
    }

}