package com.freeboard01.api.user;

import com.freeboard01.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final HttpSession httpSession;
    private final UserService userService;

    @PostMapping
    private ResponseEntity<Boolean> join(@RequestBody UserForm user){
        return ResponseEntity.ok(userService.join(user));
    }

    @PostMapping(params = {"type=LOGIN"})
    private ResponseEntity<Boolean> login(@RequestBody UserForm user){
        if(userService.login(user) == true){
            httpSession.setAttribute("USER", user);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
}
