package com.ers.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import java.util.List;

/**
 * Web - service configuration.
 *
 * @author Roman Batygin
 */
@EnableWs
@Configuration
@EnableConfigurationProperties({ServiceConfig.class, WebServiceConfig.class})
@RequiredArgsConstructor
public class WebServiceConfiguration extends WsConfigurerAdapter {

    public static final String TARGET_NAMESPACE = "http://schemas.xmlsoap.org/soap/envelope/";

    private static final String PORT_TYPE_NAME = "EvaluationResultsPort";
    private static final String LOCATION_URI = "/ws";
    private static final String WS_URL_MAPPINGS = "/ws/*";

    private final WebServiceConfig webServiceConfig;

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setXsdSchema(evaluationSchema());
        interceptors.add(validatingInterceptor);
    }

    /**
     * Bean for setting up servlet properties.
     *
     * @param applicationContext context
     * @return bean for servlet registration
     */
    @Bean
    public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
            ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, WS_URL_MAPPINGS);
    }

    /**
     * Bean for creating xsd schemas binding
     *
     * @param evaluationSchema - xsd schema
     * @return wsdl definition bean
     */
    @Bean(name = "evaluation")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema evaluationSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName(PORT_TYPE_NAME);
        wsdl11Definition.setLocationUri(LOCATION_URI);
        wsdl11Definition.setTargetNamespace(TARGET_NAMESPACE);
        wsdl11Definition.setSchema(evaluationSchema);
        return wsdl11Definition;
    }

    /**
     * Creates xsd schema bean.
     *
     * @return xsd schema bean
     */
    @Bean
    public XsdSchema evaluationSchema() {
        return new SimpleXsdSchema(new ClassPathResource(webServiceConfig.getXsdSchema()));
    }
}
