package com.gsg.blogbackend.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;

/**
 * @author shuaigang
 * @since JDK 11.0.10
 * ClassName: ValidErrorUtil
 * date: 2021/9/9 14:33
 */

@Slf4j
public class ValidErrorUtil {

    public static Result<?> hasError(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            log.error("请求参数出现错误>>> {}", errorMessage);
            return Result.ok(BaseUtil.encode(R.construct(R.ERROR_PARAMS, errorMessage)));
        }
        return null;
    }
}
