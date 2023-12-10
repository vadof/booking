package com.reservation.backend.services;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.User;
import com.reservation.backend.exceptions.AppException;
import com.reservation.backend.mapper.ImageMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.services.common.GenericService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService extends GenericService {
    private final ImageRepository imageRepository;
    private final HousingRepository housingRepository;
    private final ImageMapper imageMapper;

    public static int BITE_SIZE = 4 * 1024;

    @Transactional
    public ImageDTO addImageToHousing(MultipartFile imageFile, Long housingId) {
        User user = getCurrentUserAsEntity();
        Housing housing = housingRepository.findById(housingId).orElseThrow(() ->
            new AppException("Housing#" + housingId + " not found", HttpStatus.NOT_FOUND));
        if (user.equals(housing.getOwner()) && imageFile.getContentType().startsWith("image/")) {
            Image image = saveImage(imageFile, housing);
            housing.getImages().add(image);
            housingRepository.save(housing);
            return imageMapper.toDto(image);
        } else {
            throw new AppException("Forbidden", HttpStatus.FORBIDDEN);
        }
    }

    @SneakyThrows
    private Image saveImage(MultipartFile imageFile, Housing housing) {
        Image image = Image.builder()
                .name(imageFile.getOriginalFilename())
                .contentType(imageFile.getContentType())
                .bytes(compressImage(imageFile.getBytes()))
                .housing(housing)
                .build();

        imageRepository.save(image);
        log.info("Image saved to database");
        return image;
    }

    @SneakyThrows
    public byte[] getImage(Long id) {
        Image image = imageRepository.findById(id).orElseThrow(
                () -> new AppException("Image#" + id + " not found", HttpStatus.NOT_FOUND));
        return decompressImage(image.getBytes());
    }

    private byte[] compressImage(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[BITE_SIZE];

        while (!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp, 0, size);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }

    private byte[] decompressImage(byte[] data) throws DataFormatException, IOException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[BITE_SIZE];

        while (!inflater.finished()) {
            int count = inflater.inflate(tmp);
            outputStream.write(tmp, 0, count);
        }

        outputStream.close();
        return outputStream.toByteArray();
    }
}
