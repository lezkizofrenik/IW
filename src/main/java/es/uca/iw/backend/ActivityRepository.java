package es.uca.iw.backend;

import es.uca.iw.backend.Activity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findById(int id);
    List<Activity> findByDescription(String s);
    List<Activity> findByDescriptionStartsWithIgnoreCase(String s);
    List<Activity> findByNameStartsWithIgnoreCase(String s);

}
