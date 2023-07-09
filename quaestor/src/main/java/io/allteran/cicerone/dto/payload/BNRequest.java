package io.allteran.cicerone.dto.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString(of = {"brutto", "pit2", "contractType", "period"})
public class BNRequest {
    private double brutto;
    private long contractTypeId;
    private long periodId;
    private boolean pit2;
}
