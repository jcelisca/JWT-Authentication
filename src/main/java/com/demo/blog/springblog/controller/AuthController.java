package com.demo.blog.springblog.controller;

import com.demo.blog.springblog.dto.LoginDTO;
import com.demo.blog.springblog.dto.RegistroDTO;
import com.demo.blog.springblog.model.Rol;
import com.demo.blog.springblog.model.Usuario;
import com.demo.blog.springblog.repository.RolRepository;
import com.demo.blog.springblog.repository.UsuarioRepository;
import com.demo.blog.springblog.security.JwtAuthResponseDTO;
import com.demo.blog.springblog.security.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDTO> autenticarUsuario(@RequestBody LoginDTO loginDTO){

        //Authentication authentication1 = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(),loginDTO.getPassword()));

        Authentication authentication = new UsernamePasswordAuthenticationToken(loginDTO.getUsernameOrEmail(),loginDTO.getPassword());

        Usuario usuario = usuarioRepository.findByUsernameOrEmail(loginDTO.getUsernameOrEmail(), loginDTO.getUsernameOrEmail())
                .orElseThrow(()->new UsernameNotFoundException("El usuario o email ingresado es incorrecto"));

        //logger.info("Contraseña: "+ passwordEncoder.encode(loginDTO.getPassword()));
        logger.info("Contraseña user: "+ usuario.getPassword().compareTo(loginDTO.getPassword()));
        if(passwordEncoder.encode(loginDTO.getPassword()).equals(usuario.getPassword())){
            logger.info("Comparacion: "+(passwordEncoder.encode(loginDTO.getPassword()).equals(usuario.getPassword())));
            new UsernameNotFoundException("La contraseña ingresada es incorrecta");
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        //obtenemos el token del jwtTokenProvider
        String token = jwtTokenProvider.generarToken(authentication);

        return ResponseEntity.ok(new JwtAuthResponseDTO(token));
    }

    @PostMapping("/registrar")
    public ResponseEntity<?> registrarUsuario(@RequestBody RegistroDTO registroDTO){
        if(usuarioRepository.existsByUsername(registroDTO.getUsername())) {
            return new ResponseEntity<>("Ese nombre de usuario ya existe", HttpStatus.BAD_REQUEST);
        }

        if(usuarioRepository.existsByEmail(registroDTO.getEmail())) {
            return new ResponseEntity<>("Ese email de usuario ya existe",HttpStatus.BAD_REQUEST);
        }

        Usuario usuario = new Usuario();
        usuario.setNombre(registroDTO.getNombre());
        usuario.setUsername(registroDTO.getUsername());
        usuario.setEmail(registroDTO.getEmail());
        usuario.setPassword(passwordEncoder.encode(registroDTO.getPassword()));

        Rol roles = rolRepository.findByNombre("ROLE_ADMIN").get();
        usuario.setRoles(Collections.singleton(roles));

        usuarioRepository.save(usuario);
        return new ResponseEntity<>("Usuario registrado exitosamente",HttpStatus.OK);
    }
}
