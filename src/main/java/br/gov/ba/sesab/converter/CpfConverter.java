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

        // Remove tudo que não for número
        String apenasNumeros = value.replaceAll("\\D", "");

        // Garante que só volte se for válido
        if (apenasNumeros.length() != 11) {
            return null; // força cair na validação de required/pattern
        }

        return apenasNumeros;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent comp, String value) {
        if (value == null || value.isBlank()) {
            return "";
        }

        // Se já vier mascarado por algum motivo, limpa primeiro
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
