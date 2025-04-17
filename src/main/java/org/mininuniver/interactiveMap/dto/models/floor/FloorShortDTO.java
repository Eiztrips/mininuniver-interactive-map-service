package org.mininuniver.interactiveMap.dto.models.floor;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FloorShortDTO {
    private final Integer id;
    private final Integer floorNumber;
    private final String name;
}
