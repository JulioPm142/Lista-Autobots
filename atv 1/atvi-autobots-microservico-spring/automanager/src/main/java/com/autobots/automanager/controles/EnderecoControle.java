package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente")
public class EnderecoControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/{id}/endereco")
    public Endereco obterEndereco(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id);
        if (cliente != null) {
            return cliente.getEndereco();
        }
        return null;
    }

    @PostMapping("/{id}/endereco/add")
    public Cliente adicionarEndereco(@PathVariable long id, @RequestBody Endereco novoEndereco) {
        Cliente cliente = repositorio.findById(id);
        if (cliente != null) {
            cliente.setEndereco(novoEndereco);
            repositorio.save(cliente);
            return cliente;
        }
        return null;
    }

    @PutMapping("/{clienteId}/endereco/update/{enderecoId}")
    public Cliente atualizarEndereco(@PathVariable long clienteId, @PathVariable long enderecoId, @RequestBody Endereco atualizacaoEndereco) {
        Cliente cliente = repositorio.findById(clienteId);
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
            return cliente;
        }
        return null; 
    }

    @DeleteMapping("/{clienteId}/endereco/delete/{enderecoId}")
    public Cliente deletarEndereco(@PathVariable long clienteId, @PathVariable long enderecoId) {
        Cliente cliente = repositorio.findById(clienteId);
        if (cliente != null && cliente.getEndereco() != null && cliente.getEndereco().getId().equals(enderecoId)) {
            cliente.setEndereco(null);
            repositorio.save(cliente);
            return cliente;
        }
        return null; 
    }
}
