package com.lewis.core.base.page;

import com.lewis.core.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 分页数据
 *
 * @author Lewis
 */
@ApiModel("分页数据vo")
public class PageDomain {

	@ApiModelProperty(value = "当前记录起始索引")
	private Integer pageNum;

	@ApiModelProperty(value = "每页显示记录数")
	private Integer pageSize;

	@ApiModelProperty(value = "排序列")
	private String orderByColumn;

	@ApiModelProperty(value = "排序的方向desc或者asc")
	private String isAsc = "asc";

	public String getOrderBy() {
		if (StringUtils.isEmpty(orderByColumn)) {
			return "";
		}
		return StringUtils.toUnderScoreCase(orderByColumn) + " " + isAsc;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getOrderByColumn() {
		return orderByColumn;
	}

	public void setOrderByColumn(String orderByColumn) {
		this.orderByColumn = orderByColumn;
	}

	public String getIsAsc() {
		return isAsc;
	}

	public void setIsAsc(String isAsc) {
		// 兼容前端排序类型
		String isAsc1 = "ascending";
		String isAsc2 = "descending";
		if (isAsc1.equals(isAsc)) {
			isAsc = "asc";
		} else if (isAsc2.equals(isAsc)) {
			isAsc = "desc";
		}
		this.isAsc = isAsc;
	}
}
