package com.crop.cropanalysis.repository;

import com.crop.cropanalysis.model.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
