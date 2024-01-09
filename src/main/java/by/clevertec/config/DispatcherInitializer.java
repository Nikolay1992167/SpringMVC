package by.clevertec.config;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class DispatcherInitializer implements WebApplicationInitializer {

    /**
     * Initializes the web application by creating an {@link AnnotationConfigWebApplicationContext} and registering
     * the {@link WebMvcConfig} configuration. Then creates a {@link DispatcherServlet} instance and registers it with
     * the ServletContext to handle incoming requests.The servlet is named "dispatcher" and is mapped to the root
     * URL ("/").
     *
     * @param servletContext the ServletContext of the web application.
     */
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(WebMvcConfig.class);

        ServletRegistration.Dynamic dispatcher
                = servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}