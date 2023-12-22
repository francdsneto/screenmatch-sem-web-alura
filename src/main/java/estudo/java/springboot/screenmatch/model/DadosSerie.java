package estudo.java.springboot.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Diferença entre @JsonAlias e @JsonProperty
 *
 * O @JsonAlias ele desserializa o atributo Title em titulo, porém ao serializar, ele vai gerar um Json com o atributo titulo, e não Title.
 * O @JsonProperty funciona em ambas as direções, serialização e desserialização, então ele vai desserlizar o atributo Title como título, mas
 * ao serializar ele gerará o Json com o atributo Title, e não titulo.
 *
 * Com o @JsonAlias é possível passar uma lista de nomes para ele encontrar e desserializar no titulo,
 * como por exemplo @JsonAlias({ "Title","titulo" })
 *
 * @JsonIgnoreProperties é usado para evitar que o Jackson tente converter as propriedades não mapeadas
 *
 *
 * @param titulo
 * @param totalTemporadas
 * @param avaliacao
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosSerie(@JsonAlias("Title") String titulo,
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao,
                         @JsonAlias("Genre") String genero,
                         @JsonAlias("Actors") String atores,
                         @JsonAlias("Poster") String poster,
                         @JsonAlias("Plot") String sinopse) {
}
