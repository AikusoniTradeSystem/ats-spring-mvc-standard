package io.github.aikusonitradesystem.mvcstandard.advice;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@WebMvcTest(controllers = CheckRoleTestController.class)
@Import({AopAutoConfiguration.class, CheckRoleAdvice.class, RestExceptionHandler.class})
@ContextConfiguration(classes = {CheckRoleTestController.class})
public class CheckRoleTestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @AfterAll
    public static void afterAllTests() {
        log.info("##### 권한 테스트가 완료되었습니다. #####");
    }

    @Test
    @DisplayName("ADMIN 권한으로 ADMIN API 접근 테스트")
    void adminToAdmin() throws Exception {
        mockMvc.perform(
                        get("/check-role/admin")
                                .header("X-Roles", "ADMIN")
                                .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("OK"))
                .andExpect(jsonPath("$.message").value("관리자 권한으로 접근 했습니다."));
    }

    @Test
    @DisplayName("ADMIN 권한으로 USER API 접근 테스트")
    void adminToUser() throws Exception {
        mockMvc.perform(
                        get("/check-role/user")
                                .header("X-Roles", "ADMIN")
                                .accept("application/json")
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"))
                .andExpect(jsonPath("$.errorAlias").value("RCA-000003"))
                .andExpect(jsonPath("$.message").value("권한이 없는 계정으로 접근 했습니다."));
    }

    @Test
    @DisplayName("USER 권한으로 ADMIN API 접근 테스트")
    void userToAdmin() throws Exception {
        mockMvc.perform(
                        get("/check-role/admin")
                                .header("X-Roles", "USER")
                                .accept("application/json"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"))
                .andExpect(jsonPath("$.errorAlias").value("RCA-000003"))
                .andExpect(jsonPath("$.message").value("권한이 없는 계정으로 접근 했습니다."));
    }

    @Test
    @DisplayName("USER 권한으로 USER API 접근 테스트")
    void userToUser() throws Exception {
        mockMvc.perform(
                        get("/check-role/user")
                                .header("X-Roles", "USER")
                                .accept("application/json")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("일반 사용자 권한으로 접근 했습니다."));
    }

    @Test
    @DisplayName("권한이 설정되지 않은 계정으로 접근 테스트")
    void anonymous() throws Exception {
        mockMvc.perform(
                        get("/check-role/admin")
                                .accept("application/json")
                )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.errorCode").value("FORBIDDEN"))
                .andExpect(jsonPath("$.errorAlias").value("RCA-000002"))
                .andExpect(jsonPath("$.message").value("권한이 설정되지 않은 계정으로 접근 했습니다."));
    }
}
