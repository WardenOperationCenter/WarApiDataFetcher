package com.woc;

import com.woc.entity.ShardConfEntity;
import com.woc.entity.FoxholeMapEntity;
import com.woc.entity.MapDynamicDataId;
import com.woc.entity.MapItemsReportEntity;
import com.woc.entity.MapWarReportEntity;
import com.woc.entity.WarStatusEntity;
import com.woc.warapi_client.WarApiClient;
import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.WarMapReport;
import com.woc.warapi_client.dto.WarStatus;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;


/**
 * Service allowing retrieval and storage of data from the War API
 */
@ApplicationScoped
public class WarApiService {
    public void fetchAndStoreStaticData(ShardConfEntity shardConf, boolean dryRun) {
        Log.info("Starting fetching of static data of the war.");

        WarApiClient warApiClient = WarApiClient.instantiate(shardConf.getUrl());
        WarStatus warStatus = warApiClient.warStatus();
        Optional<WarStatusEntity> existingWarEntry = WarStatusEntity.findByIdOptional(warStatus.warId());
        if (existingWarEntry.isPresent()) {
            Log.warn(String.format("Some data about war %s data are already present. Stopping here...", warStatus.warId()));
            return;
        }

        WarStatusEntity warStatusEntity = WarStatusEntity.from(shardConf.getShard(), Instant.now(), warStatus);
        if (!dryRun) {
            warStatusEntity.persist();
        }

        Set<String> mapNames = warApiClient.mapNamesList();
        Log.info(String.format("%d maps names have been fetched.", mapNames.size()));

        for (String mapName : mapNames) {
            processMapData(warApiClient, mapName, warStatusEntity.getWarId(), dryRun);
        }

        Log.info("Static processing ended with success.");
    }

    private void processMapData(WarApiClient warApiClient, String mapName, String currentWarId, boolean dryRun) {
        Log.info(String.format("\tProcessing map %s.", mapName));
        MapData mapData = warApiClient.mapStaticData(mapName);
        if (!dryRun) {
            FoxholeMapEntity.from(mapName, currentWarId, mapData).persist();
        }
    }

    public void fetchAndStoreDynamicData(ShardConfEntity shardConf) {
        WarApiClient warApiClient = WarApiClient.instantiate(shardConf.getUrl());

        WarStatus warStatus = warApiClient.warStatus();
        if (WarStatusEntity.findByIdOptional(warStatus.warId()).isEmpty()) {
            Log.error(String.format("Data are not initialized for war %s, you should first run the static download", warStatus.warId()));
            return;
        }

        WarStatusEntity warStatusEntity = WarStatusEntity.from(shardConf.getShard(), Instant.now(), warStatus);
        warStatusEntity.persistOrUpdate();
        Log.info(String.format("Updating data of war %s", warStatusEntity.getWarId()));

        for (FoxholeMapEntity foxholeMapEntity : FoxholeMapEntity.listAllByWarID(warStatusEntity.getWarId())) {
            Log.info(String.format("\tProcessing map %s", foxholeMapEntity.getName()));
            //TODO Handle header to avoid fetching same data
            WarMapReport warMapReport = warApiClient.mapWarReport(foxholeMapEntity.getName());
            if (MapWarReportEntity.findByIdOptional(new MapDynamicDataId(foxholeMapEntity.getId(), warMapReport.version())).isEmpty()) {
                MapWarReportEntity.from(warMapReport, foxholeMapEntity.getId()).persist();
                Log.info("\t\tPersisting War Report");
            } else {
                Log.info("\t\tFetched War Report but data is already present, skipping");
            }

            MapData mapData = warApiClient.mapDynamicData(foxholeMapEntity.getName());
            if (MapItemsReportEntity.findByIdOptional(new MapDynamicDataId(foxholeMapEntity.getId(), mapData.version())).isEmpty()) {
                MapItemsReportEntity.from(mapData, foxholeMapEntity.getId()).persist();
                Log.info("\t\tPersisting Item Report");
            } else {
                Log.info("\t\tFetched Item Report but data is already present, skipping");
            }
        }
    }
}