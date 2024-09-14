package io.github.aikusoni.ats.spring.mvcstandard.model.view;

import io.github.aikusoni.ats.spring.core.constants.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static io.github.aikusoni.ats.spring.mvcstandard.constants.WebMvcTestMessageCode.WITH_MESSAGE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/ats-response-body")
public class AtsResponseBodyTestController {
    @RequestMapping(method = {POST, GET}, path = "/only-ok", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> onlyOk() {
        return ATSResponseBody.ok("OK")
                .toResponseEntity();
    }

    @RequestMapping(method = {POST, GET}, path = "/with-message-ok", produces = "application/json")
    public ResponseEntity<ATSResponseBody<String>> withMessageOk() {
        return ATSResponseBody.ok("OK", WITH_MESSAGE)
                .toResponseEntity();
    }

    @RequestMapping(method = {POST, GET}, path = "/with-message-error", produces = "application/json")
    public ResponseEntity<ATSResponseBody<Void>> withMessageError() {
        return ATSResponseBody.<Void>error(ErrorCode.INTERNAL_SERVER_ERROR,"ART-0000001", WITH_MESSAGE)
                .toResponseEntity();
    }
}
