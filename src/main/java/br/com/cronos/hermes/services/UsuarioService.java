package br.com.cronos.hermes.services;

import br.com.cronos.hermes.dto.UsuarioDto;
import br.com.cronos.hermes.entities.Usuario;
import br.com.cronos.hermes.exceptions.BusinessNotFoundException;
import br.com.cronos.hermes.repositories.UsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService {

    public static final String USUARIO_NOT_FOUND = "Não foi possível encontrar o usuario com o id ";
    
    
    private final Logger logger = LoggerFactory.getLogger(UsuarioService.class);
    private UsuarioRepository usuarioRepository;
    
    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
    
    @Transactional(readOnly = true)
    public Page<UsuarioDto> listarTodos(Pageable pageable) {
    logger.info("SERVICE - Listagem de usuarios");
    List<UsuarioDto> usuariosDto = usuarioRepository.findAll(pageable).getContent().stream().map(Usuario::toDto).toList();
        Page<UsuarioDto> usuariosPage = new PageImpl<>(usuariosDto, pageable, usuariosDto.size());
        return usuariosPage;
    }

    @Transactional(readOnly = true)
    public UsuarioDto buscarPorId(UUID id) {
        logger.info("SERVICE - Buscar usuario com o id {}", id);
        return usuarioRepository.findById(id).orElseThrow(() -> new BusinessNotFoundException(USUARIO_NOT_FOUND + id)).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UsuarioDto criarNovo(UsuarioDto usuarioDto) {
        logger.info("SERVICE - Criando um novo usuario {}", usuarioDto);
        Usuario usuario = usuarioDto.toEntity();
        return usuarioRepository.save(usuario).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public UsuarioDto atualizar(UUID id, UsuarioDto usuarioDto) {
        logger.info("SERVICE - Atualizando o usuario com o id {} com as seguintes informacoes {}", id, usuarioDto);
        Usuario usuarioRecuperado = verificarSeUsuarioExiste(id);
            usuarioRecuperado.setNome(usuarioDto.getNome());
            usuarioRecuperado.setEmail(usuarioDto.getEmail());
            usuarioRecuperado.setSenha(usuarioDto.getSenha());
    
        return usuarioRepository.save(usuarioRecuperado).toDto();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void deletar(UUID id) {
        logger.info("SERVICE - Deletando um usuario com o id {}", id);
        Usuario usuario = verificarSeUsuarioExiste(id);
        usuarioRepository.delete(usuario);
    }

    private Usuario verificarSeUsuarioExiste(UUID id) {
        logger.info("SERVICE - Verificando se usuario existe com o id {}", id);
        return usuarioRepository.findById(id).orElseThrow(() -> new BusinessNotFoundException(USUARIO_NOT_FOUND + id));
    }
}
