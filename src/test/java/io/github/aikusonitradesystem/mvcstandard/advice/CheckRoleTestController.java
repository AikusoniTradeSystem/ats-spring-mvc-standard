package io.github.aikusonitradesystem.mvcstandard.advice;

import io.github.aikusonitradesystem.mvcstandard.model.view.ATSResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.aikusonitradesystem.core.utils.MessageUtils.m;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/check-role")
public class CheckRoleTestController {
    @CheckRole("ADMIN")
    @RequestMapping(method = {POST, GET}, path = "/admin", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> admin() {
        return ATSResponseBody.ok("OK", m("mvc.admin_permission"))
                .toResponseEntity();
    }

    @CheckRole("USER")
    @RequestMapping(method = {POST, GET}, path = "/user", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> user() {
        return ATSResponseBody.ok(m("mvc.user_permission"))
                .toResponseEntity();
    }
}
