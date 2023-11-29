package com.woc.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Represent a MongoDB Document with the data about a war
 */
@MongoEntity(collection = "wars")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class WarEntity extends PanacheMongoEntity {

}
