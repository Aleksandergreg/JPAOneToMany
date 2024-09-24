package com.example.jpaonetomany.controller;

import com.example.jpaonetomany.model.Region;
import com.example.jpaonetomany.repository.RegionRepository;
import com.example.jpaonetomany.service.ApiServiceGetRegioner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
public class RegionRestController {

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private ApiServiceGetRegioner apiServiceGetRegioner;


    @GetMapping("/regions")
    public List<Region> getRegioner(){
        List<Region> lstRegioner = apiServiceGetRegioner.getRegioner();
        return lstRegioner;
    }

    @GetMapping("/region/{kode}")
    public ResponseEntity<Region> getRegionByKode(@PathVariable String kode) {
        Optional<Region> regionOptional = regionRepository.findById(kode);
        return regionOptional.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/region")
    @ResponseStatus(HttpStatus.CREATED)
    public Region addRegion(@RequestBody Region region) {
        return regionRepository.save(region);
    }

    @PutMapping("/region/{kode}")
    public ResponseEntity<Region> updateRegion(@PathVariable String kode, @RequestBody Region region) {
        if (!regionRepository.existsById(kode)) {
            return ResponseEntity.notFound().build();
        }
        region.setKode(kode);
        Region updatedRegion = regionRepository.save(region);
        return ResponseEntity.ok(updatedRegion);
    }

    // Delete a region
   /* @DeleteMapping("/region/{kode}")
    public ResponseEntity<String> deleteRegion(@PathVariable String kode) {
        if (!regionRepository.existsById(kode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{message: 'Region not found'}");
        }
        regionRepository.deleteById(kode);
        return ResponseEntity.ok("{message: 'Region deleted successfully'}");
    }*/
    @DeleteMapping("/region/{kode}")
    public ResponseEntity<String> deleteRegion(@PathVariable String kode) {
        if (!regionRepository.existsById(kode)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Region not found\"}");
        }
        regionRepository.deleteById(kode);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"message\": \"Region deleted successfully\"}");
    }


}
