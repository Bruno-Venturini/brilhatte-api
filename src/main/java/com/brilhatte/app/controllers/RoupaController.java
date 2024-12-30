package com.brilhatte.app.controllers;

import com.brilhatte.app.dtos.PedraVinculadaDTO;
import com.brilhatte.app.dtos.RoupaDTO;
import com.brilhatte.app.models.Regra;
import com.brilhatte.app.models.Roupa;
import com.brilhatte.app.services.RegraService;
import com.brilhatte.app.services.RoupaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roupas")
public class RoupaController {

    @Autowired
    private RoupaService roupaService;

    @Autowired
    private RegraService regraService;

    @GetMapping
    public ResponseEntity<Page<RoupaDTO>> findAll(@RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size,
                                                  @RequestParam(defaultValue = "") String nome) {
        Page<Roupa> roupas = roupaService.findAllByRoupaIdNome(PageRequest.of(page, size), nome);
        Page<RoupaDTO> roupasDTO = RoupaDTO.fromEntity(roupas);
        roupasDTO.forEach(dto -> {
            List<Regra> regras = regraService.findAllByRoupaId(dto.getId());
            dto.setPedrasVinculadas(PedraVinculadaDTO.fromRegras(regras));
        });

        return ResponseEntity.ok(roupasDTO);
    }

    @PostMapping
    public ResponseEntity<RoupaDTO> save(@RequestBody RoupaDTO roupaDTO) {
        Roupa roupa = roupaService.save(RoupaDTO.toEntity(roupaDTO));
        return ResponseEntity.ok(RoupaDTO.fromEntity(roupa));
    }
}
