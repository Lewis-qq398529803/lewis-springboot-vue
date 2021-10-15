package com.lewis.mvc.common;

import com.lewis.config.LewisConfig;
import com.lewis.core.constant.Constants;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.utils.MapUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.file.FileUploadUtils;
import com.lewis.core.utils.file.FileUtils;
import com.lewis.config.framework.ServerConfig;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 通用请求处理
 *
 * @author Lewis
 */
@Api(tags = "通用请求处理")
@RestController
@Slf4j
public class CommonController {

    @Resource
    private ServerConfig serverConfig;

    /**
     * 通用下载请求
     *
     * @param fileName 文件名称
     * @param delete   是否删除
     */
    @GetMapping("common/download")
    @ApiOperation("通用下载请求")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response) {
        try {
            if (!FileUtils.checkAllowDownload(fileName)) {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 " , fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = LewisConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete) {
                FileUtils.deleteFile(filePath);
            }
        } catch (Exception e) {
            log.error("下载文件失败" , e);
        }
    }

    /**
     * 通用上传请求
     *
     * @param file
     * @return BaseResult
     */
    @PostMapping("/common/upload")
    @ApiOperation("通用上传请求")
    public Object uploadFile(MultipartFile file) {
        try {
            // 上传文件路径
            String filePath = LewisConfig.getUploadPath();
            // 上传并返回新文件名称
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;
            BaseResult ok = BaseResult.ok();
            Map ajax = MapUtils.objectToMapByReflect(ok);
            ajax.put("fileName" , fileName);
            ajax.put("url" , url);
            return ajax;
        } catch (Exception e) {
            return BaseResult.fail(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     *
     * @param resource
     * @param response
     */
    @GetMapping("/common/download/resource")
    @ApiOperation("本地资源通用下载")
    public void resourceDownload(String resource, HttpServletResponse response) {
        try {
            if (!FileUtils.checkAllowDownload(resource)) {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 " , resource));
            }
            // 本地资源路径
            String localPath = LewisConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        } catch (Exception e) {
            log.error("下载文件失败" , e);
        }
    }
}
