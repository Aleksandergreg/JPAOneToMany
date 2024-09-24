package com.example.jpaonetomany.repository;

import com.example.jpaonetomany.model.Kommune;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KommuneRepository extends JpaRepository<Kommune, String> {
List <Kommune> findByNavn (String string);
}
