package com.arep.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    // Devuelve exactamente lo que envías para comprobar parseo JSON/headers
    public static record EchoRequest(String username, String password) {}

    @PostMapping("/echo")
    public ResponseEntity<?> echo(@RequestBody(required = false) EchoRequest req) {
        System.out.println("DEBUG /api/debug/echo -> req = " + req);
        if (req == null) return ResponseEntity.badRequest().body(Map.of("error", "no body"));
        return ResponseEntity.ok(Map.of(
                "receivedUsername", req.username(),
                "receivedPasswordPresent", req.password() != null
        ));
    }
}
