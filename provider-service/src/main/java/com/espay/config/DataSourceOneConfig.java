package com.espay.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.espay.mapper.one", sqlSessionTemplateRef = "sqlSessionTemplateOne")
public class DataSourceOneConfig {
    @Bean(name = "dataSourceOne")
    @Qualifier("dataSourceOne")
    @ConfigurationProperties(prefix = "spring.datasource.one")
    @Primary //设置主数据源
    public DataSource DataSourceOne() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;

    }

//    @Bean
//    public ServletRegistrationBean druidStatViewServlet() {
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        registrationBean.addInitParameter("allow", "127.0.0.1"); // IP白名单 (没有配置或者为空，则允许所有访问)
//        registrationBean.addInitParameter("deny", ""); // IP黑名单 (存在共同时，deny优先于allow)
//        registrationBean.addInitParameter("loginUsername", "admin");
//        registrationBean.addInitParameter("loginPassword", "admin");
//        registrationBean.addInitParameter("resetEnable", "false");
//        return registrationBean;
//    }
//
//    @Bean
//    public FilterRegistrationBean druidWebStatViewFilter() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean(new WebStatFilter());
//        registrationBean.addInitParameter("urlPatterns", "/*");
//        registrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*");
//        return registrationBean;
//    }


    @Bean(name = "sqlSessionFactoryOne")
    @Primary
    public SqlSessionFactory sqlSessionFactoryOne(@Qualifier("dataSourceOne") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/one/*.xml"));
//        bean.setConfigLocation("classpath:mybatis/mybatis-config.xml");
        return bean.getObject();
    }

    @Bean(name = "dataSourceTransactionManagerOne")
    @Primary
    public DataSourceTransactionManager dataSourceTransactionManagerOne(@Qualifier("dataSourceOne") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplateOne")
    @Primary
    public SqlSessionTemplate sqlSessionTemplateOne(@Qualifier("sqlSessionFactoryOne") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
