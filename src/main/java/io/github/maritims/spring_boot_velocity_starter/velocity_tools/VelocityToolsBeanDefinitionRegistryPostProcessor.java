package io.github.maritims.spring_boot_velocity_starter.velocity_tools;

import jakarta.servlet.ServletContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.genericBeanDefinition;

public class VelocityToolsBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, ServletContextInitializer, Ordered {
    private static final Logger log = LogManager.getLogger();

    private final VelocityToolDefinitionRepository _velocityToolDefinitionRepository;
    private       BeanDefinitionRegistry           _beanDefinitionRegistry;

    public VelocityToolsBeanDefinitionRegistryPostProcessor(VelocityToolDefinitionRepository velocityToolDefinitionRepository) {
        _velocityToolDefinitionRepository = velocityToolDefinitionRepository;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        _beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        for (var entry : _velocityToolDefinitionRepository.findAll().entrySet()) {
            var key = entry.getKey();
            if (_beanDefinitionRegistry.containsBeanDefinition(key)) {
                log.debug("{} is already a registered bean", key);
                continue;
            }

            var toolDefinition = entry.getValue();
            var builder        = genericBeanDefinition(toolDefinition.getToolClass());
            var beanDefinition = builder.getBeanDefinition();
            _beanDefinitionRegistry.registerBeanDefinition(key, beanDefinition);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}