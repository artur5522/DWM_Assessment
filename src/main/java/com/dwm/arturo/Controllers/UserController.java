package com.dwm.arturo.Controllers;

import com.dwm.arturo.entities.User;
import com.dwm.arturo.model.Response;
import com.dwm.arturo.services.UserService;
import java.sql.DatabaseMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<?> getAll() {
        Response response;
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getAll());
        } catch (Exception e) {            
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOne(@PathVariable String id) {
        Response response;
        try {
            User user = userService.getOne(id);
            if (user == null) {
                response = new Response("There is no user with id: ".concat(id));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(id));
        } catch (Exception e) {
            response = new Response("Server Error: Try again later");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
