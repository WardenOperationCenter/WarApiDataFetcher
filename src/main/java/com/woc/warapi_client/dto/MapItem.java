package com.woc.warapi_client.dto;

import com.woc.Faction;

public record MapItem(Faction teamId, int iconType, double x, double y, int flags, int viewDirection) {
} 