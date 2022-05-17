package com.demo.blog.springblog.service;

import com.demo.blog.springblog.dto.PublicacionDTO;
import com.demo.blog.springblog.dto.PublicacionRespuesta;
import com.demo.blog.springblog.exception.ResourceNotFoundException;
import com.demo.blog.springblog.model.Publicacion;
import com.demo.blog.springblog.repository.PublicacionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PublicacionService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PublicacionRepository repository;

    public PublicacionDTO crearPublicacion(PublicacionDTO publicacionDTO){
        Publicacion publicacion = repository.save(modelMapper.map(publicacionDTO, Publicacion.class));
        return modelMapper.map(publicacion, PublicacionDTO.class);
    }

    public PublicacionRespuesta obtenerPublicaciones(int numeroDePagina, int medidaDePagina, String ordenarPor, String sortDir){
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(ordenarPor).ascending()
                : Sort.by(ordenarPor).descending();

        Pageable pageable = PageRequest.of(numeroDePagina, medidaDePagina, sort);

        Page<Publicacion> publicaciones = repository.findAll(pageable);

        List<Publicacion> listaDePublicaciones = publicaciones.getContent();
        List<PublicacionDTO> contenido = listaDePublicaciones.stream().map(publicacion -> modelMapper.map(publicacion, PublicacionDTO.class))
                .collect(Collectors.toList());

        PublicacionRespuesta publicacionRespuesta = new PublicacionRespuesta();
        publicacionRespuesta.setContenido(contenido);
        publicacionRespuesta.setNumeroPagina(publicaciones.getNumber());
        publicacionRespuesta.setMedidaPagina(publicaciones.getSize());
        publicacionRespuesta.setTotalElementos(publicaciones.getTotalElements());
        publicacionRespuesta.setTotalPaginas(publicaciones.getTotalPages());
        publicacionRespuesta.setUltima(publicaciones.isLast());

        return publicacionRespuesta;
    }

    public PublicacionDTO obtenerPublicacionPorId(Long id){
        Publicacion publicacion = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion", "id", id));
        return modelMapper.map(publicacion, PublicacionDTO.class);
    }

    public PublicacionDTO actualizarPublicacion(PublicacionDTO publicacionDTO, Long id){
        Publicacion publicacion = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion", "id", id));

        publicacion.setTitulo(publicacionDTO.getTitulo());
        publicacion.setContenido(publicacionDTO.getContenido());
        publicacion.setDescripcion(publicacionDTO.getDescripcion());

        return modelMapper.map(repository.save(publicacion),PublicacionDTO.class);
    }

    public void eliminarPublicacion(Long id){
        Publicacion publicacion = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Publicacion", "id", id));
        repository.delete(publicacion);
    }
}
