package io.allteran.cicerone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(of = {"brutto", "pit2", "contractType", "period"})
public class BNRequest {
    private double brutto;
    private ContractTypeDTO contractType;
    private BNCalcPeriodDTO period;
    private boolean pit2;
}
