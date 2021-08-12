package com.taozi.common.utils.word;

import fr.opensagres.xdocreport.core.XDocReportException;
import fr.opensagres.xdocreport.document.IXDocReport;
import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.TemplateEngineKind;
import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 导出word utils
 *
 * @author taozi - 2021年7月27日, 027 - 10:32:41
 */
public class ExportWordUtils {

	public static void export(String templatePath, Map data, String outPutPath) throws IOException, XDocReportException {
		//获取Word模板，模板存放路径在项目的resources目录下
		//模板生成方式：选中文本按ctrl+f9,右键编辑域，选择 邮件合并.代码格式：${code}
		InputStream ins = ExportWordUtils.class.getResourceAsStream(templatePath);
		//注册xdocreport实例并加载FreeMarker模板引擎
		IXDocReport report = XDocReportRegistry.getRegistry().loadReport(ins,
				TemplateEngineKind.Freemarker);
		//创建xdocreport上下文对象
		IContext context = report.createContext();
		FieldsMetadata fm = report.createFieldsMetadata();
		//创建要替换的文本变量
		data.forEach((k, v) -> {
			try {
				context.put(k.toString(), v);
				if (v instanceof List) {
					//Word模板中的表格数据对应的集合类型
					fm.load(k.toString(), ((List<?>) v).get(0).getClass(), true);
				}
			} catch (XDocReportException e) {
				e.printStackTrace();
			}
		});

		//输出到本地目录
		FileOutputStream out = new FileOutputStream(outPutPath);
		report.process(context, out);
	}
}
