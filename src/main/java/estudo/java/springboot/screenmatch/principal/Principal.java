package estudo.java.springboot.screenmatch.principal;

import estudo.java.springboot.screenmatch.model.DadosEpisodio;
import estudo.java.springboot.screenmatch.model.DadosSerie;
import estudo.java.springboot.screenmatch.model.DadosTemporadas;
import estudo.java.springboot.screenmatch.model.Episodio;
import estudo.java.springboot.screenmatch.service.ConsumoAPI;
import estudo.java.springboot.screenmatch.service.ConverteDados;
import org.springframework.util.comparator.Comparators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=567c7693";
    private final String SEASON = "&Season=";

    public void exibeMenu() {
        System.out.println("Digite o nome da série:");
        var nomeSerie = leitura.nextLine();
        String endereco = ENDERECO + nomeSerie.replace(" ","+") + API_KEY;
        var consumoAPI = new ConsumoAPI();
        var json = consumoAPI.obterDados(endereco);
        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporadas> temporadas = new ArrayList<>();

		for (int i = 1; i <= dados.totalTemporadas(); i++) {
			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") + SEASON + i + API_KEY);
			DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporadas);
		}

		temporadas.forEach(System.out::println);

//        for (int i = 0; i < temporadas.size(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        /**
         * Exemplo de lambda
         */
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //    /**
//     * Exemplo de Stream
//     */
//    List<String> nomes = Arrays.asList("José","Maria","João","Pedro","Paulo","Lucas");
//
///**
// * O stream nada mais é que um fluxo de dados que permite a manipulação desses dados através de dois
// * tipos de funções, as intermediárias e finals.
// *
// * As funções intermediarias retornam um novo fluxo, o que permite encadeá-las.
// *
// * As funções finais são as que finalizam, não gerando mais um fluxo como retorno.
// *
// */
//        nomes.stream()
//                .sorted()
//                .limit(3)
//                .filter(n -> n.startsWith("J"))
//                .map(n -> n.toUpperCase())
//                .forEach(System.out::println);


        /**
         * FlatMap obtem uma Stream de cada elemento contido na primeira Stream e retorna a concatenação de
         * todos eles em uma nova stream
         */
//        List<DadosEpisodio> dadosEpisodios =  temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
                //.toList() ToList retorna uma lista imutável, ou seja, não pode ser alterada.

        System.out.println("Top 5 Episódios:");
//        dadosEpisodios
//                .stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .limit(5)
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream().map(de -> new Episodio(t.numero(),de))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        System.out.println("A partir de que ano você deseja ver os episódios?");
        var ano = leitura.nextInt();
        leitura.nextLine();

        LocalDate dataBusca = LocalDate.of(ano,1,1);

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios
                .stream()
                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                        "Temporada: " + e.getTemporada() +
                                " Episódios: " + e.getNumeroEpisodio() +
                                " Data Lançamento " + e.getDataLancamento().format(df)
                ));



    }

}

