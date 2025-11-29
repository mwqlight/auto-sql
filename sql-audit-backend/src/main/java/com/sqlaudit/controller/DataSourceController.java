package com.sqlaudit.controller;

import com.sqlaudit.dto.request.DataSourceConfigRequest;
import com.sqlaudit.dto.response.DataSourceConfigResponse;
import com.sqlaudit.dto.response.DataSourceTestResult;
import com.sqlaudit.entity.DataSourceConfig;
import com.sqlaudit.service.DataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/datasources")
@CrossOrigin(origins = "*")
public class DataSourceController {
    
    @Autowired
    private DataSourceService dataSourceService;
    
    @GetMapping
    public ResponseEntity<List<DataSourceConfigResponse>> getAllDataSources() {
        List<DataSourceConfig> dataSources = dataSourceService.getAllDataSources();
        List<DataSourceConfigResponse> responses = dataSources.stream().map(this::convertToResponse).collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DataSourceConfigResponse> getDataSourceById(@PathVariable Long id) {
        DataSourceConfig dataSource = dataSourceService.getDataSourceById(id);
        if (dataSource != null) {
            return ResponseEntity.ok(convertToResponse(dataSource));
        }
        return ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<DataSourceConfigResponse> createDataSource(@RequestBody DataSourceConfigRequest request) {
        DataSourceConfig dataSource = new DataSourceConfig(
            request.getName(),
            request.getType(),
            request.getHost(),
            request.getPort(),
            request.getDatabase(),
            request.getUsername(),
            request.getPassword()
        );
        
        DataSourceConfig savedDataSource = dataSourceService.createDataSource(dataSource);
        return ResponseEntity.ok(convertToResponse(savedDataSource));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DataSourceConfigResponse> updateDataSource(@PathVariable Long id, @RequestBody DataSourceConfigRequest request) {
        DataSourceConfig dataSource = new DataSourceConfig(
            request.getName(),
            request.getType(),
            request.getHost(),
            request.getPort(),
            request.getDatabase(),
            request.getUsername(),
            request.getPassword()
        );
        
        DataSourceConfig updatedDataSource = dataSourceService.updateDataSource(id, dataSource);
        if (updatedDataSource != null) {
            return ResponseEntity.ok(convertToResponse(updatedDataSource));
        }
        return ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataSource(@PathVariable Long id) {
        dataSourceService.deleteDataSource(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/{id}/test")
    public ResponseEntity<DataSourceTestResult> testConnection(@PathVariable Long id) {
        boolean success = dataSourceService.testConnection(id);
        DataSourceTestResult result = new DataSourceTestResult();
        result.setSuccess(success);
        result.setMessage(success ? "连接成功" : "连接失败");
        return ResponseEntity.ok(result);
    }
    
    private DataSourceConfigResponse convertToResponse(DataSourceConfig dataSource) {
        return new DataSourceConfigResponse(
            dataSource.getId(),
            dataSource.getName(),
            dataSource.getType(),
            dataSource.getHost(),
            dataSource.getPort(),
            dataSource.getDatabase(),
            dataSource.getUsername(),
            dataSource.getCreatedAt(),
            dataSource.getUpdatedAt()
        );
    }
}