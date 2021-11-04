package com.lewis.mvc.quartz.controller;

import com.lewis.core.annotation.Log;
import com.lewis.core.constant.Constants;
import com.lewis.core.base.controller.BaseController;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.enums.BusinessType;
import com.lewis.core.exception.job.TaskException;
import com.lewis.core.utils.SecurityUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.ExcelUtil;
import com.lewis.mvc.quartz.entity.SysJob;
import com.lewis.mvc.quartz.service.ISysJobService;
import com.lewis.core.utils.quartz.CronUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 调度任务信息操作处理
 *
 * @author Lewis
 */
@RestController
@RequestMapping("/monitor/job")
public class SysJobController extends BaseController {

	@Autowired
	private ISysJobService jobService;

	/**
	 * 查询定时任务列表
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysJob sysJob) {
		startPage();
		List<SysJob> list = jobService.selectJobList(sysJob);
		return getDataTable(list);
	}

	/**
	 * 导出定时任务列表
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:export')")
	@Log(title = "定时任务", businessType = BusinessType.EXPORT)
	@GetMapping("/export")
	public BaseResult export(SysJob sysJob) {
		List<SysJob> list = jobService.selectJobList(sysJob);
		ExcelUtil<SysJob> util = new ExcelUtil<SysJob>(SysJob.class);
		return util.exportExcel(list, "定时任务");
	}

	/**
	 * 获取定时任务详细信息
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:query')")
	@GetMapping(value = "/{jobId}")
	public BaseResult getInfo(@PathVariable("jobId") Long jobId) {
		return BaseResult.ok(jobService.selectJobById(jobId));
	}

	/**
	 * 新增定时任务
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:add')")
	@Log(title = "定时任务", businessType = BusinessType.INSERT)
	@PostMapping
	public BaseResult add(@RequestBody SysJob job) throws SchedulerException, TaskException {
		if (!CronUtils.isValid(job.getCronExpression())) {
			return error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
		} else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
			return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi://'调用");
		} else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
			return error("新增任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)//'调用");
		}
		job.setCreateBy(SecurityUtils.getUsername());
		return toAjax(jobService.insertJob(job));
	}

	/**
	 * 修改定时任务
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:edit')")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PutMapping
	public BaseResult edit(@RequestBody SysJob job) throws SchedulerException, TaskException {
		if (!CronUtils.isValid(job.getCronExpression())) {
			return error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
		} else if (StringUtils.containsIgnoreCase(job.getInvokeTarget(), Constants.LOOKUP_RMI)) {
			return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'rmi://'调用");
		} else if (StringUtils.containsAnyIgnoreCase(job.getInvokeTarget(), new String[]{Constants.HTTP, Constants.HTTPS})) {
			return error("修改任务'" + job.getJobName() + "'失败，目标字符串不允许'http(s)//'调用");
		}
		job.setUpdateBy(SecurityUtils.getUsername());
		return toAjax(jobService.updateJob(job));
	}

	/**
	 * 定时任务状态修改
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PutMapping("/changeStatus")
	public BaseResult changeStatus(@RequestBody SysJob job) throws SchedulerException {
		SysJob newJob = jobService.selectJobById(job.getJobId());
		newJob.setStatus(job.getStatus());
		return toAjax(jobService.changeStatus(newJob));
	}

	/**
	 * 定时任务立即执行一次
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:changeStatus')")
	@Log(title = "定时任务", businessType = BusinessType.UPDATE)
	@PutMapping("/run")
	public BaseResult run(@RequestBody SysJob job) throws SchedulerException {
		jobService.run(job);
		return BaseResult.ok();
	}

	/**
	 * 删除定时任务
	 */
	@PreAuthorize("@ss.hasPermi('monitor:job:remove')")
	@Log(title = "定时任务", businessType = BusinessType.DELETE)
	@DeleteMapping("/{jobIds}")
	public BaseResult remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException {
		jobService.deleteJobByIds(jobIds);
		return BaseResult.ok();
	}
}
