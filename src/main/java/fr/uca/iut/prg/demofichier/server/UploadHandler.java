package fr.uca.iut.prg.demofichier.server;


import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
