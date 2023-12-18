package com.reservation.backend.image;

import com.reservation.backend.GenericTest;
import com.reservation.backend.entities.Image;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.ImageMapper;
import com.reservation.backend.mapper.ImageMapperImpl;
import com.reservation.backend.mocks.entity.ImageMock;
import com.reservation.backend.mocks.entity.UserMock;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.services.ImageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ImageTests extends GenericTest {

    @Mock
    ImageRepository repository;

    @Mock
    HousingRepository housingRepository;

    @Spy
    ImageMapper mapper = new ImageMapperImpl();

    @InjectMocks
    ImageService service;

    @Test
    @DisplayName("Add Image Success")
    void addSuccess() {
        Image image = ImageMock.getImageMock(1L);
        MultipartFile multipartFile = new MockMultipartFile(image.getName(), image.getName(), image.getContentType(), new byte[5]);

        mockAuthenticatedUser(UserMock.getUserMock(1L));
        when(housingRepository.findById(image.getHousing().getId())).thenReturn(Optional.of(image.getHousing()));

        service.addImageToHousing(multipartFile, image.getHousing().getId());

        Mockito.verify(repository, times(1)).save(any());
        Mockito.verify(housingRepository, times(1)).save(any());
        Mockito.verify(mapper, times(1)).toDto(any());
    }

    @Test
    @DisplayName("Add Image Housing Not Found Failure")
    void addFailure1() {
        Image image = ImageMock.getImageMock(1L);
        MultipartFile multipartFile = new MockMultipartFile(image.getName(), image.getName(), image.getContentType(), new byte[5]);

        mockAuthenticatedUser(UserMock.getUserMock(1L));
        when(housingRepository.findById(5L)).thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(AppException.class,
                () -> service.addImageToHousing(multipartFile, 5L));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);

        Mockito.verify(repository, times(0)).save(any());
        Mockito.verify(housingRepository, times(0)).save(any());
        Mockito.verify(mapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Add Image Not Owner")
    void addFailure2() {
        Long housingId = 1L;
        Image image = ImageMock.getImageMock(1L);
        MultipartFile multipartFile = new MockMultipartFile(image.getName(), image.getName(), image.getContentType(), new byte[5]);

        mockAuthenticatedUser(UserMock.getUserMock(2L, "random@gmail.com"));
        when(housingRepository.findById(image.getHousing().getId())).thenReturn(Optional.of(image.getHousing()));

        AppException ex = Assertions.assertThrows(AppException.class,
                () -> service.addImageToHousing(multipartFile, housingId));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.FORBIDDEN);

        Mockito.verify(repository, times(0)).save(any());
        Mockito.verify(housingRepository, times(0)).save(any());
        Mockito.verify(mapper, times(0)).toDto(any());
    }

    @Test
    @DisplayName("Get Image Not Found")
    void getFailure() {
        when(repository.findById(5L)).thenReturn(Optional.empty());

        AppException ex = Assertions.assertThrows(AppException.class,
                () -> service.getImage(5L));

        assertThat(ex.getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
