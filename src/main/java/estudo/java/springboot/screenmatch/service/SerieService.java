package estudo.java.springboot.screenmatch.service;

import estudo.java.springboot.screenmatch.dto.EpisodioDTO;
import estudo.java.springboot.screenmatch.dto.SerieDTO;
import estudo.java.springboot.screenmatch.model.Categoria;
import estudo.java.springboot.screenmatch.model.Serie;
import estudo.java.springboot.screenmatch.repository.iSerieRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SerieService {

    @Autowired
    private iSerieRepository repositorio;

    private List<SerieDTO> converteParaDTO(@NotNull List<Serie> series) {
        return series.stream()
                .map(s -> new SerieDTO(s.getId(),
                        s.getTitulo(),
                        s.getTotalTemporadas(),
                        s.getAvaliacao(),
                        s.getGenero(),
                        s.getAtores(),
                        s.getPoster(),
                        s.getSinopse()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries() {
        return this.converteParaDTO(repositorio.findAll(Sort.by(Sort.Direction.ASC,"titulo")));
    }

    public List<SerieDTO> obterTop5Series() {
        return this.converteParaDTO(repositorio.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return this.converteParaDTO(repositorio.encontrarEpisodiosMaisRecentes());
    }

    public SerieDTO obterSeriePorId(Long id) {
        Optional<Serie> optionalSerie = repositorio.findById(id);
        if(optionalSerie.isPresent())
        {
            Serie s = optionalSerie.get();
            return new SerieDTO(s.getId(),
                    s.getTitulo(),
                    s.getTotalTemporadas(),
                    s.getAvaliacao(),
                    s.getGenero(),
                    s.getAtores(),
                    s.getPoster(),
                    s.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> optionalSerie = repositorio.findById(id);
        if(optionalSerie.isPresent())
        {
            Serie s = optionalSerie.get();
            return s.getEpisodios().stream().map(e -> new EpisodioDTO(e.getTemporada(),e.getTitulo(),e.getNumeroEpisodio())).toList();
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadaPorId(Long id, Long numero) {
        return repositorio.obterEpisodiosPorTemporada(id,numero)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),e.getTitulo(),e.getNumeroEpisodio()))
                .toList();
    }

    public List<SerieDTO> obterPorCategoria(String categoria) {
        return this.converteParaDTO(this.repositorio.findByGenero(Categoria.fromPortugues(categoria)));
    }

    public List<EpisodioDTO> obterTop5EpisodiosDaSerie(Long id) {
        return this.repositorio
                .obterTop5EpisodiosDaSerie(id)
                .stream()
                .map(e -> new EpisodioDTO(e.getTemporada(),e.getTitulo(),e.getNumeroEpisodio()))
                .toList();
    }
}
