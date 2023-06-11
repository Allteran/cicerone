package io.allteran.cicerone.util;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.allteran.cicerone.dto.BNCalcPeriodDTO;
import io.allteran.cicerone.dto.ContractTypeDTO;
import io.allteran.cicerone.entity.BNCalcPeriod;
import io.allteran.cicerone.entity.ContractType;

public class EntityConverter {
    public static BNCalcPeriodDTO convertToDTO(BNCalcPeriod entity) {
        return new BNCalcPeriodDTO(entity.getId(), entity.getName()
        );
    }

    public static BNCalcPeriod convertToEntity(BNCalcPeriodDTO dto) {
        return new BNCalcPeriod(dto.getId(), dto.getName());
    }

    public static ContractTypeDTO convertToDTO(ContractType entity) {
        return new ContractTypeDTO(entity.getId(), entity.getName());
    }

    public static ContractType convertToEntity(ContractTypeDTO dto) {
        return new ContractType(dto.getId(), dto.getName());
    }
}
