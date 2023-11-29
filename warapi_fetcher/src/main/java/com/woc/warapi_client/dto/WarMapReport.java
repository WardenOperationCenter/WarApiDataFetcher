package com.woc.warapi_client.dto;

public record WarMapReport(long totalEnlistments, long colonialCasualties, long wardenCasualties, int dayOfWar,
                           int version) {
} 