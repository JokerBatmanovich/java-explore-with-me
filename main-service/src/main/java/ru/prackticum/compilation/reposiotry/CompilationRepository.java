package ru.prackticum.compilation.reposiotry;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.prackticum.compilation.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long>,
                                                QuerydslPredicateExecutor<Compilation> {

    List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
