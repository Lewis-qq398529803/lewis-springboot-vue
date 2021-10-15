package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.poi.ExcelUtil;
import com.lewis.mvc.system.entity.SysOperLog;
import com.lewis.mvc.system.service.ISysOperLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 操作日志记录
 *
 * @author Lewis
 */
@RestController
@RequestMapping("/monitor/operlog")
public class SysOperlogController extends BaseController {
    @Autowired
    private ISysOperLogService operLogService;

    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysOperLog operLog) {
        startPage();
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        return getDataTable(list);
    }

    @Log(title = "操作日志" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:export')")
    @GetMapping("/export")
    public BaseResult export(SysOperLog operLog) {
        List<SysOperLog> list = operLogService.selectOperLogList(operLog);
        ExcelUtil<SysOperLog> util = new ExcelUtil<SysOperLog>(SysOperLog.class);
        return util.exportExcel(list, "操作日志");
    }

    @Log(title = "操作日志" , businessType = BusinessType.DELETE)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/{operIds}")
    public BaseResult remove(@PathVariable Long[] operIds) {
        return toAjax(operLogService.deleteOperLogByIds(operIds));
    }

    @Log(title = "操作日志" , businessType = BusinessType.CLEAN)
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remove')")
    @DeleteMapping("/clean")
    public BaseResult clean() {
        operLogService.cleanOperLog();
        return BaseResult.ok();
    }
}
