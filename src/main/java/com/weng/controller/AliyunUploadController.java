package com.weng.controller;

import com.weng.common.util.AliOSSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.weng.common.Result;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/common")
public class AliyunUploadController
{
    @Autowired
    private AliOSSUtils aliOSSUtils;

    /**
     * I fucking made it!
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException
    {
        String url = aliOSSUtils.upload(file);
        log.info("返回的结果的url为:{}", url);
        return Result.success(url);
    }
}
