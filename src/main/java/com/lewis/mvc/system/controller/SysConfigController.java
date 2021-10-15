package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.annotation.RepeatSubmit;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.poi.ExcelUtil;
import com.lewis.mvc.system.entity.SysConfig;
import com.lewis.mvc.system.service.ISysConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 参数配置 信息操作处理
 *
 * @author Lewis
 */
@Api(tags = "参数配置")
@RestController
@RequestMapping("/system/config")
public class SysConfigController extends BaseController {

    @Autowired
    private ISysConfigService configService;

    @ApiOperation(value = "获取参数配置列表", notes = "获取参数配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysConfig config) {
        startPage();
        List<SysConfig> list = configService.selectConfigList(config);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出参数配置列表", notes = "导出参数配置列表")
    @Log(title = "参数管理" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:config:export')")
    @GetMapping("/export")
    public BaseResult export(SysConfig config) {
        List<SysConfig> list = configService.selectConfigList(config);
        ExcelUtil<SysConfig> util = new ExcelUtil<SysConfig>(SysConfig.class);
        return util.exportExcel(list, "参数数据");
    }

    @ApiOperation(value = "根据参数编号获取详细信息", notes = "根据参数编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @GetMapping(value = "/{configId}")
    public BaseResult getInfo(@PathVariable Long configId) {
        return BaseResult.ok(configService.selectConfigById(configId));
    }

    @ApiOperation(value = "根据参数键名查询参数值", notes = "根据参数键名查询参数值")
    @GetMapping(value = "/configKey/{configKey}")
    public BaseResult getConfigKey(@PathVariable String configKey) {
        return BaseResult.ok(configService.selectConfigByKey(configKey));
    }

    @ApiOperation(value = "新增参数配置", notes = "新增参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @Log(title = "参数管理" , businessType = BusinessType.INSERT)
    @PostMapping
    @RepeatSubmit
    public BaseResult add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return BaseResult.fail("新增参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setCreateBy(SecurityUtils.getUsername());
        return toAjax(configService.insertConfig(config));
    }

    @ApiOperation(value = "修改参数配置", notes = "修改参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @Log(title = "参数管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigKeyUnique(config))) {
            return BaseResult.fail("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(configService.updateConfig(config));
    }

    @ApiOperation(value = "删除参数配置", notes = "删除参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public BaseResult remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return success();
    }

    @ApiOperation(value = "刷新参数缓存", notes = "刷新参数缓存")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @Log(title = "参数管理" , businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public BaseResult refreshCache() {
        configService.resetConfigCache();
        return BaseResult.ok();
    }
}
