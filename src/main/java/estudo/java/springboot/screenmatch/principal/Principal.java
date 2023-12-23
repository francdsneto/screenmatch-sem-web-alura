package estudo.java.springboot.screenmatch.principal;

import estudo.java.springboot.screenmatch.model.*;
import estudo.java.springboot.screenmatch.repository.iSerieRepository;
import estudo.java.springboot.screenmatch.service.ConsumoAPI;
import estudo.java.springboot.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey="+System.getenv("OMDB_API_KEY");
    private final String SEASON = "&Season=";

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

    List<Serie> series = new ArrayList<>();

    private Optional<Serie> serieBuscada;

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
                4 - Buscar Serie por Título
                5 - Buscar Series por Ator
                6 - Buscar Top 5 Series
                7 - Buscar Series por Categoria
                8 - Desafio Total de Temporadas e Avaliação
                9 - Buscar episódios por trecho
                10 - Top 5 episódios por série
                11 - Buscar episódios a partir do ano de lançamento
                
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
                case 4: {
                    this.buscarSeriePorTitulo();
                    break;
                }
                case 5: {
                    this.buscarSeriesPorAtor();
                    break;
                }
                case 6: {
                    this.buscarTop5Series();
                    break;
                }
                case 7: {
                    this.buscarSeriesPorCategoria();
                    break;
                }
                case 8: {
                    this.buscarSeriesPorTotalDeTemporadasEAvaliacao();
                    break;
                }
                case 9: {
                    this.buscarEpisodiosPorTrecho();
                    break;
                }
                case 10: {
                    this.buscarTopEpisodiosPorSerie();
                    break;
                }
                case 11: {
                    this.buscarEpisodiosAPartirDeUmaData();
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

    private void buscarEpisodiosAPartirDeUmaData() {
        this.buscarSeriePorTitulo();
        if(serieBuscada.isPresent())
        {
            Serie serie = serieBuscada.get();
            System.out.println("Informa o ano que deseja verificar:");
            var anoLancamento = leitura.nextLine();
            List<Episodio> topEpisodios = this.serieRepository.buscarEpisodiosPorSerieEAno(serie,anoLancamento);
            System.out.println("Os episódios a partir do ano são:");
            topEpisodios.forEach(episodio -> {
                System.out.printf("Serie: %s - Temporada: %s - Episódio: %s - %s - Avaliação: %s - Ano Lançamento: %s \n", episodio.getSerie().getTitulo(), episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo(), episodio.getAvaliacao(), episodio.getDataLancamento());
            });
        }
    }

    private void buscarTopEpisodiosPorSerie() {
        this.buscarSeriePorTitulo();
        if(serieBuscada.isPresent())
        {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = this.serieRepository.topEpisodiosPorSerie(serie);
            System.out.println("Os top 5 episódios são:");
            topEpisodios.forEach(episodio -> {
                System.out.printf("Serie: %s - Temporada: %s - Episódio: %s - %s  Avaliação: %s \n", episodio.getSerie().getTitulo(), episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo(), episodio.getAvaliacao());
            });
        }
    }

    private void buscarEpisodiosPorTrecho() {
        System.out.println("Qual o nome do episódio que você deseja buscar?");
        var trechoNomeEpisodio = leitura.nextLine();
        List<Episodio> episodiosEncontrados = this.serieRepository.episodiosPorTrecho(trechoNomeEpisodio);
        System.out.println("Os episódios encontrados foram:");
        episodiosEncontrados.forEach(episodio -> {
            System.out.printf("Serie: %s - Temporada: %s - Episódio: %s - %s \n", episodio.getSerie().getTitulo(), episodio.getTemporada(), episodio.getNumeroEpisodio(), episodio.getTitulo());
        });
    }

    private void buscarSeriesPorTotalDeTemporadasEAvaliacao() {
        System.out.println("Digite a quantidade máxima de temporadas:");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Digite a avaliação mínima: ");
        var avaliacaoMinima = leitura.nextDouble();
        List<Serie> seriesRetornadas = this.serieRepository.seriesPorTemporadasEAvaliacaoJQPL(totalTemporadas,avaliacaoMinima);
                //this.serieRepository.findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas,avaliacaoMinima);
        System.out.println("As temporadas retornadas foram: ");
        seriesRetornadas.forEach(serie -> {
            System.out.println(serie.getTitulo().concat(" - Temporadas: ").concat(serie.getTotalTemporadas().toString()).concat(" - Avaliação: ").concat(serie.getAvaliacao().toString()));
        });
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Digite a categoria/genero:");
        var nomeGenero = leitura.nextLine();
        List<Serie> seriesPorCategoria = this.serieRepository.findByGenero(Categoria.fromPortugues(nomeGenero));
        System.out.printf("Lista das séries de %s: \n",nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {

        List<Serie> topSeries = this.serieRepository.findTop5ByOrderByAvaliacaoDesc();
        System.out.println("As 5 séries mais bem avaliadas são: ");
        topSeries.forEach(serie -> {
            System.out.println(serie.getTitulo().concat(" - Avaliação: ").concat(serie.getAvaliacao().toString()));
        });

    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator:");
        var nomeAtor = leitura.nextLine();
        System.out.println("Digite a avaliação minima:");
        var avaliacaoMinima = leitura.nextDouble();
        List<Serie> series = this.serieRepository.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor,avaliacaoMinima);
        System.out.printf("Séries onde %s trabalhou:\n", nomeAtor);
        series.forEach(serie -> {
            System.out.println(serie.getTitulo().concat(" - Avaliação: ").concat(serie.getAvaliacao().toString()));
        });
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Digite o título da série:");
        String nomeSerie = leitura.nextLine();
        serieBuscada = this.serieRepository.findByTituloContainingIgnoreCase(nomeSerie);
        if(serieBuscada.isPresent())
        {
            System.out.println("Dados da série:");
            System.out.println(serieBuscada.get());
        }
        else
        {
            System.out.println("Série não encontrada.");
        }
    }


    private void buscarSeries() {
        System.out.println("Digite o nome da série: ");
        var nomeSerie = this.leitura.nextLine();
        this.buscarSeriesNaWeb(nomeSerie);
    }

    private void listarSeriesBuscadas() {
        series = this.serieRepository.findAll();
        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarEpisodios() {

        this.listarSeriesBuscadas();
        System.out.println("Digite o nome da série: ");
        var nomeSerie = this.leitura.nextLine();

        Optional<Serie> serie = this.serieRepository.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent())
        {
            var serieEncontrada = serie.get();
            List<DadosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoAPI.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ","+") + SEASON + i + API_KEY);
                DadosTemporadas dadosTemporadas = conversor.obterDados(json, DadosTemporadas.class);
                temporadas.add(dadosTemporadas);
            }
            temporadas.stream().forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream().flatMap(d -> d.episodios().stream().map(de -> new Episodio(d.numero(), de))).collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            serieRepository.save(serieEncontrada);

        }
        else
        {
            System.out.println("Serie não encontrada.");
        }

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

