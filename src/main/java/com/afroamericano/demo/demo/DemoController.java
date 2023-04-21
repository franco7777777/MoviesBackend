package com.afroamericano.demo.demo;



import com.afroamericano.demo.user.Role;
import com.afroamericano.demo.user.User;
import com.afroamericano.demo.user.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.mapping.Any;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/apidemo")
@CrossOrigin("*")
@RequiredArgsConstructor
public class DemoController {
    private final UserRepository repository;


    @GetMapping
    public Optional<User> sayHello(Principal principal) {
        System.out.println("soy principal");
        System.out.println(principal.getName());
        Optional<User> usuario = this.repository.findByEmail(principal.getName());
        return usuario;
    }
@PostMapping("/addmovies")
public User addMovie(@RequestBody Set<Number> bodyMovies, Principal principal) {
        Set<Number> pruebas = new HashSet<>(List.of(1,2,3,4,5,6,7,8));
        User usuario = this.repository.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    Set<Number> moviesId = new HashSet<>(bodyMovies);
    if(usuario.getPeliculas() != null) {
        moviesId.addAll(usuario.getPeliculas());
    }

    //moviesId.add((Number) usuario.getPeliculas());
    //moviesId.add(1);
    usuario.setPeliculas(moviesId);

    System.out.println("soy el usuario con la movie 0");
    this.repository.save(usuario);
    System.out.println("soy el usuario con la movie");
    System.out.println(usuario);
return usuario;


};

    @PutMapping("/deletemovies")
    public User deleteMovie(@RequestBody Set<Number> bodyMovies, Principal principal) {
        User usuario = this.repository.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        usuario.peliculas.removeAll(bodyMovies);
        this.repository.save(usuario);
        return usuario;
    };

    @PostMapping("/addseries")
    public User addSerie(@RequestBody Set<Number> bodySeries, Principal principal) {

        User usuario = this.repository.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        Set<Number> seriesId = new HashSet<>(bodySeries);
        if (usuario.getSeries() != null) {
            seriesId.addAll(usuario.getSeries());
        }
        usuario.setSeries(seriesId);
        this.repository.save(usuario);
        return usuario;
    };

    @PutMapping("/deleteseries")
    public User deleteSerie(@RequestBody Set<Number> bodySeries, Principal principal) {
        User usuario = this.repository.findByEmail(principal.getName()).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        usuario.series.removeAll(bodySeries);
        this.repository.save(usuario);
        return usuario;
    };

//   @GetMapping("/user")
//    public User sayHello6() {
//var user = User.builder()
//        .email("ychag@example.com")
//        .password("123456")
//        .role(Role.ADMIN)
//        .peliculas(new HashSet<Number>(List.of(1,2,3)))
//         .build();
//
//       return user;
//
//    }

//    @GetMapping("/user")
//    public ResponseEntity<String> sayHello3(Principal principal) {
//        return ResponseEntity.ok("hgola");
//    }
}