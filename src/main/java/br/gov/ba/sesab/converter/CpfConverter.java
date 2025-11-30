package br.gov.ba.sesab.converter;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;

@FacesConverter("cpfConverter")
public class CpfConverter implements Converter<String> {

    @Override
    public String getAsObject(FacesContext ctx, UIComponent comp, String value) {
        if (value == null) {
            return null;
        }

        // Remove tudo que não for número
        return value.replaceAll("\\D", "");
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.length() != 11) {
            return value;
        }

        // Formata: 000.000.000-00
        return value.replaceFirst(
            "(\\d{3})(\\d{3})(\\d{3})(\\d{2})",
            "$1.$2.$3-$4"
        );
    }
}
