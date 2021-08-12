package com.taozi.common.utils.word.demo;

import com.taozi.common.utils.word.ExportWordUtils;
import com.taozi.common.utils.word.vo.ExportWordVO;
import fr.opensagres.xdocreport.core.XDocReportException;
//import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportWord {

//	@Test
	public void demo(ExportWordVO exportWordVO) throws IOException, XDocReportException {
		//通过模板编号队对应模板路径
		Long templatePathNum = exportWordVO.getTemplatePathNum();
		String templatePaht = "/wordTemplates/" + templatePathNum + ".docx";
		String outPutPathT = exportWordVO.getOutPutPath();
		String outPutPath = "";
		if (outPutPathT == null || "".equals(outPutPathT)) {
			outPutPath = "D:\\Workspace\\Mine\\exportworddemo\\src\\main\\resources\\wordDownload\\导出完成.docx";
		} else {
			outPutPath = outPutPathT;
		}
		Map m = new HashMap();
		m.put("title", "深圳市");
		m.put("content", "这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容");
		m.put("red", "红色字体");
		m.put("date", "2021-07-26");

		List<User> userList = new ArrayList<User>();
		User user1 = new User();
		user1.setName("张三");
		user1.setAge(10);
		user1.setGender("男");
		User user2 = new User();
		user2.setName("李四");
		user2.setAge(101);
		user2.setGender("女");
		User user3 = new User();
		user3.setName("王五");
		user3.setAge(44);
		user3.setGender("男");
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		m.put("user", userList);
		Goods g1 = new Goods();
		g1.setName("商品1");
		Goods g2 = new Goods();
		g2.setName("商品2");
		List<Goods> goodsList = new ArrayList<>();
		goodsList.add(g1);
		goodsList.add(g2);
		m.put("goods", goodsList);
		ExportWordUtils.export(templatePaht, m, outPutPath);
	}
}