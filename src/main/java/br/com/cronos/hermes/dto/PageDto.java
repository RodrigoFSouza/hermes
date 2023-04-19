package br.com.cronos.hermes.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

@JsonComponent
public class PageDto extends JsonSerializer<PageImpl<?>> {

    @Override
    public void serialize(PageImpl<?> page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeObjectField("dados", page.getContent());
        jsonGenerator.writeNumberField("totalPaginas", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElementos", page.getTotalElements());
        jsonGenerator.writeNumberField("totalElementosPorPagina", page.getSize());
        jsonGenerator.writeNumberField("paginaAtual", page.getNumber());
        jsonGenerator.writeBooleanField("ultima", page.isLast());
        jsonGenerator.writeBooleanField("primeira", page.isFirst());

        jsonGenerator.writeEndObject();
    }
}
