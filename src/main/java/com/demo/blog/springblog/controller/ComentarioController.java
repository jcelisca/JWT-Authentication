package com.demo.blog.springblog.controller;

import com.demo.blog.springblog.dto.ComentarioDTO;
import com.demo.blog.springblog.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api")
public class ComentarioController {

    @Autowired
    private ComentarioService service;

    @GetMapping("/publicaciones/{publicacionId}/comentarios")
    public List<ComentarioDTO> listarComentariosPorPublicacion(@PathVariable(value = "publicacionId") Long publicacionId){
        return service.mostrarComentariosPorPublicacion(publicacionId);
    }

    @GetMapping("/publicaciones/comentarios/{id}")
    public ResponseEntity<ComentarioDTO> mostrarComentarioPorId(@PathVariable(value = "id") Long comentarioId){
        ComentarioDTO comentarioDTO = service.mostrarComentarioPorId(comentarioId);
        return new ResponseEntity<>(comentarioDTO, HttpStatus.OK);
    }

    @PostMapping("/publicaciones/{publicacionId}/comentarios")
    public ResponseEntity<ComentarioDTO> guardarComentario(@PathVariable(value = "publicacionId") long publicacionId,@Valid @RequestBody ComentarioDTO comentarioDTO){
        return new ResponseEntity<>(service.crearComentario(publicacionId, comentarioDTO),HttpStatus.CREATED);
    }

    @PutMapping("/publicaciones/comentarios/{id}")
    public ResponseEntity<ComentarioDTO> actualizarComentario(@PathVariable(value = "id") Long comentarioId,@Valid @RequestBody ComentarioDTO comentarioDTO){
        ComentarioDTO comentarioActualizado = service.actualizarComentario(comentarioId, comentarioDTO);
        return new ResponseEntity<>(comentarioActualizado,HttpStatus.OK);
    }

    @DeleteMapping("/publicaciones/comentarios/{id}")
    public ResponseEntity<String> eliminarComentario(@PathVariable(value = "id") Long comentarioId){
        service.eliminarComentario(comentarioId);
        return new ResponseEntity<>("Comentario eliminado con exito",HttpStatus.OK);
    }
}
