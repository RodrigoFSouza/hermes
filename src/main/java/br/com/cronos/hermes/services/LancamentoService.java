package br.com.cronos.hermes.services;


import br.com.cronos.hermes.dto.LancamentoDto;
import br.com.cronos.hermes.entities.Lancamento;
import br.com.cronos.hermes.exceptions.BusinessNotFoundException;
import br.com.cronos.hermes.repositories.LancamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class LancamentoService {

    public static final String LANCAMENTO_NOT_FOUND = "Não foi possível encontrar o lancamento com o id ";
    
    
    private final Logger logger = LoggerFactory.getLogger(LancamentoService.class);
    private LancamentoRepository lancamentoRepository;
    
    public LancamentoService(LancamentoRepository lancamentoRepository) {
        this.lancamentoRepository = lancamentoRepository;
    }
    
    @Transactional(readOnly = true)
    public Page<LancamentoDto> listarTodos(Pageable pageable) {
    logger.info("SERVICE - Listagem de lancamentos");
    List<LancamentoDto> lancamentosDto = lancamentoRepository.findAll(pageable).getContent().stream().map(Lancamento::toDto).toList();
        Page<LancamentoDto> lancamentosPage = new PageImpl<>(lancamentosDto, pageable, lancamentosDto.size());
        return lancamentosPage;
    }

    @Transactional(readOnly = true)
    public LancamentoDto buscarPorId(UUID id) {
        logger.info("SERVICE - Buscar lancamento com o id {}", id);
        return lancamentoRepository.findById(id).orElseThrow(() -> new BusinessNotFoundException(LANCAMENTO_NOT_FOUND + id)).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public LancamentoDto criarNovo(LancamentoDto lancamentoDto) {
        logger.info("SERVICE - Criando um novo lancamento {}", lancamentoDto);
        Lancamento lancamento = lancamentoDto.toEntity();
        lancamento.setDataCadastro(LocalDateTime.now());
        return lancamentoRepository.save(lancamento).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public LancamentoDto atualizar(UUID id, LancamentoDto lancamentoDto) {
        logger.info("SERVICE - Atualizando o lancamento com o id {} com as seguintes informacoes {}", id, lancamentoDto);
        Lancamento lancamentoRecuperado = verificarSeLancamentoExiste(id);
        lancamentoRecuperado.setMes(lancamentoDto.getMes());
        lancamentoRecuperado.setAno(lancamentoDto.getAno());
        lancamentoRecuperado.setUsuario(lancamentoDto.getUsuario());
        lancamentoRecuperado.setValor(lancamentoDto.getValor());
    
        return lancamentoRepository.save(lancamentoRecuperado).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletar(UUID id) {
        logger.info("SERVICE - Deletando um lancamento com o id {}", id);
        Lancamento lancamento = verificarSeLancamentoExiste(id);
        lancamentoRepository.delete(lancamento);
    }

    private Lancamento verificarSeLancamentoExiste(UUID id) {
        logger.info("SERVICE - Verificando se lancamento existe com o id {}", id);
        return lancamentoRepository.findById(id).orElseThrow(() -> new BusinessNotFoundException(LANCAMENTO_NOT_FOUND + id));
    }
}
