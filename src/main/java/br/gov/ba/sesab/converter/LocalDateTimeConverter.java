package br.gov.ba.sesab.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.ConverterException;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "localDateTimeConverter", managed = true)
public class LocalDateTimeConverter implements Converter<LocalDateTime> {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Override
    public LocalDateTime getAsObject(FacesContext context, UIComponent component, String value) {

        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        try {
            return LocalDateTime.parse(value, FORMATTER);
        } catch (Exception e) {
        	FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_ERROR,
                    "Data inválida",
                    "Use o formato dd/MM/yyyy HH:mm"
                );
        	
        	throw new ConverterException(msg);
        }
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, LocalDateTime value) {

        if (value == null) {
            return "";
        }

        return value.format(FORMATTER);
    }
}
