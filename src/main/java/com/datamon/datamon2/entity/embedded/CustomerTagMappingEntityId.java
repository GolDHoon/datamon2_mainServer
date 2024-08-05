package com.datamon.datamon2.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class CustomerTagMappingEntityId implements Serializable {
    private static final long serialVersionUID = 8345892778178721839L;
    @Column(name = "cust_id", nullable = false, length = 41)
    private String custId;

    @Column(name = "tag_id", nullable = false)
    private Integer tagId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CustomerTagMappingEntityId entity = (CustomerTagMappingEntityId) o;
        return Objects.equals(this.tagId, entity.tagId) &&
                Objects.equals(this.custId, entity.custId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, custId);
    }

}