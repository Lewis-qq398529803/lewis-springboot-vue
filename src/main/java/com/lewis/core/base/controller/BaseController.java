package com.lewis.core.base.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lewis.core.constant.HttpStatus;
import com.lewis.core.base.domain.BaseResult;
import com.lewis.core.base.page.PageDomain;
import com.lewis.core.base.page.TableDataInfo;
import com.lewis.core.base.page.TableSupport;
import com.lewis.core.utils.DateUtils;
import com.lewis.core.utils.StringUtils;
import com.lewis.core.utils.sql.SqlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;

/**
 * web层通用数据处理
 *
 * @author Lewis
 */
public class BaseController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize)) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }

    /**
     * 设置请求排序数据
     */
    protected void startOrderBy() {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes" , "unchecked"})
    protected TableDataInfo getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("查询成功");
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return rspData;
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected BaseResult toAjax(int rows) {
        return rows > 0 ? BaseResult.ok() : BaseResult.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected BaseResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public BaseResult success() {
        return BaseResult.ok();
    }

    /**
     * 返回失败消息
     */
    public BaseResult error() {
        return BaseResult.fail();
    }

    /**
     * 返回成功消息
     */
    public BaseResult success(String message) {
        return BaseResult.ok(message);
    }

    /**
     * 返回失败消息
     */
    public BaseResult error(String message) {
        return BaseResult.fail(message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StringUtils.format("redirect:{}" , url);
    }
}
