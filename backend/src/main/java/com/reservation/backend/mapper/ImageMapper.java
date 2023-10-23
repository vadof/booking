package com.reservation.backend.mapper;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends EntityMapper<Image, ImageDTO> {

    @Override
    @Mapping(target = "imageSrc", expression = "java(getImageSrc(entity))")
    ImageDTO toDto(Image entity);

    default String getImageSrc(Image entity) {
        if (entity != null) {
            return "http://localhost:8080/api/v1/images/" + entity.getId();
        }
        return null;
    }
}
