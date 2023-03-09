package com.beandon.backend.configurations;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestMaskingFilter {

    private String value;

    public RequestMaskingFilter(String value) {
        this.value = value;
    }

    public RequestMaskingFilter removePasswords() {
        regexReplace(Pattern.compile("(\"password\" *: *\")[^\"]*(\")"));
        return this;
    }

    private void regexReplace(Pattern pattern) {
        Matcher m = pattern.matcher(value);
        if (m.find()) {
            value = m.replaceAll("$1************$2");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
