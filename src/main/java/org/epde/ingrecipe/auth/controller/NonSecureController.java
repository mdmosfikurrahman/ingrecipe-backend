package org.epde.ingrecipe.auth.controller;

import org.epde.ingrecipe.common.response.RestResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/non-secure")
public class NonSecureController {

    private static final String SUCCESS_MESSAGE = "Request Successful";

    @GetMapping
    public RestResponse<String> baseEndpoint() {
        return RestResponse.success(HttpStatus.OK.value(), SUCCESS_MESSAGE, "This is the base of the non-secure API!");
    }

}
