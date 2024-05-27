package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class TelefoneControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/telefone/{id}")
    public List<Telefone> obterTelefones(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id);
        if (cliente != null) {
            return cliente.getTelefones();
        }
        return null;
    }
    
    @PostMapping("/{id}/telefone/add")
    public Cliente adicionarTelefone(@PathVariable long id, @RequestBody Telefone novoTelefone) {
        Cliente cliente = repositorio.findById(id);
        cliente.getTelefones().add(novoTelefone);
        repositorio.save(cliente);
        return null;
    }
    
    @PutMapping("/{clienteId}/telefone/update/{telefoneId}")
    public Cliente atualizarTelefone(@PathVariable long clienteId, @PathVariable long telefoneId, @RequestBody Telefone atualizacaoTelefone) {
        Cliente cliente = repositorio.findById(clienteId);
        if (cliente != null) {
            Telefone telefone = cliente.getTelefones().stream()
                .filter(t -> t.getId().equals(telefoneId))
                .findFirst()
                .orElse(null);

            if (telefone != null) {
                telefone.setDdd(atualizacaoTelefone.getDdd());
                telefone.setNumero(atualizacaoTelefone.getNumero());
                repositorio.save(cliente);
                return null;
            }
        }
        return null;
    }
    @DeleteMapping("/{clienteId}/telefone/delete/{telefoneId}")
    public Cliente deletarTelefone(@PathVariable long clienteId, @PathVariable long telefoneId) {
        Cliente cliente = repositorio.findById(clienteId);
        if (cliente != null) {
            Telefone telefone = cliente.getTelefones().stream()
                .filter(t -> t.getId().equals(telefoneId))
                .findFirst()
                .orElse(null);

            if (telefone != null) {
                cliente.getTelefones().remove(telefone);
                repositorio.save(cliente);
                return null;
            }
        }
        return null;
    }
}