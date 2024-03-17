package io.github.maritims.spring_boot_velocity_starter.view;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.view.AbstractTemplateViewResolver;
import org.springframework.web.servlet.view.AbstractUrlBasedView;

public class VelocityViewResolver extends AbstractTemplateViewResolver {
    public VelocityViewResolver() {
        setViewClass(requiredViewClass());
    }

    public VelocityViewResolver(String prefix, String suffix) {
        this();
        setPrefix(prefix);
        setSuffix(suffix);
    }

    @Override
    protected @NonNull Class<?> requiredViewClass() {
        return VelocityView.class;
    }

    @Override
    protected @NonNull AbstractUrlBasedView instantiateView() {
        return new VelocityView();
    }
}
