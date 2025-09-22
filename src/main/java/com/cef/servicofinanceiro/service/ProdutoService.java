package com.cef.servicofinanceiro.service;

import com.cef.servicofinanceiro.dto.ProdutoRequestDTO;
import com.cef.servicofinanceiro.dto.ProdutoResponseDTO;
import com.cef.servicofinanceiro.model.Produto;
import com.cef.servicofinanceiro.repository.ProdutoRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException; // Import correto
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional; // Import para o Optional


@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    @Inject
    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO requestDTO) {
        Produto produto = new Produto();
        produto.setNome(requestDTO.getNome());
        produto.setTaxaJurosAnual(requestDTO.getTaxaJurosAnual());
        produto.setPrazoMaximoMeses(requestDTO.getPrazoMaximoMeses());

        produtoRepository.persist(produto);
        return toResponseDTO(produto);
    }

    public List<ProdutoResponseDTO> listarTodos() {
        // 1. Pega a lista de entidades do banco de dados
        List<Produto> todosOsProdutos = produtoRepository.listAll();

        // 2. Cria uma nova lista vazia para guardar os DTOs
        List<ProdutoResponseDTO> dtos = new ArrayList<>();

        // 3. Itera sobre cada produto da lista original
        for (Produto produto : todosOsProdutos) {
            // 4. Converte cada produto para seu DTO correspondente
            ProdutoResponseDTO dto = toResponseDTO(produto);
            // 5. Adiciona o DTO na nova lista
            dtos.add(dto);
        }

        // 6. Retorna a lista de DTOs preenchida
        return dtos;
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = findProdutoById(id);
        return toResponseDTO(produto);
    }

    public Produto findProdutoById(Long id) {
        Optional<Produto> optionalProduto = produtoRepository.findByIdOptional(id);
        if (optionalProduto.isPresent()) {
            return optionalProduto.get();
        } else {
            throw new EntityNotFoundException("Produto não encontrado com o ID: " + id);
        }
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return ProdutoResponseDTO.builder()
                .id(produto.getId())
                .nome(produto.getNome())
                .taxaJurosAnual(produto.getTaxaJurosAnual())
                .prazoMaximoMeses(produto.getPrazoMaximoMeses())
                .build();
    }
}