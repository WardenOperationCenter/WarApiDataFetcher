package com.woc.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Represent a MongoDB Document with the data about a war
 */
@MongoEntity(collection = "apiConf")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ShardConfEntity extends PanacheMongoEntity {

    private enum ApiStatus {
        ACTIVE,
        INACTIVE
    }

    private short shard;
    private String url;
    private ApiStatus status;
    private String name;

    public static List<ShardConfEntity> listAllActive() {
        return list("status", ApiStatus.ACTIVE);
    }
}
