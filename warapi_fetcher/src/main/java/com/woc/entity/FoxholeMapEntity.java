package com.woc.entity;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.MapTextItem;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Represent a MongoDB Document with every static data about a map
 */
@MongoEntity(collection = "maps")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class FoxholeMapEntity extends PanacheMongoEntity {
    private String name;
    private int regionId;
    private List<MapTextItem> mapTextItems;
    private long lastUpdated;
    private int version;

    public static FoxholeMapEntity from(String name, MapData data) {
        return new FoxholeMapEntity()
                .setName(name)
                .setRegionId(data.regionId())
                .setMapTextItems(data.mapTextItems())
                .setLastUpdated(data.lastUpdated())
                .setVersion(data.version());
    }
}
