package io.allteran.cicerone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"name"})
public class BNCalcPeriodDTO {
    private long id;
    private String name;
}
