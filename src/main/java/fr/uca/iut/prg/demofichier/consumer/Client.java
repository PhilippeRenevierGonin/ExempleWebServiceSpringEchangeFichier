package fr.uca.iut.prg.demofichier.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;


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
			System.out.println(client.send("/upload2multi", "/file2b.txt").block());

			System.out.println("************************** envoi de plusieurs fichiers **************************************");

			System.out.println(client.sendMulti("/upload","/otherfile.txt", "/anotherfile.txt", "/application.properties").block());
			System.out.println(client.sendMulti("/upload2multi","/otherfile2.txt", "/anotherfile2.txt").block());

			System.out.println("************************** envoi de plusieurs fichiers + lang **************************************");
			System.out.println(client.sendMultiLang( "/upmulti", "fr", "/otherfile.txt", "/anotherfile.txt", "/application.properties").block());
			System.out.println(client.sendMultiLang( "/upmulti2", "fr", "/otherfile2.txt", "/anotherfile2.txt").block());
		};
	}

}
