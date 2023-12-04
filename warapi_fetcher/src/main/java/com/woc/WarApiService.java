package com.woc;

import com.woc.entity.EtagEntity;
import com.woc.entity.ShardConfEntity;
import com.woc.entity.FoxholeMapEntity;
import com.woc.entity.MapItemsReportEntity;
import com.woc.entity.MapWarReportEntity;
import com.woc.entity.WarStatusEntity;
import com.woc.warapi_client.WarApiClient;
import com.woc.warapi_client.dto.MapData;
import com.woc.warapi_client.dto.WarMapReport;
import com.woc.warapi_client.dto.WarStatus;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.jboss.resteasy.reactive.RestResponse;

import java.time.Instant;
import java.util.Optional;
import java.util.Set;


/**
 * Service allowing retrieval and storage of data from the War API
 */
@ApplicationScoped
public class WarApiService {
    public void fetchAndStoreStaticData(ShardConfEntity shardConf) {
        Log.info("Starting fetching of static data of the war.");

        WarApiClient warApiClient = WarApiClient.instantiate(shardConf.getUrl());
        WarStatus warStatus = warApiClient.warStatus();
        Optional<WarStatusEntity> existingWarEntry = WarStatusEntity.findByIdOptional(warStatus.warId());
        if (existingWarEntry.isPresent()) {
            Log.warn(String.format("Some data about war %s data are already present. Stopping here...", warStatus.warId()));
            return;
        }

        WarStatusEntity warStatusEntity = WarStatusEntity.from(shardConf.getShard(), Instant.now(), warStatus);
        warStatusEntity.persist();


        Set<String> mapNames = warApiClient.mapNamesList();
        Log.info(String.format("%d maps names have been fetched.", mapNames.size()));

        for (String mapName : mapNames) {
            processMapData(warApiClient, mapName, warStatusEntity.getWarId());
        }

        Log.info("Static processing ended with success.");
    }

    private void processMapData(WarApiClient warApiClient, String mapName, String currentWarId) {
        Log.info(String.format("\tProcessing map %s.", mapName));
        MapData mapData = warApiClient.mapStaticData(mapName);
        FoxholeMapEntity.from(mapName, currentWarId, mapData).persist();
    }

    public void fetchAndStoreDynamicData(ShardConfEntity shardConf, boolean fetchStatic) {
        WarApiClient warApiClient = WarApiClient.instantiate(shardConf.getUrl());

        WarStatus warStatus = warApiClient.warStatus();
        if (WarStatusEntity.findByIdOptional(warStatus.warId()).isEmpty()) {
            if(fetchStatic){
                Log.info(String.format("Missing static data for war %s, fetching first", warStatus.warId()));
                this.fetchAndStoreStaticData(shardConf);
            }
            else {
                Log.error(String.format("Data are not initialized for war %s, you should first run the static download", warStatus.warId()));
                return;
            }
        }

        WarStatusEntity warStatusEntity = WarStatusEntity.from(shardConf.getShard(), Instant.now(), warStatus);
        warStatusEntity.persistOrUpdate();
        Log.info(String.format("Updating data of war %s", warStatusEntity.getWarId()));

        for (FoxholeMapEntity foxholeMapEntity : FoxholeMapEntity.listAllByWarID(warStatusEntity.getWarId())) {
            this.handleMapDynamicData(warApiClient, foxholeMapEntity);
        }

        Log.info("");
    }

    private void handleMapDynamicData(WarApiClient warApiClient, FoxholeMapEntity foxholeMapEntity) {
        Log.info(String.format("\tProcessing map %s", foxholeMapEntity.getName()));

        EtagEntity existingEtag = (EtagEntity) EtagEntity
                .findByIdOptional(foxholeMapEntity.getId())
                .orElseGet(() -> new EtagEntity().setId(foxholeMapEntity.getId()));

        //Handle war report
        try (RestResponse<WarMapReport> warMapReport = warApiClient.mapWarReport(foxholeMapEntity.getName(), existingEtag.getReportEtag())) {
            if (HttpResponseStatus.NOT_MODIFIED.code() == warMapReport.getStatus()) {
                Log.info("\t\tWar Report Did not changed since last query");
            } else {
                existingEtag.setReportEtag("\"" + warMapReport.getEntity().version() + "\"");
                MapWarReportEntity.from(warMapReport.getEntity(), foxholeMapEntity.getId()).persist();
                Log.info("\t\tPersisting War Report");
            }
        }

        //Handle Dynamic Items
        try (RestResponse<MapData> mapData = warApiClient.mapDynamicData(foxholeMapEntity.getName(), existingEtag.getItemsEtag())) {
            if (HttpResponseStatus.NOT_MODIFIED.code() == mapData.getStatus()) {
                Log.info("\t\tItem Report Did not changed since last query");
            } else {
                existingEtag.setItemsEtag("\"" + mapData.getEntity().version() + "\"");
                MapItemsReportEntity.from(mapData.getEntity(), foxholeMapEntity.getId()).persist();
                Log.info("\t\tPersisting Item Report");
            }
        }

        //Update etags for next query
        existingEtag.persistOrUpdate();
    }

    public void fetchAllServersDynamicData(){
        for (ShardConfEntity shardConf : ShardConfEntity.listAllActive()) {
            Log.info(String.format("Processing server %s", shardConf.getName()));
            this.fetchAndStoreDynamicData(shardConf, false);
            Log.info(String.format("Data for server %s successfully fetched", shardConf.getName()));
        }
    }
}
