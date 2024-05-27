package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.repositorios.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class DocumentoControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/{id}/documento")
    public List<Documento> obterDocumentos(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id);
        if (cliente != null) {
            return cliente.getDocumentos();
        }
        return null; 
    }

    @PostMapping("/{id}/documento/add")
    public Cliente adicionarDocumento(@PathVariable long id, @RequestBody Documento novoDocumento) {
        Cliente cliente = repositorio.findById(id);
        if (cliente != null) {
            cliente.getDocumentos().add(novoDocumento);
            repositorio.save(cliente);
            return cliente;
        }
        return null;
    }

    @PutMapping("/{clienteId}/documento/update/{documentoId}")
    public Cliente atualizarDocumento(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
        Cliente cliente = repositorio.findById(clienteId);
        if (cliente != null) {
            Documento documento = cliente.getDocumentos().stream()
                .filter(d -> d.getId().equals(documentoId))
                .findFirst()
                .orElse(null);

            if (documento != null) {
                documento.setTipo(atualizacaoDocumento.getTipo());
                documento.setNumero(atualizacaoDocumento.getNumero());
                repositorio.save(cliente);
                return cliente;
            }
        }
        return null; 
    }

    @DeleteMapping("/{clienteId}/documento/delete/{documentoId}")
    public Cliente deletarDocumento(@PathVariable long clienteId, @PathVariable long documentoId) {
        Cliente cliente = repositorio.findById(clienteId);
        if (cliente != null) {
            Documento documento = cliente.getDocumentos().stream()
                .filter(d -> d.getId().equals(documentoId))
                .findFirst()
                .orElse(null);

            if (documento != null) {
                cliente.getDocumentos().remove(documento);
                repositorio.save(cliente);
                return cliente;
            }
        }
        return null; 
    }
}
