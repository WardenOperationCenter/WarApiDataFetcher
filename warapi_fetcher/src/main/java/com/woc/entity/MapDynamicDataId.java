package com.woc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MapDynamicDataId {

    private FoxholeMapEntity.Id mapId;
    private int version;
}
