package com.protectoria.okay.example;

import java.util.concurrent.ThreadLocalRandom;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.protectoria.okay.example.client.OkayRestClient;
import com.protectoria.okay.example.jackson.CallbackTypeJsonDeserializer;
import com.protectoria.okay.example.jackson.EnumJsonSerializer;
import com.protectoria.okay.example.jackson.ServerCallbackJsonDeserializer;
import com.protectoria.okay.example.jackson.TenantResponseStatusCodeJsonDeserializer;
import com.protectoria.pss.dto.session.SessionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperFactoryBean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestOperations;

@SpringBootApplication
public class CommandLineApplication implements ApplicationRunner {

    @Autowired
    private OkayRestClient okayRestClient;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // do nothing
    }

    public static void main(String... args) {
        SpringApplication.run(CommandLineApplication.class, args);
    }

    public void linkingExample() {
        final String linkingCode = okayRestClient.linkUser("external-user-id-" + ThreadLocalRandom.current().nextInt());
    }

    public void authExample() {
        final String session = okayRestClient.authUser(
                "user-external-id", SessionType.AUTH_OK, "header", "value");
    }

    public void checkExample() {
        String response = okayRestClient.checkStatus("session-id");
    }

    @Bean
    public Jackson2ObjectMapperFactoryBean getJackson2ObjectMapperFactoryBean() {
        final Jackson2ObjectMapperFactoryBean factoryBean = new Jackson2ObjectMapperFactoryBean();
        factoryBean.setSerializers(new EnumJsonSerializer());
        factoryBean.setDeserializers(
                new CallbackTypeJsonDeserializer(),
                new TenantResponseStatusCodeJsonDeserializer(),
                new ServerCallbackJsonDeserializer());
        return factoryBean;
    }

    @Autowired
    public void configureObjectMapper(ObjectMapper objectMapper) {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
    }

    @Bean
    public RestOperations getRestOperations(ObjectMapper objectMapper) {
        return new RestTemplateBuilder()
                .messageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();
    }

    @Bean
    public OkayRestClient getOkayRestClient(RestOperations restOperations) {
        final OkayRestClient client = new OkayRestClient();
        client.setTenantId(20002L);
        client.setTenantSecretToken("my-super-secret-token");
        client.setRestOperations(restOperations);
        return client;
    }


}
