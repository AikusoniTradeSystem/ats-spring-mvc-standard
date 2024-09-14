package io.github.aikusoni.ats.spring.mvcstandard.constants;

import io.github.aikusoni.ats.core.common.MessageCode;

public interface WebMvcTestMessageCode {
    MessageCode ADMIN_PERMISSION = MessageCode.of("mvc.admin_permission");
    MessageCode LOCALE_CONFIG_TEST = MessageCode.of("mvc.locale_config_test");
    MessageCode USER_PERMISSION = MessageCode.of("mvc.user_permission");
}
