package br.gov.ba.sesab.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter(value = "cpfConverter", managed = true)
public class CpfConverter implements Converter<String> {

    @Override
    public String getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        String apenasNumeros = value.replaceAll("\\D", "");

        if (apenasNumeros.length() != 11) {
            return null; 
        }

        return apenasNumeros;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        String apenasNumeros = value.replaceAll("\\D", "");

        if (apenasNumeros.length() != 11) {
            return apenasNumeros;
        }

        return apenasNumeros.replaceFirst(
            "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
            "$1.$2.$3-$4"
        );
    }
}
