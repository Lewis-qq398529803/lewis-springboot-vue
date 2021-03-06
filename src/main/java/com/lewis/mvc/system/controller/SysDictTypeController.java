package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.domain.entity.SysDictType;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.ExcelUtil;
import com.lewis.mvc.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据字典信息
 *
 * @author Lewis
 */
@Api(tags = "数据字典信息")
@RestController
@RequestMapping("/system/dict/type")
public class SysDictTypeController extends BaseController {

    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "获取字典类型列表", notes = "获取字典类型列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictType dictType) {
        startPage();
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出字典类型列表", notes = "导出字典类型列表")
    @Log(title = "字典类型" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public BaseResult export(SysDictType dictType) {
        List<SysDictType> list = dictTypeService.selectDictTypeList(dictType);
        ExcelUtil<SysDictType> util = new ExcelUtil<SysDictType>(SysDictType.class);
        return util.exportExcel(list, "字典类型");
    }

    @ApiOperation(value = "查询字典类型详细", notes = "查询字典类型详细")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictId}")
    public BaseResult getInfo(@PathVariable Long dictId) {
        return BaseResult.ok(dictTypeService.selectDictTypeById(dictId));
    }

    @ApiOperation(value = "新增字典类型", notes = "新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典类型" , businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult add(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return BaseResult.fail("新增字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.insertDictType(dict));
    }

    @ApiOperation(value = "修改字典类型", notes = "修改字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典类型" , businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult edit(@Validated @RequestBody SysDictType dict) {
        if (UserConstants.NOT_UNIQUE.equals(dictTypeService.checkDictTypeUnique(dict))) {
            return BaseResult.fail("修改字典'" + dict.getDictName() + "'失败，字典类型已存在");
        }
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictTypeService.updateDictType(dict));
    }

    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    public BaseResult remove(@PathVariable Long[] dictIds) {
        dictTypeService.deleteDictTypeByIds(dictIds);
        return success();
    }

    @ApiOperation(value = "刷新字典缓存", notes = "刷新字典缓存")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型" , businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    public BaseResult refreshCache() {
        dictTypeService.resetDictCache();
        return BaseResult.ok();
    }

    @ApiOperation(value = "获取字典选择框列表", notes = "获取字典选择框列表")
    @GetMapping("/optionselect")
    public BaseResult optionselect() {
        List<SysDictType> dictTypes = dictTypeService.selectDictTypeAll();
        return BaseResult.ok(dictTypes);
    }
}
