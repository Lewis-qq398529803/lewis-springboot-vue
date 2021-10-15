package com.lewis.mvc.system.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.constant.UserConstants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.domain.entity.SysRole;
import com.lewis.core.base.domain.entity.SysUser;
import com.lewis.core.base.domain.model.LoginUser;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.ServletUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.poi.ExcelUtil;
import com.lewis.mvc.framework.service.ISysPermissionService;
import com.lewis.mvc.framework.service.ITokenService;
import com.lewis.mvc.system.entity.SysUserRole;
import com.lewis.mvc.system.service.ISysRoleService;
import com.lewis.mvc.system.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色信息
 *
 * @author Lewis
 */
@Api(tags = "角色信息")
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

	@Autowired
	private ISysRoleService roleService;

	@Autowired
	private ITokenService tokenService;

	@Autowired
	private ISysPermissionService permissionService;

	@Autowired
	private ISysUserService userService;

	@ApiOperation(value = "获取角色列表", notes = "获取角色列表")
	@PreAuthorize("@ss.hasPermi('system:role:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysRole role) {
		startPage();
		List<SysRole> list = roleService.selectRoleList(role);
		return getDataTable(list);
	}

	@ApiOperation(value = "导出角色列表", notes = "导出角色列表")
	@Log(title = "角色管理", businessType = BusinessType.EXPORT)
	@PreAuthorize("@ss.hasPermi('system:role:export')")
	@GetMapping("/export")
	public BaseResult export(SysRole role) {
		List<SysRole> list = roleService.selectRoleList(role);
		ExcelUtil<SysRole> util = new ExcelUtil<SysRole>(SysRole.class);
		return util.exportExcel(list, "角色数据");
	}

	@ApiOperation(value = "根据角色编号获取详细信息", notes = "根据角色编号获取详细信息")
	@PreAuthorize("@ss.hasPermi('system:role:query')")
	@GetMapping(value = "/{roleId}")
	public BaseResult getInfo(@PathVariable Long roleId) {
		return BaseResult.ok(roleService.selectRoleById(roleId));
	}

	@ApiOperation(value = "新增角色", notes = "新增角色")
	@PreAuthorize("@ss.hasPermi('system:role:add')")
	@Log(title = "角色管理", businessType = BusinessType.INSERT)
	@PostMapping
	public BaseResult add(@Validated @RequestBody SysRole role) {
		if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return BaseResult.fail("新增角色'" + role.getRoleName() + "'失败，角色名称已存在");
		} else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
			return BaseResult.fail("新增角色'" + role.getRoleName() + "'失败，角色权限已存在");
		}
		role.setCreateBy(SecurityUtils.getUsername());
		return toAjax(roleService.insertRole(role));

	}

	@ApiOperation(value = "修改保存角色", notes = "修改保存角色")
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping
	public BaseResult edit(@Validated @RequestBody SysRole role) {
		roleService.checkRoleAllowed(role);
		if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
			return BaseResult.fail("修改角色'" + role.getRoleName() + "'失败，角色名称已存在");
		} else if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleKeyUnique(role))) {
			return BaseResult.fail("修改角色'" + role.getRoleName() + "'失败，角色权限已存在");
		}
		role.setUpdateBy(SecurityUtils.getUsername());

		if (roleService.updateRole(role) > 0) {
			// 更新缓存用户权限
			LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
			if (StringUtils.isNotNull(loginUser.getUser()) && !loginUser.getUser().isAdmin()) {
				loginUser.setPermissions(permissionService.getMenuPermission(loginUser.getUser()));
				loginUser.setUser(userService.selectUserByUserName(loginUser.getUser().getUserName()));
				tokenService.setLoginUser(loginUser);
			}
			return BaseResult.ok();
		}
		return BaseResult.fail("修改角色'" + role.getRoleName() + "'失败，请联系管理员");
	}

	@ApiOperation(value = "修改保存数据权限", notes = "修改保存数据权限")
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping("/dataScope")
	public BaseResult dataScope(@RequestBody SysRole role) {
		roleService.checkRoleAllowed(role);
		return toAjax(roleService.authDataScope(role));
	}

	@ApiOperation(value = "状态修改", notes = "状态修改")
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus")
	public BaseResult changeStatus(@RequestBody SysRole role) {
		roleService.checkRoleAllowed(role);
		role.setUpdateBy(SecurityUtils.getUsername());
		return toAjax(roleService.updateRoleStatus(role));
	}

	@ApiOperation(value = "删除角色", notes = "删除角色")
	@PreAuthorize("@ss.hasPermi('system:role:remove')")
	@Log(title = "角色管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{roleIds}")
	public BaseResult remove(@PathVariable Long[] roleIds) {
		return toAjax(roleService.deleteRoleByIds(roleIds));
	}

	@ApiOperation(value = "获取角色选择框列表", notes = "获取角色选择框列表")
	@PreAuthorize("@ss.hasPermi('system:role:query')")
	@GetMapping("/optionselect")
	public BaseResult optionselect() {
		return BaseResult.ok(roleService.selectRoleAll());
	}

	@ApiOperation(value = "查询已分配用户角色列表", notes = "查询已分配用户角色列表")
	@PreAuthorize("@ss.hasPermi('system:role:list')")
	@GetMapping("/authUser/allocatedList")
	public TableDataInfo allocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectAllocatedList(user);
		return getDataTable(list);
	}

	@ApiOperation(value = "查询未分配用户角色列表", notes = "查询未分配用户角色列表")
	@PreAuthorize("@ss.hasPermi('system:role:list')")
	@GetMapping("/authUser/unallocatedList")
	public TableDataInfo unallocatedList(SysUser user) {
		startPage();
		List<SysUser> list = userService.selectUnallocatedList(user);
		return getDataTable(list);
	}

	@ApiOperation(value = "取消授权用户", notes = "取消授权用户")
	@PreAuthorize("@ss.hasPermi('system:role:edit')")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PutMapping("/authUser/cancel")
	public BaseResult cancelAuthUser(@RequestBody SysUserRole userRole) {
		return toAjax(roleService.deleteAuthUser(userRole));
	}

	@ApiOperation(value = "批量取消授权用户", notes = "批量取消授权用户")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PutMapping("/authUser/cancelAll")
	public BaseResult cancelAuthUserAll(Long roleId, Long[] userIds) {
		return toAjax(roleService.deleteAuthUsers(roleId, userIds));
	}

	@ApiOperation(value = "批量选择用户授权", notes = "批量选择用户授权")
	@Log(title = "角色管理", businessType = BusinessType.GRANT)
	@PutMapping("/authUser/selectAll")
	public BaseResult selectAuthUserAll(Long roleId, Long[] userIds) {
		return toAjax(roleService.insertAuthUsers(roleId, userIds));
	}

}
