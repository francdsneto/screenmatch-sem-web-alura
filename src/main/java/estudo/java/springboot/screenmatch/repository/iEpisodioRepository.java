package estudo.java.springboot.screenmatch.repository;

import estudo.java.springboot.screenmatch.model.Episodio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iEpisodioRepository extends JpaRepository<Episodio,Long> {

}
