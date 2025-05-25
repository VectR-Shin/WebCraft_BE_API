package com.team22.webcraft.DTO.MapData;

import com.team22.webcraft.Domain.AccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapDataDTO {
    private Long id;

    @NotBlank(message = "mapName cannot be empty")
    @Pattern(
            regexp = "^[a-zA-Z0-9]{1,20}$",
            message = "'mapName' must consist of only uppercase and lowercase letters and numbers and must be at least 1 and no more than 20 characters long"
    )
    private String mapName;

    private LocalDateTime createTime;

    private LocalDateTime lastUpdateTime;

    @NotNull(message = "accessType cannot be null")
    private AccessType accessType;
}
