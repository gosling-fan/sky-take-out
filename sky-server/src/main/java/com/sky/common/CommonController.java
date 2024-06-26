package com.sky.common;

import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;
    @PostMapping("/upload")
    public Result<String> fileUpload(MultipartFile file){
        String originalFilename = file.getOriginalFilename();
        try {
            //防止文件名重复，重新得到一个唯一的新文件名
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            String newFilename =UUID.randomUUID().toString()+substring;
            //上传到阿里云oss,并返回文件URL给前端
            String filename = aliOssUtil.upload(file.getBytes(), newFilename);
            return Result.success(filename);
        } catch (IOException e) {
            log.error("上传失败：{}",e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
