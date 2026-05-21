package com.nkh.ecommercebackend.service.impl;

import com.nkh.ecommercebackend.service.SlugGenerator;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class SlugGeneratorImpl implements SlugGenerator {
    @Override
    public String generateSlug(String name) {
        String normalized = Normalizer.normalize(name, Normalizer.Form.NFD);

        return normalized
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("(^-|-$)", "");
    }
}
