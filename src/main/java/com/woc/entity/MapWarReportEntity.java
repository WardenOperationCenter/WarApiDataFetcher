package com.woc.entity;

import com.woc.warapi_client.dto.WarMapReport;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * MongoDB Document Representing report about a given map
 */
@MongoEntity(collection = "mapWarReports")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class MapWarReportEntity extends PanacheMongoEntity {

    private FoxholeMapEntity.Id mapId;
    private String version;
    private long totalEnlistments;
    private long colonialCasualties;
    private long wardenCasualties;
    private int dayOfWar;
    private Instant requestDate;

    public static MapWarReportEntity from(WarMapReport report, FoxholeMapEntity.Id mapId) {
        return new MapWarReportEntity()
                .setMapId(mapId)
                .setVersion(report.version())
                .setTotalEnlistments(report.totalEnlistments())
                .setColonialCasualties(report.colonialCasualties())
                .setWardenCasualties(report.wardenCasualties())
                .setDayOfWar(report.dayOfWar())
                .setRequestDate(Instant.now());
    }
}