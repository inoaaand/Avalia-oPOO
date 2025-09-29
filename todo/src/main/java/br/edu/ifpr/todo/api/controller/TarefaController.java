package br.edu.ifpr.todo.api.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import br.edu.ifpr.todo.api.dto.TarefaRequest;
import br.edu.ifpr.todo.api.dto.TarefaResponse;
import br.edu.ifpr.todo.domain.model.Tarefa;
import br.edu.ifpr.todo.domain.model.TodoStatus;
import br.edu.ifpr.todo.domain.service.TarefaService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/tarefa")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TarefaResponse criarViaPost(@RequestBody TarefaRequest dto) {
        Tarefa salvo = service.criar(dto);

        return new TarefaResponse(
                salvo.getId(),
                salvo.getNome(),
                salvo.getDescricao(),
                salvo.getStatus(),
                salvo.getDataCriacao(),
                salvo.getDataEntrega(),
                salvo.getImportante());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TarefaResponse> listarViaGet(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false, defaultValue = "A_FAZER") TodoStatus status,
            @RequestParam(required = false) Boolean importante,
            @RequestParam(required = false) LocalDate dataEntrega) {

        List<Tarefa> tarefas = service.listar(nome, status, importante, dataEntrega);

        List<TarefaResponse> resposta = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            TarefaResponse dto = new TarefaResponse(
                    tarefa.getId(),
                    tarefa.getNome(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getDataCriacao(),
                    tarefa.getDataEntrega(),
                    tarefa.getImportante());
            resposta.add(dto);
        }
        return resposta;
    }

    @GetMapping(params = "status")
    @ResponseStatus(HttpStatus.OK)
    public List<TarefaResponse> filtrarPorStatus(@RequestParam TodoStatus status) {
        List<Tarefa> tarefas = service.listar(null, status, null, null);
        List<TarefaResponse> resposta = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            TarefaResponse dto = new TarefaResponse(
                    tarefa.getId(),
                    tarefa.getNome(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getDataCriacao(),
                    tarefa.getDataEntrega(),
                    tarefa.getImportante());
            resposta.add(dto);
        }
        return resposta;
    }

    @GetMapping(params = "importante")
    @ResponseStatus(HttpStatus.OK)
    public List<TarefaResponse> filtrarImportantes(@RequestParam Boolean importante) {
        List<Tarefa> tarefas = service.listar(null, null, importante, null);
        List<TarefaResponse> resposta = new ArrayList<>();
        for (Tarefa tarefa : tarefas) {
            TarefaResponse dto = new TarefaResponse(
                    tarefa.getId(),
                    tarefa.getNome(),
                    tarefa.getDescricao(),
                    tarefa.getStatus(),
                    tarefa.getDataCriacao(),
                    tarefa.getDataEntrega(),
                    tarefa.getImportante());
            resposta.add(dto);
        }
        return resposta;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TarefaResponse buscarPorId(@PathVariable Long id) {
        Tarefa tarefa = service.buscarPorId(id);
        return new TarefaResponse(
                tarefa.getId(),
                tarefa.getNome(),
                tarefa.getDescricao(),
                tarefa.getStatus(),
                tarefa.getDataCriacao(),
                tarefa.getDataEntrega(),
                tarefa.getImportante());
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TarefaResponse atualizarParcial(@PathVariable Long id, @RequestBody TarefaRequest dto) {
        Tarefa atualizado = service.atualizar(id, dto);
        return new TarefaResponse(
                atualizado.getId(), atualizado.getNome(), atualizado.getDescricao(), atualizado.getStatus(),
                atualizado.getDataCriacao(), atualizado.getDataEntrega(), atualizado.getImportante());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}