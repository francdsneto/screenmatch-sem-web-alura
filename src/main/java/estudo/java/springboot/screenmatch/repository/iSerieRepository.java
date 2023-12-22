package estudo.java.springboot.screenmatch.repository;

import estudo.java.springboot.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface iSerieRepository extends JpaRepository<Serie,Long> {

}
