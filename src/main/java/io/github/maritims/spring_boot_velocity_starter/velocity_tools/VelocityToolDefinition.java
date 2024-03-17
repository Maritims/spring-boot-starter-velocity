package io.github.maritims.spring_boot_velocity_starter.velocity_tools;

import org.apache.velocity.tools.ToolInfo;

import java.util.Map;

class VelocityToolDefinition {
    private final ToolInfo _toolInfo;

    VelocityToolDefinition(ToolInfo toolInfo) {
        _toolInfo = toolInfo;
    }

    Object create(Map<String, Object> dynamicProperties) {
        return _toolInfo.create(dynamicProperties);
    }

    Class<?> getToolClass() {
        return _toolInfo.getToolClass();
    }
}