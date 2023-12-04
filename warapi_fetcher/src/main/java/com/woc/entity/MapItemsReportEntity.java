package com.woc.entity;

import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.MapItem;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.panache.common.Sort;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;

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
public class MapItemsReportEntity extends PanacheMongoEntityBase {

    @BsonId
    private MapDynamicDataId id;

    int scorchedVictoryTowns;
    List<MapItem> mapItems;
    Instant lastUpdated;

    public static MapItemsReportEntity from(MapData data, FoxholeMapEntity.Id mapId) {
        return new MapItemsReportEntity()
                .setId(new MapDynamicDataId(mapId, data.version()))
                .setScorchedVictoryTowns(data.scorchedVictoryTowns())
                .setMapItems(data.mapItems())
                .setLastUpdated(Instant.ofEpochMilli(data.lastUpdated()));
    }

    public static String findEtagByMapId(FoxholeMapEntity.Id id) {
        List<MapWarReportEntity> mapData = list("_id.mapId", Sort.by("_id.version").descending(), id);
        //TODO Fix this query, returns nothing
        int latestVersion = mapData.stream().findFirst().map(mapWarReportEntity -> mapWarReportEntity.getId().getVersion()).orElse(-1);
        return String.format(String.format("\"%s\"", latestVersion));
    }
} 