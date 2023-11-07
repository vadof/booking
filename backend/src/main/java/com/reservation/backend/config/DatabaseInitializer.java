package com.reservation.backend.config;

import com.reservation.backend.entities.Location;
import com.reservation.backend.exceptions.UserRegisterException;
import com.reservation.backend.repositories.HousingRepository;
import com.reservation.backend.repositories.LocationRepository;
import com.reservation.backend.repositories.UserRepository;
import com.reservation.backend.requests.RegisterRequest;
import com.reservation.backend.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DatabaseInitializer {

    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    private final HousingRepository housingRepository;
    private final AuthenticationService authenticationService;

    @Bean
    public void addAllLocationsToDatabase() {
        if (locationRepository.findAll().isEmpty()) {
            locationRepository.save(new Location("Tallinn"));
            locationRepository.save(new Location("Tartu"));
            locationRepository.save(new Location("Pärnu"));
            locationRepository.save(new Location("Narva"));
            locationRepository.save(new Location("Kohtla-Järve"));
            locationRepository.save(new Location("Viljandi"));
            locationRepository.save(new Location("Maardu"));
            locationRepository.save(new Location("Rakvere"));
            locationRepository.save(new Location("Kuressaare"));
            locationRepository.save(new Location("Sillamäe"));
            locationRepository.save(new Location("Valga"));
            locationRepository.save(new Location("Võru"));
            locationRepository.save(new Location("Jõhvi"));
            locationRepository.save(new Location("Keila"));
            locationRepository.save(new Location("Haapsalu"));
            locationRepository.save(new Location("Paide"));
            locationRepository.save(new Location("Saue"));
            locationRepository.save(new Location("Elva"));
            locationRepository.save(new Location("Tapa"));
            locationRepository.save(new Location("Põlva"));
            locationRepository.save(new Location("Türi"));
            locationRepository.save(new Location("Rapla"));
            locationRepository.save(new Location("Jõgeva"));
            locationRepository.save(new Location("Kiviõli"));
            locationRepository.save(new Location("Põltsamaa"));
            locationRepository.save(new Location("Sindi"));
            locationRepository.save(new Location("Paldiski"));
            locationRepository.save(new Location("Kärdla"));
            locationRepository.save(new Location("Kunda"));
            locationRepository.save(new Location("Tõrva"));
            locationRepository.save(new Location("Narva-Jõesuu"));
            locationRepository.save(new Location("Kehra"));
            locationRepository.save(new Location("Loksa"));
            locationRepository.save(new Location("Otepää"));
            locationRepository.save(new Location("Räpina"));
            locationRepository.save(new Location("Tamsalu"));
            locationRepository.save(new Location("Kilingi-Nõmme"));
            locationRepository.save(new Location("Karksi-Nuia"));
            locationRepository.save(new Location("Võhma"));
            locationRepository.save(new Location("Antsla"));
            locationRepository.save(new Location("Lihula"));
            locationRepository.save(new Location("Mustvee"));
            locationRepository.save(new Location("Suure-Jaani"));
            locationRepository.save(new Location("Abja-Paluoja"));
            locationRepository.save(new Location("Püssi"));
            locationRepository.save(new Location("Mõisaküla"));
            locationRepository.save(new Location("Kallaste"));
        }
    }

    @Bean
    public void initializeUser() throws UserRegisterException {
        if (userRepository.findById(1L).isEmpty()) {
            RegisterRequest registerRequest = new RegisterRequest();
            registerRequest.setFirstname("test");
            registerRequest.setLastname("test");
            registerRequest.setEmail("test@gmail.com");
            registerRequest.setPassword("test1234");
            this.authenticationService.register(registerRequest);
        }
    }
}
