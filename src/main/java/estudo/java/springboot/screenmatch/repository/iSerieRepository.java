package estudo.java.springboot.screenmatch.repository;

import estudo.java.springboot.screenmatch.model.Categoria;
import estudo.java.springboot.screenmatch.model.Episodio;
import estudo.java.springboot.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface iSerieRepository extends JpaRepository<Serie,Long> {

    Optional<Serie> findByTituloContainingIgnoreCase(String titulo);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliação);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer temporadas, Double avaliacao);

    /**
     * Exemplo de uso de NativeQuery
     * @param temporadas
     * @param avaliacao
     * @return
     */
    @Query(value = "SELECT * FROM series WHERE series.total_temporadas <= :temporadas AND series.avaliacao >= :avaliacao", nativeQuery = true)
    List<Serie> seriesPorTemporadasEAvaliacao(Integer temporadas, Double avaliacao);

    @Query("SELECT s FROM Serie AS s WHERE s.totalTemporadas <= :temporadas AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadasEAvaliacaoJQPL(Integer temporadas, Double avaliacao);

    /**
     * ILIKE = like ignoring case
     * @param trechoNomeEpisodio
     * @return
     */
    @Query("SELECT e FROM Serie AS s LEFT JOIN s.episodios AS e WHERE e.titulo ILIKE %:trechoNomeEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoNomeEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios AS e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios AS e WHERE s = :serie AND YEAR(e.dataLancamento) >= :ano")
    List<Episodio> buscarEpisodiosPorSerieEAno(Serie serie, String ano);

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.dataLancamento) DESC LIMIT 5")
    List<Serie> encontrarEpisodiosMaisRecentes();

    @Query("SELECT e FROM Serie s LEFT JOIN s.episodios e WHERE s.id = :id AND e.temporada = :numero")
    List<Episodio> obterEpisodiosPorTemporada(Long id, Long numero);
}
