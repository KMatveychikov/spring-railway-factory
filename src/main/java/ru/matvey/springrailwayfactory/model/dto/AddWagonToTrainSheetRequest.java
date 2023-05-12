package ru.matvey.springrailwayfactory.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddWagonToTrainSheetRequest {
    private Long sheetId;
    private Long wagonId;
}
