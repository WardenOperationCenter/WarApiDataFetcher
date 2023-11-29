package com.woc;

import com.woc.entity.FoxholeMapEntity;
import com.woc.warapi_client.WarApiClient;
import com.woc.warapi_client.dto.MapData;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Set;

/**
 * Service allowing retrieval and storage of data from the War API
 */
@ApplicationScoped
public class WarApiService {

    @Inject
    WarApiClient warApiClient;

    public void fetchAndStoreStaticData(boolean dryRun) {
        Log.info("Starting fetching of static data of the war.");
        long nbExistingMaps = FoxholeMapEntity.count();

        if (nbExistingMaps != 0) {
            Log.warn("Static data are already present. Stopping here...");
            return;
        }

        Set<String> mapNames = warApiClient.mapNamesList();
        Log.info(String.format("%d maps names have been fetched.", mapNames.size()));

        for (String mapName : mapNames) {
            processMapData(mapName, dryRun);
        }

        Log.info("Static processing ended with success.");
    }

    private void processMapData(String mapName, boolean dryRun) {
        Log.info(String.format("\tProcessing map %s.", mapName));
        MapData mapData = warApiClient.mapStaticData(mapName);
        if (!dryRun) {
            FoxholeMapEntity.from(mapName, mapData).persist();
        }
    }
}
