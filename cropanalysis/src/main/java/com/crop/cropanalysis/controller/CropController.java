package com.crop.cropanalysis.controller;

import com.crop.cropanalysis.dto.PythonResponse;
import com.crop.cropanalysis.model.Crop;
import com.crop.cropanalysis.service.CropService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin("*")
public class CropController {

    @Autowired
    private CropService service;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String welcome() {
        return "Crop Analysis API Running";
    }

    @PostMapping("/analyze")
    public Crop analyzeCrop(@RequestParam("image") MultipartFile file) throws Exception {

        // Save image locally
        String uploadDir = System.getProperty("user.dir") + "/uploads/";
       File dir = new File(uploadDir);
       if (!dir.exists()) dir.mkdirs();
       String filePath = uploadDir + file.getOriginalFilename();
       File dest = new File(filePath);
       file.transferTo(dest);

        // Send image to Python AI
        String pythonUrl = "http://localhost:5000/analyze";

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new FileSystemResource(dest));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

        ResponseEntity<PythonResponse> response =
                restTemplate.postForEntity(pythonUrl, requestEntity, PythonResponse.class);

        PythonResponse pythonResult = response.getBody();

        // Save in database
        Crop crop = new Crop();
        crop.setImagePath(filePath);
        crop.setHealth(pythonResult.getHealth());
        crop.setGrade(pythonResult.getGrade());
        crop.setDisease(pythonResult.getDisease());

        Crop savedCrop = service.save(crop);

// Generate QR code
        String qrText = "Crop ID: " + savedCrop.getId()
        + "\nHealth: " + savedCrop.getHealth()
        + "\nGrade: " + savedCrop.getGrade()
        + "\nDisease: " + savedCrop.getDisease();

        String qrPath = com.crop.cropanalysis.util.QRCodeGenerator.generateQRCode(qrText, savedCrop.getId());

       savedCrop.setQrCodePath(qrPath);

       return service.save(savedCrop);

    }
}
