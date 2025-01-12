package com.groom.sumbisori.common.config;

import com.groom.sumbisori.common.springdoc.ApiExceptionExplainParser;
import com.groom.sumbisori.common.util.StringCaseConverter;
import com.groom.sumbisori.domain.content.entity.Spot;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import java.util.Arrays;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

/**
 * SpringDoc OpenAPI 설정을 정의하는 클래스입니다.
 */
@Configuration
public class SpringDocConfig {


    /**
     * SpringDoc OpenAPI 스펙을 반환하는 Bean을 생성합니다.
     *
     * @return OpenAPI 객체
     */
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Sumbisori API")
                .version("v1");

        return new OpenAPI()
                .info(info);
    }

    @Bean
    public OperationCustomizer customizer() {
        return (Operation operation, HandlerMethod handlerMethod) -> {
            ApiExceptionExplainParser.parse(operation, handlerMethod); // 예외 설명 파싱
            return operation;
        };
    }

    /**
     * 전역적으로 특정 파라미터(예: @LoginUser)를 Swagger 문서에서 숨기는 설정을 정의합니다.
     *
     * @return OpenApiCustomiser 객체
     */
    @Bean
    public OpenApiCustomizer globalHeaderCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem ->
                pathItem.readOperations().forEach(operation -> {
                    if (operation.getParameters() != null) {
                        operation.setParameters(
                                operation.getParameters().stream()
                                        .filter(parameter -> !parameter.getName().equals("userId"))  // userId 파라미터 필터링
                                        .toList()
                        );
                    }
                })
        );
    }

    @Bean
    public OperationCustomizer enumCustomizer() {
        return (operation, handlerMethod) -> {
            if (operation.getParameters() == null) {
                return operation;
            }

            operation.getParameters().forEach(parameter -> {
                if (!"spot".equals(parameter.getName()) || parameter.getSchema() == null) {
                    return;
                }

                // Enum 값 리스트 설정
                parameter.getSchema().setEnum(
                        Arrays.stream(Spot.values())
                                .map(spot -> StringCaseConverter.toLowerCaseWithHyphens(spot.name()))
                                .toList()
                );

                // 기본값 변환 및 설정
                Object defaultValue = parameter.getSchema().getDefault();
                if (defaultValue != null) {
                    parameter.getSchema().setDefault(
                            StringCaseConverter.toLowerCaseWithHyphens(defaultValue.toString())
                    );
                }
            });

            return operation;
        };
    }
}
