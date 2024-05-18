package br.com.pizzaria.util;

import java.util.Base64;

public class ImageUtil {
    public static String convertBlobToBase64PNG(byte[] blob) {
        return "data:image/png;base64," + Base64.getEncoder().encodeToString(blob);
    }
}
