package estudo.java.springboot.screenmatch.principal;

import estudo.java.springboot.screenmatch.model.DadosSerie;
import estudo.java.springboot.screenmatch.model.DadosTemporadas;
import estudo.java.springboot.screenmatch.model.Serie;
import estudo.java.springboot.screenmatch.repository.iSerieRepository;
import estudo.java.springboot.screenmatch.service.ConsumoAPI;
import estudo.java.springboot.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey="+System.getenv("OMDB_API_KEY");
    private final String SEASON = "&Season=";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    private iSerieRepository serieRepository;

    public Principal(iSerieRepository serieRepository) {
        this.serieRepository = serieRepository;
    }

    public void exibirMenu() {

        var opcao = -1;

        var menu = """
                1 - Buscar Séries
                2 - Buscar Episódios
                3 - Listar Series Buscadas
                
                0 - Sair
                """;

        while (opcao != 0) {

            System.out.println(menu);

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1: {
                    this.buscarSeries();
                    break;
                }
                case 2: {
                    this.buscarEpisodios();
                    break;
                }
                case 3: {
                    this.listarSeriesBuscadas();
                    break;
                }
                case 0: {
                    System.out.println("Bye bye!");
                    break;
                }
                default: {
                    System.out.println("Opção inválida");
                    break;
                }
            }

        }

    }

    private void listarSeriesBuscadas() {
        List<Serie> series = this.serieRepository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSeries() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = this.leitura.nextLine();
        this.buscarSeriesNaWeb(nomeSerie);
    }

    private void buscarEpisodios() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = this.leitura.nextLine();
        DadosSerie dadosSerie = this.getDadosSerie(nomeSerie);
        List<DadosTemporadas> temporadas = new ArrayList<>();
		for (int i = 1; i <= dadosSerie.totalTemporadas(); i++) {
			var json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") + SEASON + i + API_KEY);
			DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
			temporadas.add(dadosTemporadas);
		}
        temporadas.stream().forEach(System.out::println);
    }

    private void buscarSeriesNaWeb(String nomeSerie) {
        DadosSerie dados = this.getDadosSerie(nomeSerie);
        Serie serie = new Serie(dados);
        serieRepository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(String nomeSerie) {
        var json = this.consumoAPI.obterDados(ENDERECO.concat(nomeSerie.replace(" ","+")).concat(API_KEY));
        return this.conversor.obterDados(json, DadosSerie.class);
    }


    /**
     * Mantendo código da aula anterior apenas para lembrança futura do que foi passado
     */

//    public void exibeMenu() {
//        System.out.println("Digite o nome da série:");
//        var nomeSerie = leitura.nextLine();
//        String endereco = ENDERECO + nomeSerie.replace(" ","+") + API_KEY;
//        var consumoAPI = new ConsumoAPI();
//        var json = consumoAPI.obterDados(endereco);
//        DadosSerie dados = conversor.obterDados(json,DadosSerie.class);
//        System.out.println(dados);
//
//        List<DadosTemporadas> temporadas = new ArrayList<>();
//
//		for (int i = 1; i <= dados.totalTemporadas(); i++) {
//			json = consumoAPI.obterDados(ENDERECO + nomeSerie.replace(" ","+") + SEASON + i + API_KEY);
//			DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
//			temporadas.add(dadosTemporadas);
//		}
//
//		temporadas.forEach(System.out::println);
//
////        for (int i = 0; i < temporadas.size(); i++) {
////            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
////            for (int j = 0; j < episodiosTemporada.size(); j++) {
////                System.out.println(episodiosTemporada.get(j).titulo());
////            }
////        }
//
//        /**
//         * Exemplo de lambda
//         */
////        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
//
//        //    /**
////     * Exemplo de Stream
////     */
////    List<String> nomes = Arrays.asList("José","Maria","João","Pedro","Paulo","Lucas");
////
/////**
//// * O stream nada mais é que um fluxo de dados que permite a manipulação desses dados através de dois
//// * tipos de funções, as intermediárias e finals.
//// *
//// * As funções intermediarias retornam um novo fluxo, o que permite encadeá-las.
//// *
//// * As funções finais são as que finalizam, não gerando mais um fluxo como retorno.
//// *
//// */
////        nomes.stream()
////                .sorted()
////                .limit(3)
////                .filter(n -> n.startsWith("J"))
////                .map(n -> n.toUpperCase())
////                .forEach(System.out::println);
//
//
//        /**
//         * FlatMap obtem uma Stream de cada elemento contido na primeira Stream e retorna a concatenação de
//         * todos eles em uma nova stream
//         */
//        List<DadosEpisodio> dadosEpisodios =  temporadas.stream()
//                .flatMap(t -> t.episodios().stream())
//                .collect(Collectors.toList());
//                //.toList() ToList retorna uma lista imutável, ou seja, não pode ser alterada.
//
////        System.out.println("Top 10 Episódios:");
////        dadosEpisodios
////                .stream()
////                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
////                .peek(e -> System.out.println("Primeiro filtro (N/A) " + e))
////                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
////                .peek(e -> System.out.println("Segundo filtro Soted by Avaliação " + e))
////                .limit(10)
////                .peek(e -> System.out.println("Terceiro filtro limit 10 " + e))
////                .map(e -> e.titulo().toUpperCase())
////                .peek(e -> System.out.println("Quarto filtro Map ToUppecase " + e))
////                .forEach(System.out::println);
//
//        List<Episodio> episodios = temporadas.stream()
//                .flatMap(t -> t.episodios().stream().map(de -> new Episodio(t.numero(),de))
//                ).collect(Collectors.toList());
//
//        episodios.forEach(System.out::println);
//
////        System.out.println("Digite o trecho do título de um episodio que você queira encontrar:");
////
////        var trechoTitulo = leitura.nextLine();
////
////        Optional<Episodio> episodioBuscado = episodios.stream()
////                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
////                .findFirst();
////
////        if(episodioBuscado.isPresent())
////        {
////            System.out.println("Episódio encontrado!");
////            System.out.println("Temporada: "+ episodioBuscado.get().getTemporada());
////        } else {
////            System.out.println("Episódio não encontrado!");
////        }
//
////        System.out.println("A partir de que ano você deseja ver os episódios?");
////        var ano = leitura.nextInt();
////        leitura.nextLine();
////
////        LocalDate dataBusca = LocalDate.of(ano,1,1);
////
////        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
////
////        episodios
////                .stream()
////                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
////                .forEach(e -> System.out.println(
////                        "Temporada: " + e.getTemporada() +
////                                " Episódios: " + e.getNumeroEpisodio() +
////                                " Data Lançamento " + e.getDataLancamento().format(df)
////                ));
//
//        Map<Integer,Double> avaliacoesPorTemporada = episodios.stream()
//                .filter(episodio -> episodio.getAvaliacao() > 0.0)
//                .collect(Collectors.groupingBy(Episodio::getTemporada,
//                        Collectors.averagingDouble(Episodio::getAvaliacao)));
//
//        System.out.println(avaliacoesPorTemporada);
//
//        DoubleSummaryStatistics est = episodios.stream()
//                .filter(episodio -> episodio.getAvaliacao() > 0.0)
//                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
//
//        System.out.println("Media: "+est.getAverage());
//        System.out.println("Pior Episódio: "+est.getMin());
//        System.out.println("Melhor Episódio: "+est.getMax());
//        System.out.println("Quantidade de episódios avaliados: "+est.getCount());
//    }



}

