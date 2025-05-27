package com.team22.webcraft.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://webcraftpc.com", allowCredentials = "true", methods={RequestMethod.POST, RequestMethod.HEAD})
public class TestController {
    @RequestMapping(value = "/ping", method = RequestMethod.HEAD)
    public ResponseEntity<Void> ping() {
        return ResponseEntity.ok().build();
    }
}
