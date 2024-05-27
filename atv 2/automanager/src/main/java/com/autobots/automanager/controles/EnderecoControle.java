package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class EnderecoControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/{id}/endereco")
    public ResponseEntity<EntityModel<Endereco>> obterEndereco(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null && cliente.getEndereco() != null) {
            Endereco endereco = cliente.getEndereco();
            EntityModel<Endereco> enderecoModel = EntityModel.of(endereco,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .obterEndereco(id)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .atualizarEndereco(id, endereco.getId(), endereco)).withRel("update"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .deletarEndereco(id, endereco.getId())).withRel("delete"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .adicionarEndereco(id, null)).withRel("create"));
            return new ResponseEntity<>(enderecoModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/endereco/add")
    public ResponseEntity<EntityModel<Cliente>> adicionarEndereco(@PathVariable long id, @RequestBody Endereco novoEndereco) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            cliente.setEndereco(novoEndereco);
            repositorio.save(cliente);

            EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .adicionarEndereco(id, novoEndereco)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .obterEndereco(id)).withRel("endereco"),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .adicionarEndereco(id, null)).withRel("create")); // Adicionando o link para create

            return new ResponseEntity<>(clienteModel, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{clienteId}/endereco/update/{enderecoId}")
    public ResponseEntity<EntityModel<Cliente>> atualizarEndereco(@PathVariable long clienteId, @PathVariable long enderecoId, @RequestBody Endereco atualizacaoEndereco) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null && cliente.getEndereco() != null && cliente.getEndereco().getId().equals(enderecoId)) {
            Endereco endereco = cliente.getEndereco();
            endereco.setEstado(atualizacaoEndereco.getEstado());
            endereco.setCidade(atualizacaoEndereco.getCidade());
            endereco.setBairro(atualizacaoEndereco.getBairro());
            endereco.setRua(atualizacaoEndereco.getRua());
            endereco.setNumero(atualizacaoEndereco.getNumero());
            endereco.setCodigoPostal(atualizacaoEndereco.getCodigoPostal());
            endereco.setInformacoesAdicionais(atualizacaoEndereco.getInformacoesAdicionais());
            repositorio.save(cliente);

            EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .atualizarEndereco(clienteId, enderecoId, atualizacaoEndereco)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .obterEndereco(clienteId)).withRel("endereco"));

            return new ResponseEntity<>(clienteModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{clienteId}/endereco/delete/{enderecoId}")
    public ResponseEntity<EntityModel<Cliente>> deletarEndereco(@PathVariable long clienteId, @PathVariable long enderecoId) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null && cliente.getEndereco() != null && cliente.getEndereco().getId().equals(enderecoId)) {
            cliente.setEndereco(null);
            repositorio.save(cliente);

            EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .deletarEndereco(clienteId, enderecoId)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EnderecoControle.class)
                            .obterEndereco(clienteId)).withRel("endereco"));

            return new ResponseEntity<>(clienteModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
