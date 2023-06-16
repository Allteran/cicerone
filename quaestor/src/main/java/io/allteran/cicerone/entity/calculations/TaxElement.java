package io.allteran.cicerone.entity.calculations;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxElement {
    private long taxId;
    private String name;
    private float percentage;
    private double result;
}
