package io.github.aikusonitradesystem.mvcstandard.config;

import io.github.aikusonitradesystem.mvcstandard.model.view.ATSResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.aikusonitradesystem.core.utils.MessageUtils.m;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/locale-config")
public class LocaleConfigTestController {
    @RequestMapping(method = {POST, GET}, path = "/test", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> test() {
        return ATSResponseBody.ok("OK", m("mvc.locale_config_test"))
                .toResponseEntity();
    }
}
