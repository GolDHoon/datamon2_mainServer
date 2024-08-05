package com.datamon.datamon2.entity;

import com.datamon.datamon2.entity.embedded.CustomerTagMappingEntityId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "TB_CUSTOMER_TAG_MAPPING")
public class CustomerTagMappingEntity {
    @EmbeddedId
    private CustomerTagMappingEntityId id;

    //TODO [Reverse Engineering] generate columns from DB
}