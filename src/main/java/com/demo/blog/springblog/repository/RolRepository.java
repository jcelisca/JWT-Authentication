package com.demo.blog.springblog.repository;

import com.demo.blog.springblog.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    public Optional<Rol> findByNombre(String nombre);
}
