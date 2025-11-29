package com.sqlaudit.service;

import com.sqlaudit.entity.DataSourceConfig;
import java.util.List;

public interface DataSourceService {
    List<DataSourceConfig> getAllDataSources();
    DataSourceConfig getDataSourceById(Long id);
    DataSourceConfig createDataSource(DataSourceConfig dataSourceConfig);
    DataSourceConfig updateDataSource(Long id, DataSourceConfig dataSourceConfig);
    void deleteDataSource(Long id);
    boolean testConnection(Long id);
    boolean testConnection(DataSourceConfig dataSourceConfig);
}