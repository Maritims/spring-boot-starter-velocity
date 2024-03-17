package io.github.maritims.spring_boot_velocity_starter;

import io.github.maritims.spring_boot_velocity_starter.velocity_tools.VelocityToolDefinitionRepository;
import io.github.maritims.spring_boot_velocity_starter.velocity_tools.VelocityToolsBeanDefinitionRegistryPostProcessor;
import io.github.maritims.spring_boot_velocity_starter.view.VelocityViewResolver;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.spring.VelocityEngineFactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@AutoConfiguration
@AutoConfigureAfter(value = {
    WebMvcAutoConfiguration.class
})
public class VelocityAutoConfiguration {
    @Bean
    VelocityEngineFactoryBean velocityEngineFactoryBean(VelocityProperties velocityProperties) {
        var bean = new VelocityEngineFactoryBean();
        bean.setResourceLoaderPath(velocityProperties.resourceLoaderPath());
        bean.setVelocityPropertiesMap(new HashMap<>(Map.of(
            Velocity.ENCODING_DEFAULT, StandardCharsets.UTF_8.toString(),
            Velocity.INPUT_ENCODING, StandardCharsets.UTF_8.toString(),
            Velocity.VM_LIBRARY, "VM_library.vm",
            Velocity.VM_LIBRARY_AUTORELOAD, velocityProperties.velocimacroLibraryAutoReload()
        )));
        return bean;
    }

    @Bean
    @ConditionalOnMissingBean(name = "velocityViewResolver")
    VelocityViewResolver velocityViewResolver(VelocityProperties velocityProperties) {
        var velocityViewResolver = new VelocityViewResolver(null, velocityProperties.suffix());
        velocityViewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return velocityViewResolver;
    }

    @Bean
    BeanDefinitionRegistryPostProcessor velocityToolsBeanDefinitionRegistryPostProcessor(VelocityToolDefinitionRepository velocityToolDefinitionRepository) {
        return new VelocityToolsBeanDefinitionRegistryPostProcessor(velocityToolDefinitionRepository);
    }
}
