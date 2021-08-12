package com.taozi.common.utils.word.demo;

public class User {

    private String name;
    private Integer age;
    private String gender;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}



//package com.exportword.demo;
//
//        import fr.opensagres.xdocreport.core.XDocReportException;
//        import fr.opensagres.xdocreport.document.IXDocReport;
//        import fr.opensagres.xdocreport.document.registry.XDocReportRegistry;
//        import fr.opensagres.xdocreport.template.IContext;
//        import fr.opensagres.xdocreport.template.TemplateEngineKind;
//        import fr.opensagres.xdocreport.template.formatter.FieldsMetadata;
//        import org.junit.Test;
//
//        import java.io.File;
//        import java.io.FileOutputStream;
//        import java.io.IOException;
//        import java.io.InputStream;
//        import java.lang.reflect.Field;
//        import java.util.*;

//public class ExportWord {
//
//
//    @Test
//    public void demo() throws IOException, XDocReportException {
//
//        String templatePaht = "/wordTemplates/导出模板.docx";
//        String outPutPath = "D:\\java\\exportword\\src\\main\\resources\\wordDownload\\导出完成.docx";
//        Map m = new HashMap();
//        m.put("title", "深圳市");
//        m.put("content", "这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容这里是正文内容");
//        m.put("red", "红色字体");
//        m.put("date", "2021-07-26");
//
////        List<User> userList = new ArrayList<User>();
////        User user1 = new User();
////        user1.setName("张三");
////        user1.setAge(10);
////        user1.setGender("男");
////        User user2 = new User();
////        user2.setName("李四");
////        user2.setAge(101);
////        user2.setGender("女");
////        User user3 = new User();
////        user3.setName("王五");
////        user3.setAge(44);
////        user3.setGender("男");
////        userList.add(user1);
////        userList.add(user2);
////        userList.add(user3);
////        m.put("user", userList);
//        Goods g1=new Goods();
//        g1.setName("商品1");
//        Goods g2=new Goods();
//        g2.setName("商品2");
//        List<Goods> goodsList = new ArrayList<>();
//        goodsList.add(g1);
//        goodsList.add(g2);
//        m.put("goodsbs", goodsList);
//        export(templatePaht, m, outPutPath);
//    }
//
//
//    public void export(String templatePaht, Map data, String outPutPath) throws IOException, XDocReportException {
//        //获取Word模板，模板存放路径在项目的resources目录下
//        //模板生成方式：选中文本按ctrl+f9,右键编辑域，选择 邮件合并.代码格式：${code}
//        InputStream ins = this.getClass().getResourceAsStream(templatePaht);
//        //注册xdocreport实例并加载FreeMarker模板引擎
//        IXDocReport report = XDocReportRegistry.getRegistry().loadReport(ins,
//                TemplateEngineKind.Freemarker);
//        //创建xdocreport上下文对象
//        IContext context = report.createContext();
//
//        //创建要替换的文本变量
//        data.forEach((k, v) -> {
//            try {
//                context.put(k.toString(), v);
//                if (v instanceof List) {
//                    //Word模板中的表格数据对应的集合类型
//                    FieldsMetadata fm = report.createFieldsMetadata();
//                    fm.load(k.toString(), ((List<?>) v).get(0).getClass(), true);
//                }
//            } catch (XDocReportException e) {
//                e.printStackTrace();
//            }
//        });
//
//        //输出到本地目录
//        FileOutputStream out = new FileOutputStream(new File(outPutPath));
//        report.process(context, out);
//    }
//}
