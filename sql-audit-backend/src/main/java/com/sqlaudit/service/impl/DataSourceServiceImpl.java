package com.sqlaudit.service.impl;

import com.sqlaudit.entity.DataSourceConfig;
import com.sqlaudit.repository.DataSourceConfigRepository;
import com.sqlaudit.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DataSourceServiceImpl implements DataSourceService {
    
    @Autowired
    private DataSourceConfigRepository dataSourceConfigRepository;
    
    @Override
    public List<DataSourceConfig> getAllDataSources() {
        return dataSourceConfigRepository.findAll();
    }
    
    @Override
    public DataSourceConfig getDataSourceById(Long id) {
        Optional<DataSourceConfig> dataSourceConfig = dataSourceConfigRepository.findById(id);
        return dataSourceConfig.orElse(null);
    }
    
    @Override
    public DataSourceConfig createDataSource(DataSourceConfig dataSourceConfig) {
        dataSourceConfig.setCreatedAt(LocalDateTime.now());
        dataSourceConfig.setUpdatedAt(LocalDateTime.now());
        return dataSourceConfigRepository.save(dataSourceConfig);
    }
    
    @Override
    public DataSourceConfig updateDataSource(Long id, DataSourceConfig dataSourceConfig) {
        Optional<DataSourceConfig> existingConfig = dataSourceConfigRepository.findById(id);
        if (existingConfig.isPresent()) {
            DataSourceConfig config = existingConfig.get();
            config.setName(dataSourceConfig.getName());
            config.setType(dataSourceConfig.getType());
            config.setHost(dataSourceConfig.getHost());
            config.setPort(dataSourceConfig.getPort());
            config.setDatabase(dataSourceConfig.getDatabase());
            config.setUsername(dataSourceConfig.getUsername());
            config.setPassword(dataSourceConfig.getPassword());
            config.setUpdatedAt(LocalDateTime.now());
            return dataSourceConfigRepository.save(config);
        }
        return null;
    }
    
    @Override
    public void deleteDataSource(Long id) {
        dataSourceConfigRepository.deleteById(id);
    }
    
    @Override
    public boolean testConnection(Long id) {
        DataSourceConfig config = getDataSourceById(id);
        if (config == null) {
            return false;
        }
        
        String url = buildJdbcUrl(config);
        try (Connection connection = DriverManager.getConnection(url, config.getUsername(), config.getPassword())) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    @Override
    public boolean testConnection(DataSourceConfig dataSourceConfig) {
        String url = buildJdbcUrl(dataSourceConfig);
        try (Connection connection = DriverManager.getConnection(url, dataSourceConfig.getUsername(), dataSourceConfig.getPassword())) {
            return connection.isValid(5);
        } catch (SQLException e) {
            return false;
        }
    }
    
    private String buildJdbcUrl(DataSourceConfig config) {
        switch (config.getType().toUpperCase()) {
            case "MYSQL":
                return String.format("jdbc:mysql://%s:%d/%s", config.getHost(), config.getPort(), config.getDatabase());
            case "POSTGRESQL":
                return String.format("jdbc:postgresql://%s:%d/%s", config.getHost(), config.getPort(), config.getDatabase());
            case "ORACLE":
                return String.format("jdbc:oracle:thin:@%s:%d:%s", config.getHost(), config.getPort(), config.getDatabase());
            case "SQLSERVER":
                return String.format("jdbc:sqlserver://%s:%d;databaseName=%s", config.getHost(), config.getPort(), config.getDatabase());
            default:
                throw new IllegalArgumentException("Unsupported database type: " + config.getType());
        }
    }
}