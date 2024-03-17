package io.github.maritims.spring_boot_velocity_starter.velocity_tools;

import io.github.maritims.spring_boot_velocity_starter.VelocityProperties;
import jakarta.servlet.ServletContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.ToolManager;
import org.apache.velocity.tools.config.ConfigurationUtils;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.web.context.support.WebApplicationContextUtils.getRequiredWebApplicationContext;

@Component
public class VelocityToolsInitializer implements ServletContextInitializer, ApplicationListener<ContextRefreshedEvent>, Ordered {
    private final VelocityProperties               _velocityProperties;
    private final VelocityToolDefinitionRepository _velocityToolDefinitionRepository;

    public VelocityToolsInitializer(VelocityProperties velocityProperties, VelocityToolDefinitionRepository velocityToolDefinitionRepository) {
        _velocityProperties               = velocityProperties;
        _velocityToolDefinitionRepository = velocityToolDefinitionRepository;
    }

    private Map<String, VelocityToolDefinition> scanToolDefinitions(ToolManager toolManager) {
        final var factoryConfiguration = ConfigurationUtils.findInClasspath(_velocityProperties.toolboxConfigLocation());
        final var toolDefinitions      = new LinkedHashMap<String, VelocityToolDefinition>();

        toolManager.setToolboxFactory(new VelocityToolboxFactory(toolDefinitions));
        toolManager.configure(factoryConfiguration);

        return Collections.unmodifiableMap(toolDefinitions);
    }

    @Override
    public void onStartup(ServletContext servletContext) {
        var webApplicationContext = getRequiredWebApplicationContext(servletContext);
        var velocityEngine        = webApplicationContext.getBean(VelocityEngine.class);
        var toolManager           = new ToolManager(false, false);
        toolManager.setVelocityEngine(velocityEngine);
        var toolDefinitions = scanToolDefinitions(toolManager);
        _velocityToolDefinitionRepository.putAll(toolDefinitions);
    }

    @Override
    public void onApplicationEvent(@NonNull ContextRefreshedEvent contextRefreshedEvent) {
        _velocityToolDefinitionRepository.init();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}