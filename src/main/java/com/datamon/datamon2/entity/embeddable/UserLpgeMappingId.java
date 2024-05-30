package com.datamon.datamon2.entity.embeddable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class UserLpgeMappingId implements Serializable {
    private static final long serialVersionUID = 6217593608629631685L;
    @Column(name = "lpge_code", nullable = false, length = 15)
    private String lpgeCode;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserLpgeMappingId entity = (UserLpgeMappingId) o;
        return Objects.equals(this.lpgeCode, entity.lpgeCode) &&
                Objects.equals(this.userId, entity.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lpgeCode, userId);
    }

}