package io.allteran.cicerone.dto;

import io.allteran.cicerone.entity.TaxPayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaxDto {
    private long id;
    private TaxPayer payer;
    private float percentage;
    private String name;
}
