package com.taozi.common.utils.word.vo;

import lombok.Data;

/**
 * 导出word vo
 *
 * @author taozi - 2021年7月27日, 027 - 10:35:30
 */
@Data
public class ExportWordVO {

	/**
	 * 摸板编号
	 */
	public Long templatePathNum;

	/**
	 * 输出路径
	 */
	public String outPutPath;
}
