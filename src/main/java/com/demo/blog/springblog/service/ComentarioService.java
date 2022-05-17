package com.demo.blog.springblog.service;

import com.demo.blog.springblog.dto.ComentarioDTO;
import com.demo.blog.springblog.exception.ResourceNotFoundException;
import com.demo.blog.springblog.model.Comentario;
import com.demo.blog.springblog.model.Publicacion;
import com.demo.blog.springblog.repository.ComentarioRepository;
import com.demo.blog.springblog.repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ComentarioRepository comentarioRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    public ComentarioDTO crearComentario(Long publicacionId, ComentarioDTO comentarioDTO){
        Publicacion publicacion = publicacionRepository.findById(publicacionId).orElseThrow(()-> new ResourceNotFoundException("Publicacion","id",publicacionId));
        Comentario comentario = modelMapper.map(comentarioDTO,Comentario.class);
        comentario.setPublicacion(publicacion);
        return modelMapper.map(comentarioRepository.save(comentario),ComentarioDTO.class);
    }

    public List<ComentarioDTO> mostrarComentariosPorPublicacion(Long publicacionId){
        List<Comentario> comentarios = comentarioRepository.findByPublicacionId(publicacionId);
        return comentarios.stream().map(comentario -> modelMapper.map(comentario, ComentarioDTO.class)).collect(Collectors.toList());
    }

    public ComentarioDTO mostrarComentarioPorId(Long comentarioId){
        Comentario comentario = comentarioRepository.findById(comentarioId).orElseThrow(()-> new ResourceNotFoundException("Comentario","id",comentarioId));

        return modelMapper.map(comentario, ComentarioDTO.class);
    }

    public ComentarioDTO actualizarComentario(Long comentarioId, ComentarioDTO comentarioDTO){
        Comentario comentario = comentarioRepository.findById(comentarioId).orElseThrow(()-> new ResourceNotFoundException("Comentario","id",comentarioId));
        comentario.setNombre(comentarioDTO.getNombre());
        comentario.setEmail(comentarioDTO.getEmail());
        comentario.setCuerpo(comentarioDTO.getCuerpo());
        return modelMapper.map(comentarioRepository.save(comentario), ComentarioDTO.class);
    }

    public void eliminarComentario(Long id){
        Comentario comentario = comentarioRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Comentario", "id", id));
        comentarioRepository.delete(comentario);
    }
}
