package com.redooper.multi.datasource.core;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.Map;

/**
 * @Auther: Jackie
 * @Date: 2019-08-03 17:21
 * @Description:
 */
@AllArgsConstructor
public class MultiDataSource extends AbstractRoutingDataSource {

    private Object defaultTargetDataSource;

    private Map<Object, Object> targetDataSources;

    @Override
    public void afterPropertiesSet() {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return MultiDataSourceManager.peek();
    }
}
