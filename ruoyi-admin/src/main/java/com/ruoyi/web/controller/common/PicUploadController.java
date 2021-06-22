package com.ruoyi.web.controller.common;

import com.ruoyi.common.utils.file.FileUploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 图片上传接口
 *
 * @author chenyitao - 2021年6月22日, 022 - 15:29:42
 */
@Api(tags = "图片上传")
@RestController
@RequestMapping("/pic")
public class PicUploadController {

    @PostMapping("/upload")
    @ApiOperation("上传")
    public String upload(MultipartFile multipartFile) throws IOException {
        return FileUploadUtils.upload(multipartFile);
    }

}
