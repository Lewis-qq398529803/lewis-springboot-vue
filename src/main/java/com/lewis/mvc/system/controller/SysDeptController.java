package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.domain.entity.SysDept;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.MapUtils;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.mvc.system.service.ISysDeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 部门信息
 *
 * @author Lewis
 */
@Api(tags = "部门信息")
@RestController
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private ISysDeptService deptService;

    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list")
    public BaseResult list(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return BaseResult.ok(depts);
    }

    @ApiOperation(value = "查询部门列表（排除节点）", notes = "查询部门列表（排除节点）")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @GetMapping("/list/exclude/{deptId}")
    public BaseResult excludeChild(@PathVariable(value = "deptId" , required = false) Long deptId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        Iterator<SysDept> it = depts.iterator();
        while (it.hasNext()) {
            SysDept d = (SysDept) it.next();
            if (d.getDeptId().intValue() == deptId
                    || ArrayUtils.contains(StringUtils.split(d.getAncestors(), ","), deptId + "")) {
                it.remove();
            }
        }
        return BaseResult.ok(depts);
    }

    @ApiOperation(value = "根据部门编号获取详细信息", notes = "根据部门编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:dept:query')")
    @GetMapping(value = "/{deptId}")
    public BaseResult getInfo(@PathVariable Long deptId) {
        return BaseResult.ok(deptService.selectDeptById(deptId));
    }

    @ApiOperation(value = "获取部门下拉树列表", notes = "获取部门下拉树列表")
    @GetMapping("/treeselect")
    public BaseResult treeselect(SysDept dept) {
        List<SysDept> depts = deptService.selectDeptList(dept);
        return BaseResult.ok(deptService.buildDeptTreeSelect(depts));
    }

    @ApiOperation(value = "加载对应角色部门列表树", notes = "加载对应角色部门列表树")
    @GetMapping(value = "/roleDeptTreeselect/{roleId}")
    public Object roleDeptTreeselect(@PathVariable("roleId") Long roleId) {
        List<SysDept> depts = deptService.selectDeptList(new SysDept());
        BaseResult ok = BaseResult.ok();
        Map<String, Object> ajax = MapUtils.objectToMapByReflect(ok);
        ajax.put("checkedKeys" , deptService.selectDeptListByRoleId(roleId));
        ajax.put("depts" , deptService.buildDeptTreeSelect(depts));
        return ajax;
    }

    @ApiOperation(value = "新增部门", notes = "新增部门")
    @PreAuthorize("@ss.hasPermi('system:dept:add')")
    @Log(title = "部门管理" , businessType = BusinessType.INSERT)
    @PostMapping
    public BaseResult add(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return BaseResult.fail("新增部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        }
        dept.setCreateBy(SecurityUtils.getUsername());
        return toAjax(deptService.insertDept(dept));
    }

    @ApiOperation(value = "修改部门", notes = "修改部门")
    @PreAuthorize("@ss.hasPermi('system:dept:edit')")
    @Log(title = "部门管理" , businessType = BusinessType.UPDATE)
    @PutMapping
    public BaseResult edit(@Validated @RequestBody SysDept dept) {
        if (UserConstants.NOT_UNIQUE.equals(deptService.checkDeptNameUnique(dept))) {
            return BaseResult.fail("修改部门'" + dept.getDeptName() + "'失败，部门名称已存在");
        } else if (dept.getParentId().equals(dept.getDeptId())) {
            return BaseResult.fail("修改部门'" + dept.getDeptName() + "'失败，上级部门不能是自己");
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.getStatus())
                && deptService.selectNormalChildrenDeptById(dept.getDeptId()) > 0) {
            return BaseResult.fail("该部门包含未停用的子部门！");
        }
        dept.setUpdateBy(SecurityUtils.getUsername());
        return toAjax(deptService.updateDept(dept));
    }

    @ApiOperation(value = "删除部门", notes = "删除部门")
    @PreAuthorize("@ss.hasPermi('system:dept:remove')")
    @Log(title = "部门管理" , businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    public BaseResult remove(@PathVariable Long deptId) {
        if (deptService.hasChildByDeptId(deptId)) {
            return BaseResult.fail("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return BaseResult.fail("部门存在用户,不允许删除");
        }
        return toAjax(deptService.deleteDeptById(deptId));
    }
}
