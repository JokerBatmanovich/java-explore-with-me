package ru.prackticum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.prackticum.compilation.model.Compilation;
import ru.prackticum.compilation.reposiotry.CompilationRepository;
import ru.prackticum.exception.CompilationNotFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{
    private final CompilationRepository compilationRepository;

    @Override
    public Compilation save(Compilation compilation) {
        return compilationRepository.save(compilation);
    }

    @Override
    public String delete(Long compilationId) {
        compilationRepository.deleteById(compilationId);
        return "Подборка удалена";
    }

    @Override
    public Compilation getById(Long compilationId) {
        try {
            Compilation compilation = compilationRepository.getReferenceById(compilationId);
            System.out.println(compilation);
            return compilation;
        } catch (EntityNotFoundException e) {
            throw new CompilationNotFoundException(compilationId);
        }
    }

    @Override
    public List<Compilation> getByParams(Boolean pinned, Pageable pageable){
        if (pinned != null) {
            return compilationRepository.findAllByPinned(pinned, pageable);
        } else {
            return compilationRepository.findAll(pageable).toList();
        }
    }
}
