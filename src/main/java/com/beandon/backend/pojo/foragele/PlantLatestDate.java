package com.beandon.backend.pojo.foragele;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class PlantLatestDate {
    private Timestamp latestDate;
}
