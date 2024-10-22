package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.DbDuplicateDataProcessingDto;
import com.datamon.datamon2.mapper.repository.DbDuplicateDataProcessingMapper;
import com.datamon.datamon2.repository.jpa.DbDuplicateDataProcessingRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DbDuplicationDataProcessingService {
    private DbDuplicateDataProcessingRepository dbDuplicateDataProcessingRepository;
    private DbDuplicateDataProcessingMapper dbDuplicateDataProcessingMapper;

    public DbDuplicationDataProcessingService(DbDuplicateDataProcessingRepository dbDuplicateDataProcessingRepository, DbDuplicateDataProcessingMapper dbDuplicateDataProcessingMapper) {
        this.dbDuplicateDataProcessingRepository = dbDuplicateDataProcessingRepository;
        this.dbDuplicateDataProcessingMapper = dbDuplicateDataProcessingMapper;
    }

    public List<DbDuplicateDataProcessingDto> getByDbCode(String dbCode){
        List<DbDuplicateDataProcessingDto> result = new ArrayList<>();
        dbDuplicateDataProcessingRepository.findByDbCode(dbCode).forEach(entity -> {
            result.add(dbDuplicateDataProcessingMapper.toDto(entity));
        });
        return result;
    }
}
