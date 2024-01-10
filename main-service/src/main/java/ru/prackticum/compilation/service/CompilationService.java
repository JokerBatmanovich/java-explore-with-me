package ru.prackticum.compilation.service;

import org.springframework.data.domain.Pageable;
import ru.prackticum.compilation.model.Compilation;

import java.util.List;

public interface CompilationService {

    Compilation save(Compilation compilation);

    String delete(Long compilationId);

    Compilation getById(Long compilationId);

    List<Compilation> getByParams(Boolean pinned, Pageable pageable);
}
