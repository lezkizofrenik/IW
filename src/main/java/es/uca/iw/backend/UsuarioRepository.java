package es.uca.iw.backend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findById(int id);

    Usuario findByUsername(String user);

    Usuario findByUsername(Usuario usuario);

    Usuario findByEmail(String email);

    @Transactional
    @Modifying
    @Query(value = "update Usuario u set u.id = :id where u.username like :username")
    int setId(@Param("id") Long id, @Param("username") String username);
}