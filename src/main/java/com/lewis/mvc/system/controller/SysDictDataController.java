package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.AjaxResult;
import com.lewis.core.base.domain.entity.SysDictData;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.poi.ExcelUtil;
import com.lewis.mvc.system.service.ISysDictDataService;
import com.lewis.mvc.system.service.ISysDictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据字典信息
 *
 * @author Lewis
 */
@Api(tags = "数据字典信息")
@RestController
@RequestMapping("/system/dict/data")
public class SysDictDataController extends BaseController {

    @Autowired
    private ISysDictDataService dictDataService;

    @Autowired
    private ISysDictTypeService dictTypeService;

    @ApiOperation(value = "获取字典列表", notes = "获取字典列表")
    @PreAuthorize("@ss.hasPermi('system:dict:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysDictData dictData) {
        startPage();
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        return getDataTable(list);
    }

    @ApiOperation(value = "导出字典列表", notes = "导出字典列表")
    @Log(title = "字典数据" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('system:dict:export')")
    @GetMapping("/export")
    public AjaxResult export(SysDictData dictData) {
        List<SysDictData> list = dictDataService.selectDictDataList(dictData);
        ExcelUtil<SysDictData> util = new ExcelUtil<SysDictData>(SysDictData.class);
        return util.exportExcel(list, "字典数据");
    }

    @ApiOperation(value = "查询字典数据详细", notes = "查询字典数据详细")
    @PreAuthorize("@ss.hasPermi('system:dict:query')")
    @GetMapping(value = "/{dictCode}")
    public AjaxResult getInfo(@PathVariable Long dictCode) {
        return AjaxResult.success(dictDataService.selectDictDataById(dictCode));
    }

    @ApiOperation(value = "根据字典类型查询字典数据信息", notes = "根据字典类型查询字典数据信息")
    @GetMapping(value = "/type/{dictType}")
    public AjaxResult dictType(@PathVariable String dictType) {
        List<SysDictData> data = dictTypeService.selectDictDataByType(dictType);
        if (StringUtils.isNull(data)) {
            data = new ArrayList<SysDictData>();
        }
        return AjaxResult.success(data);
    }

    @ApiOperation(value = "新增字典类型", notes = "新增字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:add')")
    @Log(title = "字典数据" , businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@Validated @RequestBody SysDictData dict) {
        dict.setCreateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.insertDictData(dict));
    }

    @ApiOperation(value = "修改保存字典类型", notes = "修改保存字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:edit')")
    @Log(title = "字典数据" , businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysDictData dict) {
        dict.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(dictDataService.updateDictData(dict));
    }

    @ApiOperation(value = "删除字典类型", notes = "删除字典类型")
    @PreAuthorize("@ss.hasPermi('system:dict:remove')")
    @Log(title = "字典类型" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    public AjaxResult remove(@PathVariable Long[] dictCodes) {
        dictDataService.deleteDictDataByIds(dictCodes);
        return success();
    }
}
