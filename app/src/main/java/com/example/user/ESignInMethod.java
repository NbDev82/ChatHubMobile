package com.example.user;

import java.util.ArrayList;
import java.util.List;

public enum ESignInMethod {
    PASSWORD("password"),
    GOOGLE("google.com"),
    GITHUB("github.com"),
    SMS("phone");

    private final String providerId;

    ESignInMethod(String providerId) {
        this.providerId = providerId;
    }

    public static ESignInMethod fromProviderId(String providerId) {
        for (ESignInMethod method : ESignInMethod.values()) {
            if (method.providerId.equals(providerId)) {
                return method;
            }
        }
        return null;
    }

    public static List<ESignInMethod> mapSignInMethods(List<String> signInMethods) {
        List<ESignInMethod> signInMethodEnums = new ArrayList<>();
        if (signInMethods == null) {
            return signInMethodEnums;
        }
        for (String providerId : signInMethods) {
            ESignInMethod signInMethod = fromProviderId(providerId);
            if (signInMethods != null) {
                signInMethodEnums.add(signInMethod);
            }
        }
        return signInMethodEnums;
    }
}
