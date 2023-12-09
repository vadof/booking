package com.reservation.backend.mocks.dto;

import com.reservation.backend.dto.ImageDTO;

public class ImageDtoMock {

    public static ImageDTO getImageDtoMock(Long id) {
        return new ImageDTO(id, "src/" + id);
    }

}
