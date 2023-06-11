package io.allteran.cicerone.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of = {"id", "name"})
public class ContractTypeDTO {
    private long id;
    private String name;
}
