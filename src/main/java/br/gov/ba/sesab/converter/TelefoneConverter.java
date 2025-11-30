package br.gov.ba.sesab.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(forClass = String.class, value = "telefoneConverter")
public class TelefoneConverter implements Converter<String> {

    @Override
    public String getAsObject(FacesContext context, UIComponent component, String value) {
        if (value == null) return null;

        return value.replaceAll("\\D", "");
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, String value) {
        if (value == null) return "";

        return value;
    }
}
