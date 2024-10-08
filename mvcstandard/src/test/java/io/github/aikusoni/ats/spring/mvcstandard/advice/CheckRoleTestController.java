package io.github.aikusoni.ats.spring.mvcstandard.advice;

import io.github.aikusoni.ats.spring.mvcstandard.constants.WebMvcMessageCode;
import io.github.aikusoni.ats.spring.mvcstandard.constants.WebMvcTestMessageCode;
import io.github.aikusoni.ats.spring.mvcstandard.model.view.ATSResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.aikusoni.ats.spring.mvcstandard.constants.WebMvcTestMessageCode.*;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/check-role")
public class CheckRoleTestController {
    @CheckRole("ADMIN")
    @RequestMapping(method = {POST, GET}, path = "/admin", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> admin() {
        return ATSResponseBody.ok("OK", ADMIN_PERMISSION)
                .toResponseEntity();
    }

    @CheckRole("USER")
    @RequestMapping(method = {POST, GET}, path = "/user", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> user() {
        return ATSResponseBody.ok("OK", USER_PERMISSION.getMessage())
                .toResponseEntity();
    }
}
