package br.edu.ifpr.todo.domain.service;

import br.edu.ifpr.todo.domain.model.Tarefa;
import br.edu.ifpr.todo.domain.model.TodoStatus;
import br.edu.ifpr.todo.api.dto.TarefaRequest;
import br.edu.ifpr.todo.api.exception.ResourceNotFoundException;
import br.edu.ifpr.todo.domain.repository.TarefaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class TarefaService {
    private final TarefaRepository repo;

    public TarefaService(TarefaRepository repo) {
        this.repo = repo;
    }

    public List<Tarefa> listar(String q, TodoStatus status, Boolean importante, LocalDate ate) {

        if (q != null && !q.isBlank()) {
            return repo.findByNomeContainingIgnoreCase(q);
        }
        if (status != null && Boolean.TRUE.equals(importante)) {
            return repo.findByStatusAndImportanteTrue(status);
        }
        if (status != null) {
            return repo.findByStatus(status);
        }
        if (Boolean.TRUE.equals(importante)) {
            return repo.findByImportanteTrue();
        }
        if (ate != null) {
            return repo.findByDataEntregaBefore(ate.plusDays(1));
        }
        return repo.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tarefa id: " + id + " não encontrada"));
    }

    @Transactional
    public Tarefa criar(TarefaRequest dto) {
        Tarefa t = new Tarefa();
        t.setNome(dto.getNome());
        t.setDescricao(dto.getDescricao());
        t.setStatus(dto.getStatus() != null ? dto.getStatus() : TodoStatus.A_FAZER);
        t.setDataCriacao(LocalDate.now());
        t.setDataEntrega(dto.getDataEntrega());
        t.setImportante(dto.getImportante() != null ? dto.getImportante() : false);
        return repo.save(t);
    }

    @Transactional
    public Tarefa atualizar(Long id, TarefaRequest dto) {
        Tarefa t = buscarPorId(id);

        if (dto.getNome() != null) {
            t.setNome(dto.getNome());
        }
        if (dto.getDescricao() != null) {
            t.setDescricao(dto.getDescricao());
        }
        if (dto.getStatus() != null) {
            t.setStatus(dto.getStatus());
        }
        if (dto.getDataEntrega() != null) {
            t.setDataEntrega(dto.getDataEntrega());
        }
        if (dto.getImportante() != null) {
            t.setImportante(dto.getImportante());
        }
        return repo.save(t);
    }

    @Transactional
    public Tarefa atualizarStatus(Long id, TodoStatus novoStatus) {
        Tarefa t = buscarPorId(id);
        t.setStatus(novoStatus);
        return repo.save(t);
    }

    @Transactional
    public Tarefa marcarImportante(Long id, boolean importante) {
        Tarefa t = buscarPorId(id);
        t.setImportante(importante);
        return repo.save(t);
    }

    @Transactional
    public void remover(Long id) {
        Tarefa t = buscarPorId(id);
        repo.delete(t);
    }
}