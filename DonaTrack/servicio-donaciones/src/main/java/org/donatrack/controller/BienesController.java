package org.donatrack.controller;
import java.util.ArrayList;


import org.donatrack.controller.dto.Bienes.*;
import org.donatrack.dominio.bien.Bien;
import org.donatrack.service.BienesService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/bienes")
@CrossOrigin(origins = "*")
public class BienesController {

    private final BienesService bienesService;

    public BienesController(BienesService bienesService) {
        this.bienesService = bienesService;
    }

    
}
