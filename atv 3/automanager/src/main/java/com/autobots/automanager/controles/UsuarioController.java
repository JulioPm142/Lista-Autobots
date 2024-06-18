package com.autobots.automanager.controles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.repositorios.RepositorioUsuario;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private RepositorioUsuario usuarioRepositorio;

    @PostMapping
    public ResponseEntity<EntityModel<Usuario>> criarUsuario(@RequestBody Usuario usuario) {
        Usuario novoUsuario = usuarioRepositorio.save(usuario);
        return new ResponseEntity<>(adicionarLinks(novoUsuario), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Usuario>>> obterUsuarios() {
        List<EntityModel<Usuario>> usuarios = usuarioRepositorio.findAll().stream()
                .map(this::adicionarLinks)
                .collect(Collectors.toList());
        return new ResponseEntity<>(usuarios, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> obterUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isPresent()) {
            return new ResponseEntity<>(adicionarLinks(usuario.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Usuario>> atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuarioAtualizado) {
        Optional<Usuario> usuarioExistente = usuarioRepositorio.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setNomeSocial(usuarioAtualizado.getNomeSocial());
            usuario.setPerfis(usuarioAtualizado.getPerfis());
            usuario.setTelefones(usuarioAtualizado.getTelefones());
            usuario.setEndereco(usuarioAtualizado.getEndereco());
            usuario.setDocumentos(usuarioAtualizado.getDocumentos());
            usuario.setEmails(usuarioAtualizado.getEmails());
            usuario.setCredenciais(usuarioAtualizado.getCredenciais());
            usuario.setMercadorias(usuarioAtualizado.getMercadorias());
            usuario.setVendas(usuarioAtualizado.getVendas());
            usuario.setVeiculos(usuarioAtualizado.getVeiculos());
            usuarioRepositorio.save(usuario);
            return new ResponseEntity<>(adicionarLinks(usuario), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepositorio.findById(id);
        if (usuario.isPresent()) {
            usuarioRepositorio.delete(usuario.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private EntityModel<Usuario> adicionarLinks(Usuario usuario) {
        EntityModel<Usuario> resource = EntityModel.of(usuario);
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(UsuarioController.class).obterUsuarioPorId(usuario.getId())).withSelfRel());
        resource.add(WebMvcLinkBuilder.linkTo(methodOn(UsuarioController.class).obterUsuarios()).withRel("usuarios"));
        return resource;
    }
}
