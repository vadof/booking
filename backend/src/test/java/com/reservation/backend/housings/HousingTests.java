package com.reservation.backend.housings;
import com.reservation.backend.GenericTest;
import com.reservation.backend.dto.HousingDTO;
import com.reservation.backend.dto.HousingPreviewDTO;
import com.reservation.backend.dto.PaginatedResponseDTO;
import com.reservation.backend.dto.PriceDto;
import com.reservation.backend.dto.search.HousingSearchDTO;
import com.reservation.backend.entities.Booking;
import com.reservation.backend.entities.Housing;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.User;
import com.reservation.backend.mapper.*;
import com.reservation.backend.mocks.dto.HousingDtoMock;
import com.reservation.backend.mocks.dto.HousingPreviewDtoMock;
import com.reservation.backend.mocks.entity.BookingMock;
import com.reservation.backend.mocks.entity.HousingMock;
import com.reservation.backend.mocks.entity.ImageMock;
import com.reservation.backend.mocks.entity.UserMock;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.services.HousingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HousingTests extends GenericTest {
    @Spy
    HousingPreviewMapper housingPreviewMapper = new HousingPreviewMapperImpl();
    @Spy
    HousingMapper housingMapper = new HousingMapperImpl();
    @Mock
    HousingRepository housingRepository;
    @Mock
    ImageRepository imageRepository;
    @Spy
    ImageMapper imageMapper = new ImageMapperImpl();
    @Mock
    UserRepository userRepository;
    @Captor
    private ArgumentCaptor<Housing> housingArgumentCaptor;
    @InjectMocks
    HousingService housingService;


    @BeforeEach
    public void iniMapperDependencies() {
        LocationMapper locationMapper = new LocationMapperImpl();
        ReviewMapper reviewMapper = new ReviewMapperImpl();
        UserMapper userMapper = new UserMapperImpl();
        ImageMapper imageMapper = new ImageMapperImpl();
        BookingMapper bookingMapper = new BookingMapperImpl();

        ReflectionTestUtils.setField(housingMapper, "userMapper", userMapper);
        ReflectionTestUtils.setField(housingMapper, "imageMapper", imageMapper);
        ReflectionTestUtils.setField(housingMapper, "locationMapper", locationMapper);
        ReflectionTestUtils.setField(housingMapper, "reviewMapper", reviewMapper);
        ReflectionTestUtils.setField(housingMapper, "bookingMapper", bookingMapper);
        ReflectionTestUtils.setField(housingPreviewMapper, "imageMapper", imageMapper);
        ReflectionTestUtils.setField(housingPreviewMapper, "locationMapper", locationMapper);
    }


    @Test
    void addHousing() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);
        HousingDTO housingDTO1 = HousingDtoMock.getHousingDtoMock(1L);

        HousingDTO savedHousing = housingService.addHousing(housingDTO1);

        verify(housingRepository).save(housingArgumentCaptor.capture());
        Housing savedEntity = housingArgumentCaptor.getValue();

        assertThat(savedEntity.getName()).isEqualTo(housingDTO1.getName());
        assertThat(savedEntity.getPeople()).isEqualTo(housingDTO1.getPeople());
        assertThat(savedEntity.getCoordinates()).isEqualTo(housingDTO1.getCoordinates());
        assertThat(savedEntity.getPublished()).isEqualTo(housingDTO1.getPublished());
        assertThat(savedEntity.getOwner()).isEqualTo(user);
        assertThat(imageMapper.toDto(savedEntity.getPreviewImage())).isEqualTo(housingDTO1.getPreviewImage());
        assertThat(savedEntity.getLocation().getName()).isEqualTo(housingDTO1.getLocation().getName());

        assertThat(savedHousing.getPublished()).isFalse();
        Mockito.verify(housingMapper, times(1)).toEntity(housingDTO1);
        Mockito.verify(housingMapper, times(1)).toDto(savedEntity);

        Mockito.verify(housingRepository, times(1)).save(any());
    }

    @Test
    void getAllHousings() {
        HousingSearchDTO housingSearchDTO = new HousingSearchDTO();
        housingSearchDTO.setLocation("Tallinn");
        housingSearchDTO.setMaxPrice(new BigDecimal("1000"));
        housingSearchDTO.setMinPrice(new BigDecimal("500"));
        Page<Housing> page = new PageImpl<>(new ArrayList<>());

        when(housingRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(page);
        when(housingPreviewMapper.toDtos(page.getContent())).thenReturn(new ArrayList<>());

        PaginatedResponseDTO<HousingPreviewDTO> response = housingService.getAllHousings(housingSearchDTO);

        assertEquals(0, response.getData().size());
        verify(housingPreviewMapper, times(1)).toDtos(page.getContent());

    }

    @Test
    void updateHousing() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing existing = HousingMock.getHousingMock(1L);
        when(housingRepository.findById(existing.getId())).thenReturn(Optional.of(existing));

        HousingDTO housingDTO1 = HousingDtoMock.getHousingDtoMock(1L);
        housingDTO1.setDescription("Updated");

        HousingDTO savedHousing = housingService.updateHousing(housingDTO1);
        System.out.println(savedHousing.getDescription());
        assertThat(savedHousing.getDescription())
                .isEqualTo(housingDTO1.getDescription());
        verify(housingRepository).saveAndFlush(housingArgumentCaptor.capture());
        Housing savedEntity = housingArgumentCaptor.getValue();

        assertThat(savedEntity.getName()).isEqualTo(housingDTO1.getName());
        assertThat(savedEntity.getPeople()).isEqualTo(housingDTO1.getPeople());
        assertThat(savedEntity.getCoordinates()).isEqualTo(housingDTO1.getCoordinates());
        assertThat(savedEntity.getPublished()).isEqualTo(housingDTO1.getPublished());
        assertThat(savedEntity.getOwner()).isEqualTo(user);
        assertThat(imageMapper.toDto(savedEntity.getPreviewImage())).isEqualTo(housingDTO1.getPreviewImage());
        assertThat(savedEntity.getLocation().getName()).isEqualTo(housingDTO1.getLocation().getName());

        Mockito.verify(housingMapper, times(1)).toDto(savedEntity);
        Mockito.verify(housingRepository, times(1)).saveAndFlush(any());
    }

    @Test
    void getHousingById() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing existing = HousingMock.getHousingMock(1L);
        when(housingRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        HousingDTO housingDTO = housingService.getHousingById(existing.getId());
        verify(housingMapper, times(1)).toDto(any());
        assertThat(housingDTO.getName()).isEqualTo(existing.getName());
    }

    @Test
    void getHousingByOwner() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        List<Housing> housings = Arrays.asList(HousingMock.getHousingMock(1L), HousingMock.getHousingMock(2L));
        when(housingRepository.findByOwner(user)).thenReturn(housings);

        List<HousingPreviewDTO> housingPreviewDTOs = housingService.getHousingsByOwner();

        verify(housingPreviewMapper, times(1)).toDtos(housings);
        assertEquals(housingPreviewDTOs.size(), housings.size());
        for (int i = 0; i < housings.size(); i++) {
            assertThat(housingPreviewDTOs.get(i).getName()).isEqualTo(housings.get(i).getName());
        }
    }

    @Test
    void deleteHousing() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing existing = HousingMock.getHousingMock(1L);
        when(housingRepository.findById(existing.getId())).thenReturn(Optional.of(existing));
        HousingDTO housingDTO = housingService.deleteHousing(existing.getId());
        verify(housingMapper, times(1)).toDto(any());
        assertThat(housingDTO.getName()).isEqualTo(existing.getName());
    }

    @Test
    void getHousingPrices() {
        PriceDto priceDto1 = new PriceDto();
        priceDto1.setMin(new BigDecimal(1));
        priceDto1.setMax(new BigDecimal(2));
        when(housingRepository.getPrices()).thenReturn(priceDto1);
        PriceDto priceDto = housingService.getHousingPrices();
        assertThat(priceDto.getMin()).isEqualTo(priceDto1.getMin());
    }

    @Test
    void addHousingToFavourites() {
        Long housingId = 1L;
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing housing = HousingMock.getHousingMock(housingId);
        HousingDTO housingDTO = new HousingDTO();

//        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(housingRepository.findById(housingId)).thenReturn(Optional.of(housing));
        when(housingMapper.toDto(housing)).thenReturn(housingDTO);

        HousingDTO result = housingService.addHousingToFavourites(housingId);

        verify(userRepository, times(1)).save(user);
        verify(housingMapper, times(1)).toDto(housing);
        assertThat(result).isEqualTo(housingDTO);
        assertTrue(user.getFavourites().contains(housing));
    }

    @Test
    void getAllFavourites() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        List<Housing> housings = Arrays.asList(HousingMock.getHousingMock(1L), HousingMock.getHousingMock(2L));
        List<HousingDTO> housingDTO = Arrays.asList(HousingDtoMock.getHousingDtoMock(1L), HousingDtoMock.getHousingDtoMock(2L));

