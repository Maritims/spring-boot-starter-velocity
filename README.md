# Spring Boot Velocity Starter

This is a Spring Boot starter which adds [Apache Velocity](https://velocity.apache.org/) support to your Spring Boot project.

## Getting started
1. Add the following dependency to your Spring Boot project's `pom.xml` file:
    ```xml
    <dependency>
        <groupId>io.github.maritims</groupId>
        <artfiactId>spring-boot-velocity-starter</artfiactId>
        <version>1.0.0</version>
    </dependency>
    ```
2. Configure your `application.yml` file:
    ```yaml
   spring:
       velocity:
           resource-loader-path: classpath:/templates/ # The Velocity engine will look for Velocity template files in templates/ in your class path. It's typically placed in src/main/resources.
           suffix: .vm                                 # The Velocity engine will interpret files with the extension .vm as Velocity template files.
           layout-url: /layout/default.vm              # Velocity views will merge the contents of templates/layout/default.vm into any template before rendering said template. 
           toolbox-config-location: tools.xml          # The Velocity Tools Initializer will expect tools.xml to be in your class path. It's typically placed in src/main/resources.
    ```
3. Annotate your entry point class with the following annotation:
    ```java
    @EnableConfigurationProperties(value = { VelocityProperties.class })
    ```
4. Annotate your entry point class with the following annotation to scan the Velocity components:
   ```java
   @ComponentScan(value = { "io.github.maritims.spring_boot_velocity_starter" })
   ```
5. Add the `VM_library.vm` file to your templates/ directory. This is where you'll put your Velocity macros.
6. Add the `tools.xml` to your class path so that it's found during startup.
   
If you wish to use the `ResourceTool` class from the Velocity Tools project, make sure you place the `resources_en_US.properties` file in `src/main/resources`.