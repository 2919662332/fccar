package cn.itsource;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Genterator {

    public static void main(String[] args) {
        //读取配置文件
        ResourceBundle rb = ResourceBundle.getBundle("config-pay");
        //业务代码输出路径
        String outputDir = rb.getString("OutputDir");
        //sql映射文件输出路径
        String outputDirXml = rb.getString("OutputDirXml");
        //作者
        String author = rb.getString("author");

        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        // 代码输出位置
        gc.setOutputDir(outputDir);
        // 作者
        gc.setAuthor(author);
        // 打开代码生成目录
        gc.setOpen(true);
        //生成 resultMap
        gc.setBaseResultMap(true);
        //生成查询列明
        gc.setBaseColumnList(true);
        //日期类型
        gc.setDateType(DateType.ONLY_DATE);
        //ID使用雪花算法
        gc.setIdType(IdType.ASSIGN_ID);

        //添加接口文档注解
        gc.setSwagger2(true);

        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        // 数据库类型
        dsc.setDbType(DbType.MYSQL);
        dsc.setTypeConvert(new MySqlTypeConvert());
        // 连接属性
        dsc.setDriverName(rb.getString("jdbc.driver"));
        dsc.setUsername(rb.getString("jdbc.user"));
        dsc.setPassword(rb.getString("jdbc.pwd"));
        dsc.setUrl(rb.getString("jdbc.url"));
        mpg.setDataSource(dsc);

        // 表策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setTablePrefix(new String[] { "t_" });// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略

        strategy.setInclude(new String[]{
                "t_pay_order",
                "t_wxpay_flow"
        }); // 需要生成的表

        //使用lombok
        strategy.setEntityLombokModel(true);
        strategy.setEntitySerialVersionUID(true);
        //乐观锁字段
        strategy.setVersionFieldName("version");
        //逻辑删除字段
        strategy.setLogicDeleteFieldName("deleted");
        //domain的父类
        //strategy.setSuperEntityClass("cn.itsource.pojo.BaseDomain");

        //controller的父类
        //strategy.setSuperControllerClass("cn.itsource.controller.BaseController");
        //生成注解
        strategy.setEntityTableFieldAnnotationEnable(true);
        strategy.setEntitySerialVersionUID(true);

        mpg.setStrategy(strategy);

        // 包配置
        PackageConfig pc = new PackageConfig();
        //基础路径 cn.xxx
        pc.setParent(rb.getString("parent"));
        //controller的包
        pc.setController("controller.manager");
        pc.setService("service");
        pc.setServiceImpl("service.impl");
        pc.setEntity("pojo.domain");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 注入自定义配置，可以在 VM 中使用 cfg.abc 【可无】
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                this.setMap(new HashMap<String, Object>());
            }
        };

        //文件生成配置
        List<FileOutConfig> focList = new ArrayList<FileOutConfig>();

        //controller的输出配置
        focList.add(new FileOutConfig("/templates/controller.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //合并好的内容输出到哪儿？
                return outputDir+ "/cn/itsource/controller/manager/" + tableInfo.getEntityName() + "Controller.java";
            }
        });


        // 调整 domain 生成目录演示
        focList.add(new FileOutConfig("/templates/entity.java.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outputDir+ "/cn/itsource/pojo/domain/" + tableInfo.getEntityName() + ".java";
            }
        });

        // 调整 xml 生成目录演示
        focList.add(new FileOutConfig("/templates/mapper.xml.vm") {
            @Override
            public String outputFile(TableInfo tableInfo) {
                return outputDirXml+ "/cn/itsource/mapper/" + tableInfo.getEntityName() + "Mapper.xml";
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);

        // 自定义模板配置，可以 copy 源码 mybatis-plus/src/main/resources/templates 下面内容修改，
        // 放置自己项目的 src/main/resources/templates 目录下, 默认名称可以不配置，也可以自定义模板名称
        TemplateConfig tc = new TemplateConfig();
        tc.setService("/templates/service.java.vm");
        tc.setServiceImpl("/templates/serviceImpl.java.vm");
        tc.setMapper("/templates/mapper.java.vm");
        tc.setEntity(null);
        tc.setController(null);
        tc.setXml(null);
        // 如上任何一个模块如果设置 空 OR Null 将不生成该模块。
        mpg.setTemplate(tc);

        // 执行生成
        mpg.execute();
    }

}