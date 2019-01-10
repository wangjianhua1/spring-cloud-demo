package com.espay.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.espay.mapper.three", sqlSessionTemplateRef = "sqlSessionTemplateThree")
public class DataSourceThreeConfig {
    @Bean(name = "dataSourceThree")
    @Qualifier("dataSourceThree")
    @ConfigurationProperties(prefix = "spring.datasource.third")
    public DataSource DataSourceThree() {
        DruidDataSource dataSource = new DruidDataSource();
        return dataSource;

    }

//    @Bean
//    public ServletRegistrationBean druidStatViewServlet() {
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        registrationBean.addInitParameter("allow", "127.0.0.1"); // IP白名单 (没有配置或者为空，则允许所有访问)
//        registrationBean.addInitParameter("deny", ""); // IP黑名单 (存在共同时，deny优先于allow)
//        registrationBean.addInitParameter("resetEnable", "false");
//        registrationBean.addInitParameter("loginPassword", "admin");
//        registrationBean.addInitParameter("loginUsername", "admin");
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


    @Bean(name = "sqlSessionFactoryThree")
    public SqlSessionFactory sqlSessionFactoryThree(@Qualifier("dataSourceThree") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/three/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "dataSourceTransactionManagerThree")
    public DataSourceTransactionManager dataSourceTransactionManagerThree(@Qualifier("dataSourceThree") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "sqlSessionTemplateThree")
    public SqlSessionTemplate sqlSessionTemplateThree(@Qualifier("sqlSessionFactoryThree") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
