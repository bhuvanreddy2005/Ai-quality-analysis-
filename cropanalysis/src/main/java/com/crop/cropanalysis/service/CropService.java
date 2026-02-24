package com.crop.cropanalysis.service;

import com.crop.cropanalysis.model.Crop;
import com.crop.cropanalysis.repository.CropRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CropService {

    @Autowired
    private CropRepository repository;

    public Crop save(Crop crop) {
        return repository.save(crop);
    }
}
