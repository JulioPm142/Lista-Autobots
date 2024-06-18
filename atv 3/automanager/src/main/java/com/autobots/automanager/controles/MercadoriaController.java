package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mercadorias")
public class MercadoriaController {

    @Autowired
    private RepositorioMercadoria mercadoriaRepositorio;

    @PostMapping
    public ResponseEntity<EntityModel<Mercadoria>> criarMercadoria(@RequestBody Mercadoria mercadoria) {
        Mercadoria novaMercadoria = mercadoriaRepositorio.save(mercadoria);
        return new ResponseEntity<>(adicionarLinks(novaMercadoria), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Mercadoria>>> obterMercadorias() {
        List<Mercadoria> mercadorias = mercadoriaRepositorio.findAll();
        List<EntityModel<Mercadoria>> mercadoriasModel = mercadorias.stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(mercadoriasModel, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> obterMercadoriaPorId(@PathVariable Long id) {
        Optional<Mercadoria> mercadoria = mercadoriaRepositorio.findById(id);
        return mercadoria.map(value -> new ResponseEntity<>(adicionarLinks(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Mercadoria>> atualizarMercadoria(@PathVariable Long id, @RequestBody Mercadoria mercadoriaAtualizada) {
        Optional<Mercadoria> mercadoriaExistente = mercadoriaRepositorio.findById(id);
        if (mercadoriaExistente.isPresent()) {
            Mercadoria mercadoria = mercadoriaExistente.get();
            mercadoria.setValidade(mercadoriaAtualizada.getValidade());
            mercadoria.setFabricao(mercadoriaAtualizada.getFabricao());
            mercadoria.setCadastro(mercadoriaAtualizada.getCadastro());
            mercadoria.setNome(mercadoriaAtualizada.getNome());
            mercadoria.setQuantidade(mercadoriaAtualizada.getQuantidade());
            mercadoria.setValor(mercadoriaAtualizada.getValor());
            mercadoria.setDescricao(mercadoriaAtualizada.getDescricao());
            mercadoriaRepositorio.save(mercadoria);
            return new ResponseEntity<>(adicionarLinks(mercadoria), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMercadoria(@PathVariable Long id) {
        Optional<Mercadoria> mercadoria = mercadoriaRepositorio.findById(id);
        if (mercadoria.isPresent()) {
            mercadoriaRepositorio.delete(mercadoria.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Mercadoria> adicionarLinks(Mercadoria mercadoria) {
        EntityModel<Mercadoria> mercadoriaModel = EntityModel.of(mercadoria);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaController.class).obterMercadoriaPorId(mercadoria.getId())).withSelfRel();
        Link allMercadoriasLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaController.class).obterMercadorias()).withRel("all-mercadorias");
        mercadoriaModel.add(selfLink, allMercadoriasLink);
        return mercadoriaModel;
    }
}
