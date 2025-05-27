package com.team22.webcraft.Controller;

import com.team22.webcraft.Controller.ReturnValue.SuccessReturn;
import com.team22.webcraft.DTO.UserData.UserDataDTO;
import com.team22.webcraft.Domain.UserData;
import com.team22.webcraft.Interceptor.SessionConst;
import com.team22.webcraft.Service.UserDataService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "https://webcraftpc.com", allowCredentials = "true", methods = {RequestMethod.HEAD, RequestMethod.POST})
public class UserDataController {
    private final UserDataService userService;

    @PostMapping(value = "/signUp", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SuccessReturn signUp(@RequestBody UserDataDTO dto) throws Exception {
        userService.signUp(dto);
        return new SuccessReturn("Sign up completed");
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public SuccessReturn signIn(@RequestBody UserDataDTO dto, HttpServletRequest request) {
        UserData userData = userService.signIn(dto);

        // login success
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_MEMBER, userData);
        session.setMaxInactiveInterval(1800);

        return new SuccessReturn("Sign in completed");
    }
}
