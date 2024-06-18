package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.repositorios.RepositorioServico;

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
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private RepositorioServico servicoRepositorio;

    // Criar novo serviço
    @PostMapping
    public ResponseEntity<EntityModel<Servico>> criarServico(@RequestBody Servico servico) {
        Servico novoServico = servicoRepositorio.save(servico);
        return new ResponseEntity<>(adicionarLinks(novoServico), HttpStatus.CREATED);
    }

    // Obter todos os serviços
    @GetMapping
    public ResponseEntity<List<EntityModel<Servico>>> obterServicos() {
        List<Servico> servicos = servicoRepositorio.findAll();
        List<EntityModel<Servico>> servicosModel = servicos.stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(servicosModel, HttpStatus.OK);
    }

    // Obter serviço por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Servico>> obterServicoPorId(@PathVariable Long id) {
        Optional<Servico> servico = servicoRepositorio.findById(id);
        return servico.map(value -> new ResponseEntity<>(adicionarLinks(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Atualizar serviço por ID
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Servico>> atualizarServico(@PathVariable Long id, @RequestBody Servico servicoAtualizado) {
        Optional<Servico> servicoExistente = servicoRepositorio.findById(id);
        if (servicoExistente.isPresent()) {
            Servico servico = servicoExistente.get();
            servico.setNome(servicoAtualizado.getNome());
            servico.setValor(servicoAtualizado.getValor());
            servico.setDescricao(servicoAtualizado.getDescricao());
            servicoRepositorio.save(servico);
            return new ResponseEntity<>(adicionarLinks(servico), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Excluir serviço por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable Long id) {
        Optional<Servico> servico = servicoRepositorio.findById(id);
        if (servico.isPresent()) {
            servicoRepositorio.delete(servico.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Servico> adicionarLinks(Servico servico) {
        EntityModel<Servico> servicoModel = EntityModel.of(servico);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoController.class).obterServicoPorId(servico.getId())).withSelfRel();
        Link allServicosLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ServicoController.class).obterServicos()).withRel("all-servicos");
        servicoModel.add(selfLink, allServicosLink);
        return servicoModel;
    }
}
