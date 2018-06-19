package com.viavarejo.pontointeligente.api.repositories;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.viavarejo.pontointeligente.api.entities.Empresa;
import com.viavarejo.pontointeligente.api.entities.Funcionario;
import com.viavarejo.pontointeligente.api.entities.Lancamento;
import com.viavarejo.pontointeligente.api.enums.PerfilEnum;
import com.viavarejo.pontointeligente.api.enums.TipoEnum;
import com.viavarejo.pontointeligente.api.util.PasswordUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class LancamentoRepositoryTest {
	
	@Autowired
	LancamentoRepository lancamentoRepository;
	
	@Autowired
	EmpresaRepository empresaRepository;
	
	
	@Autowired
	FuncionarioRepository funcionarioRepository;
	
	
	private Long funcionarioId;

	@Before
	public void setUp()throws Exception{
		Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
		
		Funcionario funcionario = this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		this.funcionarioId = funcionario.getId();
		
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
		this.lancamentoRepository.save(obterDadosLancamentos(funcionario));
	}
		
	@After
	public void tearDown() throws Exception {
		this.empresaRepository.deleteAll();
	}

	
	@Test
	public void testBuscarLancamentosPorFuncionarioId() {
		List<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId);		
		
		assertEquals(2, lancamentos.size());
		
	}
	
	@Test
	public void testBuscarLancamentosPorFuncionarioIdPaginado() {
		PageRequest page = new PageRequest(0, 10);
		Page<Lancamento> lancamentos = this.lancamentoRepository.findByFuncionarioId(funcionarioId, page);
		
		assertEquals(2, lancamentos.getTotalElements());
	}
	
	public Lancamento obterDadosLancamentos(Funcionario funcionario) {
		Lancamento lancameto = new Lancamento();
		lancameto.setData(new Date());
		lancameto.setTipo(TipoEnum.INICIO_ALMOCO);
		lancameto.setFuncionario(funcionario);
		
		return lancameto;		
	}
	
	public Funcionario obterDadosFuncionario(Empresa empresa) throws NoSuchAlgorithmException{
		
		Funcionario funcionario = new Funcionario();
		funcionario.setNome("Paulo Brito");
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
		funcionario.setCpf("24291173474");
		funcionario.setEmail("email@email.com");
		funcionario.setQtdHorasAlmoco(1.50F);
		funcionario.setQtdHorasTrabalhoDia(8.30F);
		funcionario.setValorHora( new BigDecimal("85.23"));
		funcionario.setEmpresa(empresa);
		return funcionario;	
		
	}
	
	private Empresa obterDadosEmpresa() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial("Empresa de exemplo");
		empresa.setCnpj("51463645000100");
		return empresa;
	}
	
	
	
}
