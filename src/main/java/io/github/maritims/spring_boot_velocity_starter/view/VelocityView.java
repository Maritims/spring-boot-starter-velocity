package io.github.maritims.spring_boot_velocity_starter.view;

import io.github.maritims.spring_boot_velocity_starter.VelocityProperties;
import io.github.maritims.spring_boot_velocity_starter.velocity_tools.VelocityToolDefinitionRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.view.AbstractTemplateView;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VelocityView extends AbstractTemplateView {
    private VelocityEngine                   _velocityEngine;
    private VelocityProperties               _velocityProperties;
    private VelocityToolDefinitionRepository _velocityToolDefinitionRepository;

    @Override
    protected void initApplicationContext(@NonNull ApplicationContext context) {
        super.initApplicationContext(context);
        if (_velocityEngine == null) {
            _velocityEngine = BeanFactoryUtils.beanOfTypeIncludingAncestors(obtainApplicationContext(), VelocityEngine.class, true, false);
        }
        if (_velocityProperties == null) {
            _velocityProperties = BeanFactoryUtils.beanOfTypeIncludingAncestors(obtainApplicationContext(), VelocityProperties.class, true, false);
        }
        if (_velocityToolDefinitionRepository == null) {
            _velocityToolDefinitionRepository = BeanFactoryUtils.beanOfTypeIncludingAncestors(obtainApplicationContext(), VelocityToolDefinitionRepository.class, true, false);
        }
    }

    @Override
    protected void renderMergedTemplateModel(@NonNull Map<String, Object> model, @NonNull HttpServletRequest request, @NonNull HttpServletResponse response) throws Exception {
        var velocityContext       = new VelocityContext(model);
        var url                   = getUrl();
        var screenContentTemplate = _velocityEngine.getTemplate(url);
        var stringWriter          = new StringWriter();

        _velocityToolDefinitionRepository.getAllTools().forEach(velocityContext::put);

        velocityContext.put("request", request);
        velocityContext.put("response", response);
        screenContentTemplate.merge(velocityContext, stringWriter);
        velocityContext.put("screen_content", stringWriter.toString());

        var layout = (String) velocityContext.get("layout");
        if (layout == null) {
            layout = _velocityProperties.layoutUrl();
        }
        var layoutTemplate = _velocityEngine.getTemplate(layout);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        layoutTemplate.merge(velocityContext, response.getWriter());
    }
}