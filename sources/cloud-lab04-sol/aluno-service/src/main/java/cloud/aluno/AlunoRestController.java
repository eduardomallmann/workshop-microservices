package cloud.aluno;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alunos")
public class AlunoRestController {

	@Autowired
	AlunoRepository repository;
	
	@Autowired
	DisciplinaClient disciplinaClient;
	
	@GetMapping("/nomes")
	public List<String> getAlunos() {
		return repository.findAll()
				.stream().map(a -> a.getNome()).collect(Collectors.toList());
	}

	@GetMapping("/{id}/dto")
	@SuppressWarnings("all")
	public AlunoDTO getAluno(@PathVariable Long id) throws Exception {

		Resources<DisciplinaDTO> disciplinas = disciplinaClient.getAllDisciplinas();
		List<String> nomesDisciplinas = disciplinas.getContent().stream()
				.map(d -> d.getNome()).collect(Collectors.toList());
				
		Aluno aluno = repository.findOne(id);

		return AlunoDTO.builder().id(aluno.getId())
				.nome(aluno.getNome())
				.matricula(aluno.getMatricula())
				.email(aluno.getEmail())
				.disciplinas(nomesDisciplinas)
				.build();
	}

}