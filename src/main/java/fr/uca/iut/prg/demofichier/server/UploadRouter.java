package fr.uca.iut.prg.demofichier.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration(proxyBeanMethods = false)
public class UploadRouter {

	/**
	 * déclaration des chemins exposés par router (ici) / handler (le paramètre)
	 * @param uploadHandler le handler qui propose des méthodes pour traiter les requêtes
	 * @return
	 */
	@Bean
	public RouterFunction<ServerResponse> route(UploadHandler uploadHandler) {

		return RouterFunctions
			.route(POST("/upload").and(accept(MediaType.MULTIPART_FORM_DATA)), uploadHandler::upload);

	}
}
