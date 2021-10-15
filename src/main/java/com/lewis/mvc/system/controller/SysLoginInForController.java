package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.poi.ExcelUtil;
import com.lewis.mvc.system.entity.SysLogininfor;
import com.lewis.mvc.system.service.ISysLogininforService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统访问记录
 *
 * @author Lewis
 */
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLoginInForController extends BaseController {

    @Autowired
    private ISysLogininforService logininforService;

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysLogininfor logininfor) {
        startPage();
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        return getDataTable(list);
    }

    @Log(title = "登录日志" , businessType = BusinessType.EXPORT)
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:export')")
    @GetMapping("/export")
    public BaseResult export(SysLogininfor logininfor) {
        List<SysLogininfor> list = logininforService.selectLogininforList(logininfor);
        ExcelUtil<SysLogininfor> util = new ExcelUtil<SysLogininfor>(SysLogininfor.class);
        return util.exportExcel(list, "登录日志");
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    public BaseResult remove(@PathVariable Long[] infoIds) {
        return toAjax(logininforService.deleteLogininforByIds(infoIds));
    }

    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @Log(title = "登录日志" , businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    public BaseResult clean() {
        logininforService.cleanLogininfor();
        return BaseResult.ok();
    }
}
