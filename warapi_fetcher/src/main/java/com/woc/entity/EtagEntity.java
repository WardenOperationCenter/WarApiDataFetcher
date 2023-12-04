package com.woc.entity;

import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bson.codecs.pojo.annotations.BsonId;

/**
 * Represent a MongoDB Document with every static data about a map
 */
@MongoEntity(collection = "etags")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class EtagEntity extends PanacheMongoEntityBase {

    @BsonId
    private FoxholeMapEntity.Id id;
    private String itemsEtag ;
    private String reportEtag ;

}
