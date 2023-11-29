package com.woc.warapi_client.dto;

public record WarStatus(String warId, int warNumber, String winner, long conquestStartTime, long conquestEndTime,
                        long resistanceStartTime, int requiredVictoryTowns) {
} 