package com.redooper.multi.datasource.autoconfigure;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Auther: Jackie
 * @Date: 2019-08-03 18:10
 * @Description:
 */
@Data
@Configuration
@SuppressWarnings("ConfigurationProperties")
public class MultiDataSourceProperties<T extends DataSource> {

    protected String defaultTargetDataSource;

    protected Map<String, T> targetDataSources = new HashMap<>(0);

    @Configuration
    @ConditionalOnClass(HikariDataSource.class)
    public static class Hikari extends MultiDataSourceProperties<HikariDataSource> {

        @Bean
        @ConfigurationProperties("spring.multi-datasource")
        public MultiDataSourceProperties<HikariDataSource> multiDataSourceProperties() {
            return new Hikari();
        }
    }

    @Configuration
    @ConditionalOnClass(DruidDataSource.class)
    public static class Druid extends MultiDataSourceProperties<DruidDataSource> implements InitializingBean {

        @Override
        public void afterPropertiesSet() throws Exception {
            for (Map.Entry<String, DruidDataSource> entry : targetDataSources.entrySet()) {
                entry.getValue().init();
            }
        }

        @Bean
        @ConfigurationProperties("spring.multi-datasource")
        public MultiDataSourceProperties<DruidDataSource> multiDataSourceProperties() {
            return new Druid();
        }
    }
}
