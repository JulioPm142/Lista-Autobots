package com.autobots.automanager.controles;

import com.autobots.automanager.entidades.Cliente;
import com.autobots.automanager.entidades.Documento;
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
public class DocumentoControle {

    @Autowired
    private ClienteRepositorio repositorio;

    @GetMapping("/{id}/documento")
    public ResponseEntity<CollectionModel<EntityModel<Documento>>> obterDocumentos(@PathVariable long id) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            List<EntityModel<Documento>> documentoModels = cliente.getDocumentos().stream()
                    .map(documento -> EntityModel.of(documento,
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                    .obterDocumentos(id)).withSelfRel(),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                    .atualizarDocumento(id, documento.getId(), documento)).withRel("update"),
                            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                    .deletarDocumento(id, documento.getId())).withRel("delete")))
                    .collect(Collectors.toList());

            CollectionModel<EntityModel<Documento>> collectionModel = CollectionModel.of(documentoModels);
            collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                    .obterDocumentos(id)).withSelfRel());
            collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                    .adicionarDocumento(id, null)).withRel("create"));

            return new ResponseEntity<>(collectionModel, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/{id}/documento/add")
    public ResponseEntity<EntityModel<Cliente>> adicionarDocumento(@PathVariable long id, @RequestBody Documento novoDocumento) {
        Cliente cliente = repositorio.findById(id).orElse(null);
        if (cliente != null) {
            cliente.getDocumentos().add(novoDocumento);
            repositorio.save(cliente);

            EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                            .adicionarDocumento(id, novoDocumento)).withSelfRel(),
                    WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                            .obterDocumentos(id)).withRel("documentos"));

            return new ResponseEntity<>(clienteModel, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{clienteId}/documento/update/{documentoId}")
    public ResponseEntity<EntityModel<Cliente>> atualizarDocumento(@PathVariable long clienteId, @PathVariable long documentoId, @RequestBody Documento atualizacaoDocumento) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            Documento documento = cliente.getDocumentos().stream()
                    .filter(d -> d.getId().equals(documentoId))
                    .findFirst()
                    .orElse(null);

            if (documento != null) {
                documento.setTipo(atualizacaoDocumento.getTipo());
                documento.setNumero(atualizacaoDocumento.getNumero());
                repositorio.save(cliente);

                EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                .atualizarDocumento(clienteId, documentoId, atualizacaoDocumento)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                .obterDocumentos(clienteId)).withRel("documentos"));

                return new ResponseEntity<>(clienteModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{clienteId}/documento/delete/{documentoId}")
    public ResponseEntity<EntityModel<Cliente>> deletarDocumento(@PathVariable long clienteId, @PathVariable long documentoId) {
        Cliente cliente = repositorio.findById(clienteId).orElse(null);
        if (cliente != null) {
            Documento documento = cliente.getDocumentos().stream()
                    .filter(d -> d.getId().equals(documentoId))
                    .findFirst()
                    .orElse(null);

            if (documento != null) {
                cliente.getDocumentos().remove(documento);
                repositorio.save(cliente);

                EntityModel<Cliente> clienteModel = EntityModel.of(cliente,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                .deletarDocumento(clienteId, documentoId)).withSelfRel(),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DocumentoControle.class)
                                .obterDocumentos(clienteId)).withRel("documentos"));

                return new ResponseEntity<>(clienteModel, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
