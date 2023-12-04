package com.woc.entity;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.MapItem;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.List;

/**
 * MongoDB Document Representing report about a given map
 */
@MongoEntity(collection = "mapItemsReports")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class MapItemsReportEntity extends PanacheMongoEntity {

    private FoxholeMapEntity.Id mapId;
    private String version;
    int scorchedVictoryTowns;
    List<MapItem> mapItems;
    Instant lastUpdated;

    public static MapItemsReportEntity from(MapData data, FoxholeMapEntity.Id mapId) {
        return new MapItemsReportEntity()
                .setMapId(mapId)
                .setVersion(data.version())
                .setScorchedVictoryTowns(data.scorchedVictoryTowns())
                .setMapItems(data.mapItems())
                .setLastUpdated(Instant.ofEpochMilli(data.lastUpdated()));
    }
} 