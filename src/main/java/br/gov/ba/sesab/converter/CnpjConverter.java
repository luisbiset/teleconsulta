package br.gov.ba.sesab.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("cnpjConverter")
public class CnpjConverter implements Converter<String> {

    @Override
    public String getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null) return null;
        return value.replaceAll("\\D", ""); 
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.length() != 14) return value;

        return value.replaceFirst(
            "(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})",
            "$1.$2.$3/$4-$5"
        );
    }
}

