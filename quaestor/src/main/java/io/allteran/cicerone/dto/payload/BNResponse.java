package io.allteran.cicerone.dto.payload;

import io.allteran.cicerone.dto.TaxElement;
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
