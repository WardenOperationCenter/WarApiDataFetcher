package com.woc.entity;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 *  MongoDB Document Representing report about a given map
 */
@MongoEntity(collection = "mapReports")
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class MapReportEntity {
    //TODO: Think about how best store dynamic Map Items
    //private int scorchedVictoryTowns; //Don't know if it's
    //List<MapItem > mapItems,
} 