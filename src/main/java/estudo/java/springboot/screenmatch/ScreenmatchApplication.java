package estudo.java.springboot.screenmatch;

import estudo.java.springboot.screenmatch.model.DadosSerie;
import estudo.java.springboot.screenmatch.service.ConsumoAPI;
import estudo.java.springboot.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		var consumoApi = new ConsumoAPI();
		/**
		 * Endereco
		 * "https://www.omdbapi.com/?t=loki&apikey=567c7693"
		 */
		String endereco = "https://www.omdbapi.com/?t=loki&apikey=567c7693";
		var json = consumoApi.obterDados(endereco);
		System.out.println(json);

		ConverteDados conversor = new ConverteDados();

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}

}
