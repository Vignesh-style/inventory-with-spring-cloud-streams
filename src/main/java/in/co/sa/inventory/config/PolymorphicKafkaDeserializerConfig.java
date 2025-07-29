package in.co.sa.inventory.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import in.co.sa.inventory.commons.SimpleQueueObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;

@Configuration
public class PolymorphicKafkaDeserializerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        mapper.registerSubtypes(
//            new NamedType(InventoryDataBuilderQueueObject.class, "InvBuilderQInput"),
//            new NamedType(InventoryDataPersisterQueueObject.class, "InvPersisterQInput"),
//            new NamedType(PostReconInvokerCommand.class, "PostReconInput"),
            new NamedType(SimpleQueueObject.class, "SimpleQueueObject")
        );

        return mapper;
    }

    @Bean
    public MessageConverter customMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setObjectMapper(objectMapper);
        converter.setSerializedPayloadClass(String.class);  // Important for JSON
        converter.setStrictContentTypeMatch(false);          // Loosen matching
        return converter;
    }
}
