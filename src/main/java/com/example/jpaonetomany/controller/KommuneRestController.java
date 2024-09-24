package com.example.jpaonetomany.controller;

import com.example.jpaonetomany.model.Kommune;
import com.example.jpaonetomany.model.Region;
import com.example.jpaonetomany.repository.KommuneRepository;
import com.example.jpaonetomany.repository.RegionRepository;
import com.example.jpaonetomany.service.ApiServiceGetKommuner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class KommuneRestController {

    @Autowired
    private KommuneRepository kommuneRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ApiServiceGetKommuner apiServiceGetKommuner;

    // Fetch kommuner from external API and save to repository
    @GetMapping("/getkommuner")
    public List<Kommune> getExternalKommuner() {
        List<Kommune> lstKommuner = apiServiceGetKommuner.getKommuner();
        return lstKommuner;
    }

    // Get all kommuner from repository
    @GetMapping("/kommuner")
    public List<Kommune> getAllKommuner() {
        return kommuneRepository.findAll();
    }

    // Get a kommune by kode
    @GetMapping("/kommune/{kode}")
    public ResponseEntity<Kommune> getKommuneByKode(@PathVariable String kode) {
        Optional<Kommune> kommuneOptional = kommuneRepository.findById(kode);
        return kommuneOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Add a new kommune
    @PostMapping("/kommune")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Kommune> addKommune(@RequestBody Kommune kommune) {
        // Ensure that the region exists
        if (kommune.getRegion() != null) {
            String regionKode = kommune.getRegion().getKode();
            Optional<Region> regionOptional = regionRepository.findById(regionKode);
            if (!regionOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Or return a message indicating the region doesn't exist
            }
            kommune.setRegion(regionOptional.get());
        }
        Kommune savedKommune = kommuneRepository.save(kommune);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedKommune);
    }

    // Update an existing kommune
    @PutMapping("/kommune/{kode}")
    public ResponseEntity<Kommune> updateKommune(@PathVariable String kode, @RequestBody Kommune kommune) {
        if (!kommuneRepository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }
        // Ensure that the region exists
        if (kommune.getRegion() != null) {
            String regionKode = kommune.getRegion().getKode();
            Optional<Region> regionOptional = regionRepository.findById(regionKode);
            if (!regionOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(null); // Or return a message indicating the region doesn't exist
            }
            kommune.setRegion(regionOptional.get());
        }
        kommune.setKode(kode);
        Kommune updatedKommune = kommuneRepository.save(kommune);
        return ResponseEntity.ok(updatedKommune);
    }

    // Delete a kommune
    @DeleteMapping("/kommune/{kode}")
    public ResponseEntity<Void> deleteKommune(@PathVariable String kode) {
        if (!kommuneRepository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }
        kommuneRepository.deleteById(kode);
        return ResponseEntity.noContent().build();
    }
}
