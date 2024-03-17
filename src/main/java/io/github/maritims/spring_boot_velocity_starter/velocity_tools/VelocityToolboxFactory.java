package io.github.maritims.spring_boot_velocity_starter.velocity_tools;

import org.apache.velocity.tools.ToolInfo;
import org.apache.velocity.tools.ToolboxFactory;

import java.util.Map;

class VelocityToolboxFactory extends ToolboxFactory {
    private final Map<String, VelocityToolDefinition> _velocityToolDefinitions;

    VelocityToolboxFactory(Map<String, VelocityToolDefinition> velocityToolDefinitions) {
        _velocityToolDefinitions = velocityToolDefinitions;
    }

    @Override
    protected void addToolInfo(String scope, ToolInfo toolInfo) {
        super.addToolInfo(scope, toolInfo);
        _velocityToolDefinitions.put(toolInfo.getKey(), new VelocityToolDefinition(toolInfo));
    }
}