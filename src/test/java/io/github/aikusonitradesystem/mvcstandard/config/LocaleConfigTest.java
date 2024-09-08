package io.github.aikusonitradesystem.mvcstandard.config;

import io.github.aikusonitradesystem.core.config.MessageConfig;
import io.github.aikusonitradesystem.mvcstandard.advice.CheckRoleTestController;
import io.github.aikusonitradesystem.mvcstandard.advice.RestExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Locale;

import static io.github.aikusonitradesystem.core.utils.MessageUtils.m;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = LocaleConfigTestController.class)
@Import({MessageConfig.class, LocaleConfig.class})
@ContextConfiguration(classes = {LocaleConfigTestController.class})
public class LocaleConfigTest {
    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    public static void afterAllTests() {
        log.info("##### 로케일 테스트가 완료되었습니다. #####");
    }

    @Test
    @DisplayName("")
    void test() throws Exception {
        for (var locale : Locale.getAvailableLocales()) {
            mockMvc.perform(
                            get("/locale-config/test")
                                    .header("Accept-Language", locale.toLanguageTag())
                                    .accept("application/json")
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("OK"))
                    .andExpect(jsonPath("$.message").value(m("mvc.locale_config_test", null, locale)));
        }
    }
}
