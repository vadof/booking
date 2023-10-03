package com.reservation.backend.mapper;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.entities.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper {

    @Mapping(target = "imageSrc", expression = "java(getImageSrc(image))")
    ImageDTO toImageDTO(Image image);
    List<ImageDTO> toImageDTOList(List<Image> imageList);

    default String getImageSrc(Image image) {
        if (image != null) {
            return "http://localhost:8080/api/v1/images/" + image.getId();
        }
        return null;
    }
}
