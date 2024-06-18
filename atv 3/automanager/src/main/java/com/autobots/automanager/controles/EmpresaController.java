package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/empresas")
public class EmpresaController {

    @Autowired
    private RepositorioEmpresa empresaRepositorio;

    @PostMapping
    public ResponseEntity<EntityModel<Empresa>> criarEmpresa(@RequestBody Empresa empresa) {
        Empresa novaEmpresa = empresaRepositorio.save(empresa);
        return new ResponseEntity<>(adicionarLinks(novaEmpresa), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Empresa>>> obterEmpresas() {
        List<EntityModel<Empresa>> empresas = empresaRepositorio.findAll().stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(empresas, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Empresa>> obterEmpresaPorId(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaRepositorio.findById(id);
        if (empresa.isPresent()) {
            return new ResponseEntity<>(adicionarLinks(empresa.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Empresa>> atualizarEmpresa(@PathVariable Long id, @RequestBody Empresa empresaAtualizada) {
        Optional<Empresa> empresaExistente = empresaRepositorio.findById(id);
        if (empresaExistente.isPresent()) {
            Empresa empresa = empresaExistente.get();
            empresa.setRazaoSocial(empresaAtualizada.getRazaoSocial());
            empresa.setNomeFantasia(empresaAtualizada.getNomeFantasia());
            empresa.setTelefones(empresaAtualizada.getTelefones());
            empresa.setEndereco(empresaAtualizada.getEndereco());
            empresa.setCadastro(empresaAtualizada.getCadastro());
            empresa.setUsuarios(empresaAtualizada.getUsuarios());
            empresa.setMercadorias(empresaAtualizada.getMercadorias());
            empresa.setServicos(empresaAtualizada.getServicos());
            empresa.setVendas(empresaAtualizada.getVendas());
            empresaRepositorio.save(empresa);
            return new ResponseEntity<>(adicionarLinks(empresa), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Long id) {
        Optional<Empresa> empresa = empresaRepositorio.findById(id);
        if (empresa.isPresent()) {
            empresaRepositorio.delete(empresa.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Empresa> adicionarLinks(Empresa empresa) {
        EntityModel<Empresa> resource = EntityModel.of(empresa);
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(EmpresaController.class).obterEmpresaPorId(empresa.getId())).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(EmpresaController.class).obterEmpresas()).withRel("empresas"));
        return resource;
    }
}
