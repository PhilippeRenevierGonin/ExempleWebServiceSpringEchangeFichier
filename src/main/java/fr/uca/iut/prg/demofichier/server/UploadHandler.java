package fr.uca.iut.prg.demofichier.server;


import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Le compossant "handler" qui propose 5 méthodes pour 5 traitements de requêtes
 */
@Component
public class UploadHandler {
	public Mono<ServerResponse> upload(ServerRequest serverRequest) {
		Flux<String> recopieDuFichier = serverRequest.multipartData()
				.map(parts -> parts.get("fileToUpload"))
				.flatMapMany(Flux::fromIterable)
				.cast(FilePart.class)
				.flatMap(fp -> fp.transferTo(Paths.get(fp.filename())).then(Mono.just("upload fait... "+fp.filename()+" <br />\n")));
		return ServerResponse.ok().body(recopieDuFichier, String.class);
	}

	public Mono<ServerResponse> uploadmulti(ServerRequest serverRequest) {
		String lang = serverRequest.queryParam("lang").orElse("fr");
		File langDir = new File(lang);
		if (langDir.exists()) {
			if (! langDir.isDirectory()) return ServerResponse.badRequest().body(BodyInserters.fromValue("la langue spécifiée pose soucis"));
		}
		else {
			try {
				Files.createDirectories(Paths.get(lang));
			} catch (IOException e) {
				return ServerResponse.badRequest().body(BodyInserters.fromValue(e.getStackTrace()));
			}
		}

		Flux<String> recopieDuFichier = serverRequest.multipartData()
				.map(parts -> parts.get("fileToUpload"))
				.flatMapMany(Flux::fromIterable)
				.cast(FilePart.class)
				.flatMap(fp -> fp.transferTo(Paths.get(lang+"/"+fp.filename())).then(Mono.just("upload fait... "+lang+"/"+fp.filename()+"\n")));
		return ServerResponse.ok().body(recopieDuFichier, String.class);

	}
}
