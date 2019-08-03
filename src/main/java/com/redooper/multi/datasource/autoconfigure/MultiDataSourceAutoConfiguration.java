package com.redooper.multi.datasource.autoconfigure;

import com.redooper.multi.datasource.aspect.MultiDataSourceAspect;
import com.redooper.multi.datasource.core.MultiDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Auther: Jackie
 * @Date: 2019-08-03 18:11
 * @Description:
 */
@Configuration
@Import(MultiDataSourceProperties.class)
@EnableAspectJAutoProxy
public class MultiDataSourceAutoConfiguration {

    @Bean
    public DataSource dataSource(MultiDataSourceProperties multiDataSourceProperties) {
        String lookupKey = multiDataSourceProperties.getDefaultTargetDataSource();
        Map targetDataSources = multiDataSourceProperties.getTargetDataSources();
        return new MultiDataSource(targetDataSources.get(lookupKey), targetDataSources);
    }

    @Bean
    public MultiDataSourceAspect multiDataSourceAspect() {
        return new MultiDataSourceAspect();
    }
}
