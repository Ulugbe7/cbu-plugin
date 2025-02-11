package uz.zaytun.cbuplugin.service.mapper.base;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface BaseMapper<ENTITY, DTO> {

    ENTITY toEntity(DTO dto);

    DTO toDto(ENTITY entity);

    @Named("partialUpdate")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void partialUpdate(@MappingTarget ENTITY entity, DTO dto);
}
