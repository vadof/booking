package com.reservation.backend.services;

import com.reservation.backend.dto.ImageDTO;
import com.reservation.backend.entities.Housing;
import com.reservation.backend.entities.Image;
import com.reservation.backend.entities.User;
import com.reservation.backend.mapper.ImageMapper;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.ImageRepository;
import com.reservation.backend.security.JwtService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;
    private final HousingRepository housingRepository;
    private final JwtService jwtService;
    private final ImageMapper imageMapper;

    public static int BITE_SIZE = 4 * 1024;

    @Transactional
    public Optional<ImageDTO> addImageToHousing(MultipartFile imageFile, Long housingId, String token) {
        try {
            User user = this.jwtService.getUserFromBearerToken(token).orElseThrow();
            Housing housing = this.housingRepository.findById(housingId).orElseThrow();

            if (user.equals(housing.getOwner())) {
                Image image = saveImage(imageFile).orElseThrow();
                housing.getImages().add(image);
                this.housingRepository.save(housing);
                return Optional.of(imageMapper.toImageDTO(image));
            }
        } catch (Exception ignored) {}
        return Optional.empty();
    }

    private Optional<Image> saveImage(MultipartFile imageFile) {
        try {
            Image image = Image.builder()
                    .name(imageFile.getOriginalFilename())
                    .contentType(imageFile.getContentType())
                    .bytes(compressImage(imageFile.getBytes()))
                    .housing(housingRepository.findAll().get(0))
                    .build();

            this.imageRepository.save(image);
            log.info("Image saved to database");
            return Optional.of(image);
        } catch (Exception e) {
            log.error("Error adding image to database {}", e.getMessage());
            return Optional.empty();
        }
    }

    public byte[] getImage(Long id) {
        try {
            Image image = this.imageRepository.findById(id).orElseThrow();
            return decompressImage(image.getBytes());
        } catch (Exception e) {
            return null;
        }
    }

    private byte[] compressImage(byte[] data) throws IOException {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[BITE_SIZE];

        while(!deflater.finished()) {
            int size = deflater.deflate(tmp);
            outputStream.write(tmp,0, size);
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
