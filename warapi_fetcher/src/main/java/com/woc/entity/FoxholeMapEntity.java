package com.woc.entity;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.MapTextItem;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.AllArgsConstructor;
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
public class FoxholeMapEntity extends PanacheMongoEntityBase {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Id {
        private String warId;
        private int regionId;
    }

    private Id id;
    private String name;
    private List<MapTextItem> textItems;

    public static List<FoxholeMapEntity> listAllByWarID(String warId) {
        return list("_id.warId", warId);
    }

    public static FoxholeMapEntity from(String name, String currentWarId, MapData data) {
        return new FoxholeMapEntity()
                .setName(name)
                .setId(new Id(currentWarId, data.regionId()))
                .setTextItems(data.mapTextItems());
    }
}
