package com.woc.entity;

import com.woc.Faction;
import com.woc.warapi_client.dto.WarStatus;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;

import java.time.Instant;
import java.util.Objects;

/**
 * Represent a MongoDB Document with the data about a war
 */
@MongoEntity(collection = "wars")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class WarStatusEntity extends PanacheMongoEntityBase {

    @BsonId
    private String warId;
    private short shard;
    private int warNumber;
    private Faction winner;
    private Instant conquestStartTime;
    private Instant conquestEndTime;
    private Instant resistanceStartTime;
    private int requiredVictoryTowns;
    private Instant requestTime;


    public static WarStatusEntity from(short shard, Instant requestTime, WarStatus status) {
        return new WarStatusEntity()
                .setWarId(status.warId())
                .setShard(shard)
                .setWarNumber(status.warNumber())
                .setWinner(Faction.valueOf(status.winner()))
                .setConquestStartTime(Instant.ofEpochMilli(status.conquestStartTime()))
                .setConquestEndTime(Objects.isNull(status.conquestEndTime()) ? null : Instant.ofEpochMilli(status.conquestEndTime()))
                .setResistanceStartTime(Objects.isNull(status.resistanceStartTime()) ? null : Instant.ofEpochMilli(status.resistanceStartTime()))
                .setRequiredVictoryTowns(status.requiredVictoryTowns())
                .setRequestTime(requestTime);
    }
}
