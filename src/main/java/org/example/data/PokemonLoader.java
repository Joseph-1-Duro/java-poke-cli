package org.example.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Pokemon;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PokemonLoader {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Pokemon> loadPokemon(String fileName) throws IOException {
        // Load data as a stream
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            return objectMapper.readValue(is, new TypeReference<List<Pokemon>>() {});
        } catch (IOException e) {
            throw new IOException("File error" + e);
        }
    }
}