//        when(housingPreviewMapper.toDtos(housings)).thenReturn(housingPreviewMapper.toDtos(user.getFavourites()));
        List<HousingPreviewDTO> housingPreviewDTOS = housingService.getAllFavourites();
        verify(housingPreviewMapper, times(1)).toDtos(any());
        assertThat(housingPreviewDTOS).isNotNull();

    }

    @Test
    void deleteFromFavourites() {
        Long housingId = 1L;
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing housing = HousingMock.getHousingMock(housingId);
        HousingDTO housingDTO = new HousingDTO();

        when(housingRepository.findById(housingId)).thenReturn(Optional.of(housing));
        when(housingMapper.toDto(housing)).thenReturn(housingDTO);

        housingService.addHousingToFavourites(housingId);
        HousingDTO result = housingService.deleteFromFavourites(housingId);


        verify(userRepository, times(2)).save(user);
        verify(housingMapper, times(2)).toDto(housing);
        assertThat(result).isEqualTo(housingDTO);
        assertFalse(user.getFavourites().contains(housing));
    }

    @Test
    void publishHousing() {
        User user = UserMock.getUserMock(1L);
        mockAuthenticatedUser(user);

        Housing housing = HousingMock.getHousingMock(1L);
        housing.setOwner(user);
        HousingPreviewDTO housingPreviewDTO = HousingPreviewDtoMock.getHousingPreviewDtoMock(1L);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));

        when(housingService.getCurrentUserAsEntity()).thenReturn(user);
        when(housingPreviewMapper.toDto(housing)).thenReturn(housingPreviewDTO);

        HousingPreviewDTO result = housingService.publishHousing(housing.getId(), false);

        verify(housingRepository).save(housing);
        assertFalse(housing.getPublished());
        assertEquals(housingPreviewDTO, result);
    }

    @Test
    void changePreviewImageSuccess() {
        User owner = UserMock.getUserMock(1L);
        mockAuthenticatedUser(owner);

        Housing housing = HousingMock.getHousingMock(1L);
        Image image = ImageMock.getImageMock(1L);

        when(imageRepository.findById(image.getId())).thenReturn(Optional.of(image));
        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));

        housingService.changeImagePreview(housing.getId(), image.getId());

        verify(housingRepository, times(1)).save(housing);
    }

    @Test
    void getBookedDaysSuccess() {
        Housing housing = HousingMock.getHousingMock(1L);
        Booking booking = BookingMock.getBookingMock(1L);
        housing.getBookings().add(booking);

        when(housingRepository.findById(housing.getId())).thenReturn(Optional.of(housing));

        List<LocalDate> dates = housingService.getHousingBookedDays(housing.getId());

        assertEquals(1, dates.size());
        assertThat(dates.get(0)).isEqualTo(booking.getCheckInDate());
    }


}
