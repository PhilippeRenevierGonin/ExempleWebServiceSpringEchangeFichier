package fr.uca.iut.prg.demofichier.server;


import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.codec.multipart.FilePart;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;


@RestController
public class UploadController {
    private final Path rootLocation = Paths.get("."); // endroit où on veut recopier le fichier


    @GetMapping("hello")
    public String hello() {return "hello !";}


    /*
    pour spring mvc
    		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-web</artifactId>
		</dependency>
     */
    /*
    @PostMapping("/uploadRest")
    public String uploadFile(@RequestParam("fileToUpload") MultipartFile file) throws Exception {
        System.out.println("upload2 ... by RestController");
        String result = "problème...";

        if (file.isEmpty()) {
            throw new Exception("fichier vide... non sauvegardable");
        }

        Path destinationFile = rootLocation.resolve(Paths.get(file.getOriginalFilename())).normalize().toAbsolutePath();

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);  // recopier du fichier temporaire reçu
            result = destinationFile.toString();
        }

        return result;
    }

    */

    @PostMapping("upload2")
    public Mono<String> upload(@RequestPart("fileToUpload") Mono<FilePart> file){
        return  file.flatMap(fp -> fp.transferTo(Paths.get(fp.filename())).then(Mono.just("upload fait... "+fp.filename())));

    }


    @PostMapping("upload2multi")
    public Flux<String> upload(@RequestPart("fileToUpload") Flux<FilePart> file){
        return  file.flatMap(fp -> fp.transferTo(Paths.get(fp.filename())).then(Mono.just("upload fait... "+fp.filename())));

    }



    @PostMapping("upmulti2")
    public Flux<String> uploadMulti(@RequestPart("fileToUpload") Flux<FilePart> file, @RequestPart("lang") String lang){
        File langDir = new File(lang);
        if (langDir.exists()) {
            if (! langDir.isDirectory()) return Flux.just("la langue spécifiée pose soucis");
        }
        else {
            try {
                Files.createDirectories(Paths.get(lang));
            } catch (IOException e) {
                return  Flux.just(Arrays.toString(e.getStackTrace()));
            }
        }
        return  file.flatMap(fp -> fp.transferTo(Paths.get(lang+"/"+fp.filename())).then(Mono.just("upload fait... "+lang+"/"+fp.filename()+"\n")));

    }
}
