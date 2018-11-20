package com.baomidou.mybatisplus.test.generator;

import org.junit.Test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

/**
 * <p>
 * 代码生成器 示例
 * </p>
 *
 * @author K神
 * @since 2017/12/29
 */
public class CodeGeneratorTest {

    /**
     * 是否强制带上注解
     */
    boolean enableTableFieldAnnotation = false;
    /**
     * 生成的注解带上IdType类型
     */
    IdType tableIdType = null;
    /**
     * 是否去掉生成实体的属性名前缀
     */
    String[] fieldPrefix = null;
    /**
     * 生成的Service 接口类名是否以I开头
     * 默认是以I开头
     * user表 -> IUserService, UserServiceImpl
     */
    boolean serviceClassNameStartWithI = true;

    @Test
    public void generateCode() {
        String packageName = "com.baomidou.springboot";
        enableTableFieldAnnotation = false;
        tableIdType = null;
        generateByTables(packageName + ".noannoidtype", "user");
        enableTableFieldAnnotation = true;
        tableIdType = null;
        generateByTables(packageName + ".noidtype", "user");
        enableTableFieldAnnotation = false;
        tableIdType = IdType.INPUT;
        generateByTables(packageName + ".noanno", "user");
        enableTableFieldAnnotation = true;
        tableIdType = IdType.INPUT;
        generateByTables(packageName + ".both", "user");

        fieldPrefix = new String[]{"test"};
        enableTableFieldAnnotation = false;
        tableIdType = null;
        generateByTables(packageName + ".noannoidtypewithprefix", "user");
        enableTableFieldAnnotation = true;
        tableIdType = null;
        generateByTables(packageName + ".noidtypewithprefix", "user");
        enableTableFieldAnnotation = false;
        tableIdType = IdType.INPUT;
        generateByTables(packageName + ".noannowithprefix", "user");
        enableTableFieldAnnotation = true;
        tableIdType = IdType.INPUT;
        generateByTables(packageName + ".withannoidtypeprefix", "user");

        serviceClassNameStartWithI = false;
        generateByTables(packageName, "user");
    }

    private void generateByTables(String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://localhost:3306/mybatis-plus";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType(DbType.MYSQL);
        dataSourceConfig.setUrl(dbUrl);
        dataSourceConfig.setUsername("root");
        dataSourceConfig.setPassword("");
        dataSourceConfig.setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
            .setCapitalMode(true)
            .setEntityLombokModel(false)
            // .setDbColumnUnderline(true) 改为如下 2 个配置
            .setNaming(NamingStrategy.underline_to_camel)
            .setColumnNaming(NamingStrategy.underline_to_camel)
            .entityTableFieldAnnotationEnable(enableTableFieldAnnotation)
            .setFieldPrefix(fieldPrefix)//test_id -> id, test_type -> type
            .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false);
        config.setIdType(tableIdType);
        config.setAuthor("K神带你飞");
        config.setOutputDir("d:\\codeGen");
        config.setFileOverride(true);
        if (!serviceClassNameStartWithI) {
            config.setServiceName("%sService");
        }
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setParent(packageName);
        packageConfig.setController("controller");
        packageConfig.setEntity("entity");
        AutoGenerator autoGenerator =new AutoGenerator();
        autoGenerator.setGlobalConfig(config);
        autoGenerator.setDataSource(dataSourceConfig);
        autoGenerator.setStrategy(strategyConfig);
        autoGenerator.setPackageInfo(packageConfig);
        autoGenerator.execute();
    }
}
