package io.allteran.cicerone.util;

import com.fasterxml.jackson.databind.util.BeanUtil;
import io.allteran.cicerone.dto.BNCalcPeriodDTO;
import io.allteran.cicerone.entity.BNCalcPeriod;

public class EntityConverter {
    public static BNCalcPeriodDTO convertToDTO(BNCalcPeriod entity) {
        return new BNCalcPeriodDTO(entity.getId(), entity.getName()
        );
    }

    public static BNCalcPeriod convertToEntity(BNCalcPeriodDTO dto) {
        return new BNCalcPeriod(dto.getId(), dto.getName());
    }
}
