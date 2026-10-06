package br.com.autoescola.api.exception;

import br.com.autoescola.api.adapter.out.integration.viacep.ViaCepIndisponivelException;
import br.com.autoescola.api.adapter.out.integration.viacep.ViaCepNotFoundException;
import br.com.autoescola.api.exception.type.AlunoNotFoundException;
import br.com.autoescola.api.exception.type.ConflitoException;
import br.com.autoescola.api.exception.type.InstrucaoNotFoundException;
import br.com.autoescola.api.exception.type.InstrutorNotFoundException;
import br.com.autoescola.api.exception.type.UsuarioNotFoundException;
import br.com.autoescola.api.exception.type.ValidacaoException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({EntityNotFoundException.class, AlunoNotFoundException.class, InstrutorNotFoundException.class,
            InstrucaoNotFoundException.class, UsuarioNotFoundException.class, ViaCepNotFoundException.class})
    public ResponseEntity<DadosMessage> tratarNotFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosBadRequest>> tratarBadRequest(MethodArgumentNotValidException e) {
        List<DadosBadRequest> erros = e.getBindingResult().getFieldErrors().stream()
                .map(DadosBadRequest::new).toList();
        return ResponseEntity.badRequest().body(erros);
    }

    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity<DadosMessage> tratarValidacao(ValidacaoException e) {
        return ResponseEntity.badRequest().body(new DadosMessage(e.getMessage()));
    }

    @ExceptionHandler({ConflitoException.class, DataIntegrityViolationException.class})
    public ResponseEntity<DadosMessage> tratarConflito(RuntimeException e) {
        String mensagem = e instanceof ConflitoException ? e.getMessage() : "Já existe um registro com os mesmos dados únicos.";
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new DadosMessage(mensagem));
    }

    @ExceptionHandler(ViaCepIndisponivelException.class)
    public ResponseEntity<DadosMessage> tratarServicoExternoIndisponivel(ViaCepIndisponivelException e) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(new DadosMessage(e.getMessage()));
    }

    private record DadosBadRequest(String campo, String mensagem) {
        public DadosBadRequest(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record DadosMessage(String mensagem) {
    }
}
