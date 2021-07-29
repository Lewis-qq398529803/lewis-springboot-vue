package com.taozi.utils;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.taozi.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 自动生成mybatisplus的相关代码
 * @author TAOZI
 */
public class GeneratorCode {

    /**
     * 自动生成代码
     * @param addressAndPort    --格式：114.114.114.114:3306
     * @param databaseName      --表名
     * @param username          --用户名
     * @param password          --用户密码
     */
    public static void generatorStart(String addressAndPort, String databaseName, String username, String password, String parentPath, String tablesSelected, String moduleName, String prefix) {

        //代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //全局配置
        String projectPath = System.getProperty("user.dir");//获取当前工程路径
        GlobalConfig globalConfig = new GlobalConfig();

        globalConfig.setOutputDir(projectPath + "/src/main/java")
                .setActiveRecord(true)          //是否支持AR模式
                .setOpen(false)                 //生成后是否打开资源管理器
                .setFileOverride(true)          //是否覆盖已有文件
                .setSwagger2(true)              //开启Swagger2模式- 实体属性 Swagger2 注解
                .setActiveRecord(false)         //不需要ActiveRecord特性的请改为false
                .setEnableCache(false)          //XML 二级缓存
                .setBaseResultMap(false)        //XML ResultMap
                .setBaseColumnList(true)        //XML columList
                .setIdType(IdType.AUTO)         //主键策略
                .setDateType(DateType.ONLY_DATE)//定义生成的实体类中日期类型
//                .setServiceName("%sService")    //默认service接口名IXXXService 自定义指定之后就不会用I开头了
                .setBaseResultMap(true)         //生成基本的resultMap
                .setBaseColumnList(true)        //生成基本的SQL片段
                .setAuthor("taozi");            //生成作者注释

        mpg.setGlobalConfig(globalConfig);

        //数据源配置
        DataSourceConfig dsc = new DataSourceConfig();

        dsc.setUrl("jdbc:mysql://" + addressAndPort + "/" + databaseName + "?useUnicode=true&serverTimezone=GMT&useSSL=false&characterEncoding=utf8")
                .setDriverName("com.mysql.jdbc.Driver")//数据库驱动
                .setUsername(username)//数据库账号
                .setPassword(password);//数据库密码

        mpg.setDataSource(dsc);

        /* ============================================== 包配置 ====================================================*/
        PackageConfig pc = new PackageConfig();

        pc.setParent(parentPath)//放在哪个包下
                .setEntity("domain")
                .setMapper("mapper")
                .setService("service")
                .setServiceImpl("service.impl")
                .setController("controller");
        if (!moduleName.equals("")) {
            pc.setModuleName(moduleName);
        }
        mpg.setPackageInfo(pc);

        //配置模板
        mpg.setTemplate(new TemplateConfig().setXml(null));// 关闭默认 xml 生成，调整生成 至 根目录

        //策略配置
        StrategyConfig strategy = new StrategyConfig();

        strategy.setNaming(NamingStrategy.underline_to_camel)//设置字段和表名的是否把下划线完成驼峰命名规则
                .setColumnNaming(NamingStrategy.underline_to_camel)//数据库表字段映射到实体的命名策略
                .setSuperEntityClass("com.baomidou.mybatisplus.extension.activerecord.Model")//设置生成的实体类继承的父类
                .setEntityLombokModel(true)//是否启动lombok，lombok 模型 @Accessors(chain = true) setter链式操作
                .setRestControllerStyle(true)//是否生成restController
//                .setSuperControllerClass("com.baomidou.ant.common.BaseController")// 公共父类
                .setEntityLombokModel(true);
//        strategy.setSuperEntityColumns("id");// 写于父类中的公共字段

        // 5. 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        List<FileOutConfig> fileOutConfigList = new ArrayList<>();
        fileOutConfigList.add(new FileOutConfig("/templates/mapper.xml.ftl") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输入文件名称，如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                String mapperFilePath = projectPath
                        + "/src/main/resources/mapper/"
//                        + pc.getModuleName()
                        + tableInfo.getEntityName()
                        + "/" + tableInfo.getEntityName()
                        + "Mapper"
                        + StringPool.DOT_XML;
                System.out.println("mapperFilePath： " + mapperFilePath);
                return mapperFilePath;
            }
        });
        cfg.setFileOutConfigList(fileOutConfigList);
        mpg.setCfg(cfg);//将自定义配置放入自动生成工具类中

        if (!tablesSelected.equals("")) {//要设置生成哪些表 如果不设置就是生成所有的表
            //处理字符串成string[]
            String[] split = StringUtils.split(tablesSelected, ",");
            strategy.setInclude(split)
                    .setControllerMappingHyphenStyle(true)      //url中驼峰转连字符
                    .setTablePrefix(prefix)   //生成实体时去掉表前缀
                    .setLogicDeleteFieldName("deleted");        //逻辑删除字段设置
        }
        mpg.setStrategy(strategy);

        mpg.setTemplateEngine(new FreemarkerTemplateEngine());// 指定选择 freemarker 引擎，注意 pom 依赖必须有！
        mpg.execute();//导出
    }

    public static void main(String[] args) {
        //1、地址和端口 [114.114.114.114:8888]
        String ipAndPort = "localhost:3306";
        //2、数据库名名 [databaseName]
        String databaseName = "tmt_qn_rental_house";
        //3、用户名 [userName]
        String userName = "root";
        //4、用户密码 [password]
        String password = "";
        //5、父包路径 [com.taozi.parent]
        String parentPath = "com.taozi.system";
        //6、表名，多个英文逗号分割 不填默认生成所有表
        String tablesSelected = "" +
                "qn_bill" +
                ",qn_block" +
                ",qn_block_common_config" +
                ",qn_block_desc_template" +
                ",qn_config_params" +
                ",qn_device" +
                ",qn_device_record" +
                ",qn_house" +
                ",qn_house_common_config" +
                ",qn_house_config" +
                ",qn_lease" +
                ",qn_manager" +
                ",qn_message" +
                ",qn_tenant";
        //7、模块名，不填则不设置模块名
        String moduleName = "";
        //8、生成表时去掉前缀
        String prefix = "qn_";
        generatorStart(ipAndPort, databaseName, userName, password, parentPath, tablesSelected, moduleName, prefix);
    }
}