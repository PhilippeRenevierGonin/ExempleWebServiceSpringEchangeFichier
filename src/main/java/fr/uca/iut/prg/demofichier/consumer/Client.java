package fr.uca.iut.prg.demofichier.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.util.concurrent.TimeUnit;


@SpringBootApplication
public class Client {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Client.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean
	public CommandLineRunner plusieursRequetes(@Autowired FileUploader client) {
		return args -> {
			System.out.println("************************** requetes synchrones **************************************");
			System.out.println("************************** envoi d'un fichier **************************************");

			System.out.println(client.send("/upload", "/file.txt").block());
			System.out.println(client.send("/upload2", "/file2.txt").block());

			System.out.println("************************** envoi de plusieurs fichiers **************************************");

			client.sendMulti("/upload","/otherfile.txt", "/anotherfile.txt", "/application.properties").block().bodyToFlux(String.class).subscribe(
					s -> System.out.println("cas envoi de plusieurs fichiers Router/Handler : " + s)
			);
			client.sendMulti("/upload2multi", "/otherfile2.txt", "/anotherfile2.txt").block().bodyToFlux(String.class).subscribe(
			 		s -> System.out.println("cas envoi de plusieurs fichiers rest ctrl : " + s)
			);

			System.out.println("************************** envoi de plusieurs fichiers + lang **************************************");
			client.sendMultiLang( "/upmulti", "azerty", "/otherfile.txt", "/anotherfile.txt", "/application.properties").block().bodyToFlux(String.class).subscribe(
				s -> System.out.println("cas multi + lang 1, /upmulti et azerty : " + s)
			);
			client.sendMultiLang( "/upmulti", "aa", "/otherfile.txt", "/anotherfile.txt", "/application.properties").block().bodyToFlux(String.class).subscribe(
					s -> System.out.println("cas multi + lang 2, /upmulti et aa : " + s)
			);
			client.sendMultiLang( "/upmulti2", "en", "/otherfile2.txt", "/anotherfile2.txt").block().bodyToFlux(String.class).subscribe(
					s -> System.out.println("cas multi + lang 3, /upmulti2 et en : " + s)
			);
			client.sendMultiLang( "/upmulti2", "bb", "/otherfile2.txt", "/anotherfile2.txt").block().bodyToFlux(String.class).subscribe(
					s -> System.out.println("cas multi + lang 4, /upmulti2 et bb : " + s)
			);
		};
	}

}
