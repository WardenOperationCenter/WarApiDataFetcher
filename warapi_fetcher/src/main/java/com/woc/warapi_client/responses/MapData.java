package com.woc.warapi_client.warapi_client.responses;

import java.util.List;

public record MapData(int regionId, int scorchedVictoryTowns, List<MapItem> mapItems, List<MapItem> mapItemsC, List<MapItem> mapItemsW, List<MapTextItem> mapTextItems, long lastUpdated, int version) {
} 