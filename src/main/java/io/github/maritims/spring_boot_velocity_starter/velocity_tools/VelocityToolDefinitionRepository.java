package io.github.maritims.spring_boot_velocity_starter.velocity_tools;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Repository
public class VelocityToolDefinitionRepository implements BeanFactoryPostProcessor {
    private final LinkedHashMap<String, VelocityToolDefinition> _repository = new LinkedHashMap<>();
    private       BeanFactory                                   _beanFactory;
    /**
     * A map of all the instantiated tools based on the instances of {@link VelocityToolDefinition} in {@link this#_repository}.
     * This map is initialized during {@link this#init()} during the Spring lifecycle init phase.
     */
    private       Map<String, Object>                           _tools;

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        _beanFactory = beanFactory;
    }

    /**
     * Called whenever a {@link ContextRefreshedEvent} occurs as defined in {@link VelocityToolsInitializer#onApplicationEvent(ContextRefreshedEvent)}.
     */
    void init() {
        final var tools = new LinkedHashMap<String, Object>();

        for (var entry : _repository.entrySet()) {
            var key            = entry.getKey();
            var toolDefinition = entry.getValue();
            var toolInstance   = _beanFactory.containsBean(key) ? _beanFactory.getBean(key) : toolDefinition.create(Map.of());
            tools.put(key, toolInstance);
        }

        _tools = Collections.unmodifiableMap(tools);
    }

    void putAll(Map<String, VelocityToolDefinition> toolDefinitions) {
        _repository.putAll(toolDefinitions);
    }

    Map<String, VelocityToolDefinition> findAll() {
        return Collections.unmodifiableMap(_repository);
    }

    public Map<String, Object> getAllTools() {
        return _tools;
    }
}