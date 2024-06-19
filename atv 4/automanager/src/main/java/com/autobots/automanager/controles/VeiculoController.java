package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.repositorios.RepositorioVeiculo;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private RepositorioVeiculo veiculoRepositorio;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PostMapping
    public ResponseEntity<EntityModel<Veiculo>> criarVeiculo(@RequestBody Veiculo veiculo) {
        Veiculo novoVeiculo = veiculoRepositorio.save(veiculo);
        return new ResponseEntity<>(adicionarLinks(novoVeiculo), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping
    public ResponseEntity<List<EntityModel<Veiculo>>> obterVeiculos() {
        List<EntityModel<Veiculo>> veiculos = veiculoRepositorio.findAll().stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(veiculos, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Veiculo>> obterVeiculoPorId(@PathVariable Long id) {
        Optional<Veiculo> veiculo = veiculoRepositorio.findById(id);
        if (veiculo.isPresent()) {
            return new ResponseEntity<>(adicionarLinks(veiculo.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Veiculo>> atualizarVeiculo(@PathVariable Long id, @RequestBody Veiculo veiculoAtualizado) {
        Optional<Veiculo> veiculoExistente = veiculoRepositorio.findById(id);
        if (veiculoExistente.isPresent()) {
            Veiculo veiculo = veiculoExistente.get();
            veiculo.setTipo(veiculoAtualizado.getTipo());
            veiculo.setModelo(veiculoAtualizado.getModelo());
            veiculo.setPlaca(veiculoAtualizado.getPlaca());
            veiculo.setProprietario(veiculoAtualizado.getProprietario());
            veiculo.setVendas(veiculoAtualizado.getVendas());
            veiculoRepositorio.save(veiculo);
            return new ResponseEntity<>(adicionarLinks(veiculo), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVeiculo(@PathVariable Long id) {
        Optional<Veiculo> veiculo = veiculoRepositorio.findById(id);
        if (veiculo.isPresent()) {
            veiculoRepositorio.delete(veiculo.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Veiculo> adicionarLinks(Veiculo veiculo) {
        EntityModel<Veiculo> resource = EntityModel.of(veiculo);
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(VeiculoController.class).obterVeiculoPorId(veiculo.getId())).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(VeiculoController.class).obterVeiculos()).withRel("veiculos"));
        return resource;
    }
}
