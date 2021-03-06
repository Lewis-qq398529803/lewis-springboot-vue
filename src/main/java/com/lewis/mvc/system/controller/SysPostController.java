package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.ExcelUtil;
import com.lewis.mvc.system.entity.SysPost;
import com.lewis.mvc.system.service.ISysPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 岗位信息操作处理
 *
 * @author Lewis
 */
@Api(tags = "岗位信息操作处理")
@RestController
@RequestMapping("/system/post")
public class SysPostController extends BaseController {

    @Autowired
    private ISysPostService postService;

    @ApiOperation(value = "获取岗位列表", notes = "获取岗位列表")
    @PreAuthorize("@ss.hasPermi('system:post:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPost post) {
        startPage();
        List<SysPost> list = postService.selectPostList(post);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出岗位列表", notes = "导出岗位列表")
    @Log(title = "岗位管理" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:post:export')")
    @GetMapping("/export")
    public BaseResult export(SysPost post) {
        List<SysPost> list = postService.selectPostList(post);
        ExcelUtil<SysPost> util = new ExcelUtil<SysPost>(SysPost.class);
        return util.exportExcel(list, "岗位数据");
    }

    @ApiOperation(value = "根据岗位编号获取详细信息", notes = "根据岗位编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:post:query')")
    @GetMapping(value = "/{postId}")
    public BaseResult getInfo(@PathVariable Long postId) {
        return BaseResult.ok(postService.selectPostById(postId));
    }

    @ApiOperation(value = "新增岗位", notes = "新增岗位")
    @PreAuthorize("@ss.hasPermi('system:post:add')")
    @Log(title = "岗位管理" , businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult add(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return BaseResult.fail("新增岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return BaseResult.fail("新增岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setCreateBy(SecurityUtils.getUsername());
        return toAjax(postService.insertPost(post));
    }

    @ApiOperation(value = "修改岗位", notes = "修改岗位")
    @PreAuthorize("@ss.hasPermi('system:post:edit')")
    @Log(title = "岗位管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult edit(@Validated @RequestBody SysPost post) {
        if (UserConstants.NOT_UNIQUE.equals(postService.checkPostNameUnique(post))) {
            return BaseResult.fail("修改岗位'" + post.getPostName() + "'失败，岗位名称已存在");
        } else if (UserConstants.NOT_UNIQUE.equals(postService.checkPostCodeUnique(post))) {
            return BaseResult.fail("修改岗位'" + post.getPostName() + "'失败，岗位编码已存在");
        }
        post.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(postService.updatePost(post));
    }

    @ApiOperation(value = "删除岗位", notes = "删除岗位")
    @PreAuthorize("@ss.hasPermi('system:post:remove')")
    @Log(title = "岗位管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{postIds}")
    public BaseResult remove(@PathVariable Long[] postIds) {
        return toAjax(postService.deletePostByIds(postIds));
    }

    @ApiOperation(value = "获取岗位选择框列表", notes = "获取岗位选择框列表")
    @GetMapping("/optionselect")
    public BaseResult optionselect() {
        List<SysPost> posts = postService.selectPostAll();
        return BaseResult.ok(posts);
    }
}
