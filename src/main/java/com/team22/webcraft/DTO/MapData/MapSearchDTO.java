package com.team22.webcraft.DTO.MapData;

import com.team22.webcraft.Domain.AccessType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MapSearchDTO {
    @NotNull(message = "accessType cannot be null")
    private AccessType accessType;
}
