package com.woc.entity;

import com.woc.warapi_client.dto.WarMapReport;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import java.time.Instant;

/**
 * MongoDB Document Representing report about a given map
 */
@MongoEntity(collection = "mapWarReports")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class MapWarReportEntity extends PanacheMongoEntityBase {

    @BsonId
    private MapDynamicDataId id;

    private long totalEnlistments;
    private long colonialCasualties;
    private long wardenCasualties;
    private int dayOfWar;
    private Instant requestDate;

    public static MapWarReportEntity from(WarMapReport report, FoxholeMapEntity.Id mapId) {
        return new MapWarReportEntity()
                .setId(new MapDynamicDataId(mapId, report.version()))
                .setTotalEnlistments(report.totalEnlistments())
                .setColonialCasualties(report.colonialCasualties())
                .setWardenCasualties(report.wardenCasualties())
                .setDayOfWar(report.dayOfWar())
                .setRequestDate(Instant.now());
    }
} 