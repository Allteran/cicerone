package io.allteran.cicerone.dto;

import io.allteran.cicerone.entity.calculations.TaxElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BNResponse {
    private double brutto;
    private List<TaxElement> taxes;
    private double netto;
}
