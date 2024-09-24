package com.example.jpaonetomany.service;

import com.example.jpaonetomany.model.Kommune;
import com.example.jpaonetomany.model.Region;
import com.example.jpaonetomany.repository.KommuneRepository;
import com.example.jpaonetomany.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ApiServiceGetKommunerImpl implements ApiServiceGetKommuner {

    private RestTemplate restTemplate;

    @Autowired
    public ApiServiceGetKommunerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private final String kommuneURL = "https://api.dataforsyningen.dk/kommuner";

    @Autowired
    private KommuneRepository kommuneRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Override
    public List<Kommune> getKommuner() {
        ResponseEntity<List<Kommune>> kommuneResponse = restTemplate.exchange(
                kommuneURL, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Kommune>>() {}
        );
        List<Kommune> kommuner = kommuneResponse.getBody();
        saveKommuner(kommuner);
        return kommuner;
    }

    private void saveKommuner(List<Kommune> kommuner) {
        for (Kommune kommune : kommuner) {
            String regionKode = kommune.getRegion().getKode();
            Optional<Region> regionOptional = regionRepository.findById(regionKode);
            if (regionOptional.isPresent()) {
                kommune.setRegion(regionOptional.get());
                kommuneRepository.save(kommune);
            } else {

            }
        }
    }
}
