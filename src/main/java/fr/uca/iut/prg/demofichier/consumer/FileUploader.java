package fr.uca.iut.prg.demofichier.consumer;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.http.client.reactive.ClientHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class FileUploader {

    private final WebClient client;


    public FileUploader(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:8080").build();
    }


    public Mono<String> send(String url, String s) {
        File toBeSent = new File(getClass().getResource(s).getFile());
        Mono<String> response = client.post()
                .uri(url).contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData("fileToUpload", new ClassPathResource(toBeSent.getName())))
                .exchange()
                .then(Mono.just("sent..."));

        return response;
    }

    public Mono<String> sendMulti(String url, String... s) {

        Mono<String> response = client.post().uri(url).contentType(MediaType.MULTIPART_FORM_DATA)
                .body(BodyInserters.fromMultipartData(fromFiles(s)))
                .exchange().then(Mono.just("sent..."));

        return response;
    }

    protected MultiValueMap<String, HttpEntity<?>> fromFiles(String... s) {
        MultipartBodyBuilder builder = new MultipartBodyBuilder();
        for (int i = 0; i < s.length; i++) {
            File toBeSent = new File(getClass().getResource(s[i]).getFile());
            builder.part("fileToUpload", new ClassPathResource(toBeSent.getName()));
        }
        return builder.build();
    }
}
