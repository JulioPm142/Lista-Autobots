package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.repositorios.RepositorioVenda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vendas")
public class VendaController {

    @Autowired
    private RepositorioVenda vendaRepository;

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @PostMapping
    public ResponseEntity<EntityModel<Venda>> criarVenda(@RequestBody Venda venda) {
        Venda novaVenda = vendaRepository.save(venda);
        return new ResponseEntity<>(adicionarLinks(novaVenda), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @GetMapping
    public ResponseEntity<List<EntityModel<Venda>>> obterVendas() {
        List<Venda> vendas = vendaRepository.findAll();
        List<EntityModel<Venda>> vendasModel = vendas.stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(vendasModel, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE','VENDEDOR')")
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> obterVendaPorId(@PathVariable Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        return venda.map(value -> new ResponseEntity<>(adicionarLinks(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Venda>> atualizarVenda(@PathVariable Long id, @RequestBody Venda vendaAtualizada) {
        Optional<Venda> vendaExistente = vendaRepository.findById(id);
        if (vendaExistente.isPresent()) {
            Venda venda = vendaExistente.get();
            venda.setCadastro(vendaAtualizada.getCadastro());
            venda.setIdentificacao(vendaAtualizada.getIdentificacao());
            venda.setCliente(vendaAtualizada.getCliente());
            venda.setFuncionario(vendaAtualizada.getFuncionario());
            venda.setMercadorias(vendaAtualizada.getMercadorias());
            venda.setServicos(vendaAtualizada.getServicos());
            venda.setVeiculo(vendaAtualizada.getVeiculo());
            vendaRepository.save(venda);
            return new ResponseEntity<>(adicionarLinks(venda), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarVenda(@PathVariable Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        if (venda.isPresent()) {
            vendaRepository.delete(venda.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Venda> adicionarLinks(Venda venda) {
        EntityModel<Venda> vendaModel = EntityModel.of(venda);
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaController.class).obterVendaPorId(venda.getId())).withSelfRel();
        Link allVendasLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VendaController.class).obterVendas()).withRel("all-vendas");
        vendaModel.add(selfLink, allVendasLink);
        return vendaModel;
    }
}
