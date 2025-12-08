package it.molinari.server.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GestioneJson 
{

    private final String[] archivi = {
            "src/main/java/it/molinari/server/files/archivio.json", 
            "src/main/java/it/molinari/server/files/users.json", 
            "src/main/java/it/molinari/server/files/whitelist.json", 
            "src/main/java/it/molinari/server/files/tokens.json",
            "src/main/java/it/molinari/server/files/prestiti.json"
    };

    private ObjectMapper mapper;

    public GestioneJson() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // ============================================================
    //  FUNZIONE GENERICA PER LEGGERE QUALSIASI TIPO DI LISTA
    // ============================================================
    public <T> List<T> leggiJson(int indiceFile, Class<T> clazz) {
        try {
            File file = new File(archivi[indiceFile]);
            if (!file.exists() || file.length() == 0) {
                // File vuoto: ritorna lista vuota
                return new ArrayList<>();
            }
            return mapper.readValue(file, mapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            System.out.println("ERRORE in leggiJson [" + clazz.getSimpleName() + "]");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    // ============================================================
    //  FUNZIONE GENERICA PER SCRIVERE QUALSIASI LISTA DI T
    // ============================================================
    public <T> boolean scriviJson(int indiceFile, List<T> lista) {

        try {
            File file = new File(archivi[indiceFile]);

            // crea eventuali cartelle mancanti
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            mapper.writeValue(file, lista);

            System.out.println("scriviJson OK [" + file.getName() + "] " + lista.size() + " elementi salvati");
            return true;

        } catch (Exception e) {
            System.out.println("ERRORE in scriviJson!");
            e.printStackTrace();
            return false;
        }
    }

}
