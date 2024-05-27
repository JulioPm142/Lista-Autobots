package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cliente")
public class TelefoneControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/{id}/telefone")
    public ResponseEntity<CollectionModel<EntityModel<Telefone>>> obterTelefones(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            List<EntityModel<Telefone>> telefoneModels = cliente.getTelefones().stream()
                    .map(telefone -> EntityModel.of(telefone,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                    .obterTelefones(id)).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                    .atualizarTelefone(id, telefone.getId(), telefone)).withRel("update"),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                    .deletarTelefone(id, telefone.getId())).withRel("delete")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Telefone>> collectionModel = CollectionModel.of(telefoneModels);
            collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                    .obterTelefones(id)).withSelfRel());

            // Adicionando o link para criar um novo telefone
            collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                    .adicionarTelefone(id, null)).withRel("create"));

            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/telefone/add")
    public ResponseEntity<EntityModel<Cliente>> adicionarTelefone(@PathVariable long id, @RequestBody Telefone novoTelefone) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            cliente.getTelefones().add(novoTelefone);
            repositorio.save(cliente);

            EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                            .adicionarTelefone(id, novoTelefone)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                            .obterTelefones(id)).withRel("telefones"));

            return new ResponseEntity<>(clienteModel, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{clienteId}/telefone/update/{telefoneId}")
    public ResponseEntity<EntityModel<Cliente>> atualizarTelefone(@PathVariable long clienteId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            Telefone telefone = cliente.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);

            if (telefone != null) {
                telefone.setDdd(atualizacaoTelefone.getDdd());
                telefone.setNumero(atualizacaoTelefone.getNumero());
                repositorio.save(cliente);

                EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                .atualizarTelefone(clienteId, telefoneId, atualizacaoTelefone)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                .obterTelefones(clienteId)).withRel("telefones"));

                return new ResponseEntity<>(clienteModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{clienteId}/telefone/delete/{telefoneId}")
    public ResponseEntity<EntityModel<Cliente>> deletarTelefone(@PathVariable long clienteId, @PathVariable long telefoneId) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            Telefone telefone = cliente.getTelefones().stream()
                    .filter(t -> t.getId().equals(telefoneId))
                    .findFirst()
                    .orElse(null);

            if (telefone != null) {
                cliente.getTelefones().remove(telefone);
                repositorio.save(cliente);

                EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                .deletarTelefone(clienteId, telefoneId)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class)
                                .obterTelefones(clienteId)).withRel("telefones"));

                return new ResponseEntity<>(clienteModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
