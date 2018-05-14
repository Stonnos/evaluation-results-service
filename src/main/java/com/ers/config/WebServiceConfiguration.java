package com.ers.config;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

/**
 * Web - service configuration.
 *
 * @author Roman Batygin
 */
@EnableWs
@Configuration
public class WebServiceConfiguration extends WsConfigurerAdapter {

    private static final String WS_URL_MAPPINGS = "/ws/*";

    /**
     * Creates service config bean.
     *
     * @return service config bean
     */
    @Bean
    public ServiceConfig serviceConfig() {
        return new ServiceConfig();
    }

    /**
     * Creates web service config bean.
     *
     * @return web service config bean
     */
    @Bean
    public WebServiceConfig webServiceConfig() {
        return new WebServiceConfig();
    }

    /**
     * Bean for setting up servlet properties.
     *
     * @param applicationContext context
     * @return bean for servlet registration
     */
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, WS_URL_MAPPINGS);
    }

    /**
     * Bean for creating xsd schemas binding
     *
     * @param evaluationSchema - xsd schema
     * @param webServiceConfig - web service config
     * @return wsdl definition bean
     */
    @Bean(name = "evaluation")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema evaluationSchema,
                                                           WebServiceConfig webServiceConfig) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(webServiceConfig.getWsdlConfig().getPortTypeName());
        wsdl11Definition.setLocationUri(webServiceConfig.getWsdlConfig().getLocationUri());
        wsdl11Definition.setTargetNamespace(webServiceConfig.getWsdlConfig().getTargetNamespace());
        wsdl11Definition.setSchema(evaluationSchema);
        return wsdl11Definition;
    }

    /**
     * Creates xsd schema bean.
     *
     * @param webServiceConfig - evaluation config bean
     * @return xsd schema bean
     */
    @Bean
    public XsdSchema evaluationSchema(WebServiceConfig webServiceConfig) {
        return new SimpleXsdSchema(new ClassPathResource(webServiceConfig.getXsdSchema()));
    }
}
