package io.github.maritims.spring_boot_velocity_starter;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("spring.velocity")
public record VelocityProperties(
    @NotEmpty String resourceLoaderPath,
    @NotEmpty String suffix,
    @NotEmpty String layoutUrl,
    @NotEmpty String toolboxConfigLocation,
    boolean velocimacroLibraryAutoReload
) {
}
