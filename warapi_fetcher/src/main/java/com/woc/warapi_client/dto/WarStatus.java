package com.woc.warapi_client.dto;

public record WarStatus(String warId, int warNumber, String winner, long conquestStartTime, Long conquestEndTime,
                        Long resistanceStartTime, int requiredVictoryTowns) {
} 