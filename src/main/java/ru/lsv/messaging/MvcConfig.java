package ru.lsv.messaging;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Spring MVC config
 * 
 * @author s.lezhnev
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter
     * #addViewControllers(org.springframework.web.servlet.config.annotation.
     * ViewControllerRegistry)
     */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/view").setViewName("view");
        registry.addViewController("/").setViewName("view");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/addMessage").setViewName("addMessage");
        registry.addViewController("/addAddrBook").setViewName("addToAddressBook");
    }
}
