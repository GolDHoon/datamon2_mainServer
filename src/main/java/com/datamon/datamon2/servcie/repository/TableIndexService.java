package com.datamon.datamon2.servcie.repository;

import com.datamon.datamon2.dto.repository.TableIndexDto;
import com.datamon.datamon2.entity.TableIndexEntity;
import com.datamon.datamon2.mapper.repository.TableIndexMapper;
import com.datamon.datamon2.repository.jpa.TableIndexRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TableIndexService {
    private TableIndexRepository tableIndexRepository;
    private TableIndexMapper tableIndexMapper;

    public TableIndexService(TableIndexRepository tableIndexRepository, TableIndexMapper tableIndexMapper) {
        this.tableIndexRepository = tableIndexRepository;
        this.tableIndexMapper = tableIndexMapper;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public TableIndexDto save(TableIndexDto tableIndexDto){
        return tableIndexMapper.toDto(tableIndexRepository.save(tableIndexMapper.toEntity(tableIndexDto)));
    }

    @Transactional(readOnly = true)
    public TableIndexDto getTableIndexByOptionName(String optionName){
        return tableIndexMapper.toDto(tableIndexRepository.findByOptionName(optionName).orElse(new TableIndexEntity()));
    }
}
