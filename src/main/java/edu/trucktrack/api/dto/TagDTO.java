package edu.trucktrack.api.dto;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class TagDTO {

    private Long id;

    private Integer expenseId;

    private String name;

    private Boolean isSystem;

    private Integer createdByEmployeeId;

    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TagDTO tagDTO = (TagDTO) o;

        return new EqualsBuilder().append(id, tagDTO.id).append(name, tagDTO.name).append(createdAt, tagDTO.createdAt).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).append(createdAt).toHashCode();
    }
}
