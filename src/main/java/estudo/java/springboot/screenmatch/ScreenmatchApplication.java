package estudo.java.springboot.screenmatch;

import estudo.java.springboot.screenmatch.model.DadosEpisodio;
import estudo.java.springboot.screenmatch.model.DadosSerie;
import estudo.java.springboot.screenmatch.model.DadosTemporadas;
import estudo.java.springboot.screenmatch.principal.Principal;
import estudo.java.springboot.screenmatch.service.ConsumoAPI;
import estudo.java.springboot.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Principal principal = new Principal();
		principal.exibeMenu();

	}

}
