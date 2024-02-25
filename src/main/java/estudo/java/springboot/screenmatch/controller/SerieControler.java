package estudo.java.springboot.screenmatch.controller;

import estudo.java.springboot.screenmatch.dto.EpisodioDTO;
import estudo.java.springboot.screenmatch.dto.SerieDTO;
import estudo.java.springboot.screenmatch.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series")
public class SerieControler {

    @Autowired
    private SerieService serieService;

    @GetMapping("")
    public List<SerieDTO> obterSeries() {
        return serieService.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series() {
        return serieService.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterLancamentos() {
        return serieService.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterSeriePorId(@PathVariable Long id) {
        return serieService.obterSeriePorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        return serieService.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadaPorId(@PathVariable Long id, @PathVariable Long numero) {
        return serieService.obterTemporadaPorId(id,numero);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDTO> obterPorCategoria(@PathVariable String categoria) {
        return serieService.obterPorCategoria(categoria);
    }

}
