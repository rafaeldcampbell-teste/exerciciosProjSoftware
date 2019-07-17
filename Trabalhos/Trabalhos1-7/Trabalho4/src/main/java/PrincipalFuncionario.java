import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import corejava.Console;
import excecao.ObjetoNaoEncontradoException;
import modelo.Funcionarios;
import modelo.Lojas;
import servico.FuncionarioAppService;
import servico.LojaAppService;

public class PrincipalFuncionario {

	public static void main(String[] args) {
		int codigo;
		int id_loja;
		String nome;
		String funcao;
		Lojas loja;
		Funcionarios funcionario;
		List<Funcionarios> funcionarios = new ArrayList<Funcionarios>();
		
		@SuppressWarnings("resource")
		ApplicationContext fabrica = new ClassPathXmlApplicationContext("beans-jpa.xml");

		LojaAppService lojaAppService = (LojaAppService)fabrica.getBean ("lojaAppService");
		FuncionarioAppService funcionarioAppService = (FuncionarioAppService)fabrica.getBean ("funcionarioAppService");
		

		boolean continua = true;
		while (continua) {
			System.out.println("O que voc� deseja fazer?\n");
		    System.out.println("1. Cadastrar um funcion�rio em uma loja");
		    System.out.println("2. Remover um funcion�rio de uma loja");
		    System.out.println("3. Listar todas os funcionarios de uma loja");
		    System.out.println("4. Voltar");

		    int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 4:");
		    
		    switch (opcao) {
			    case 1:{
		    		id_loja = Console.readInt('\n' + "Digite o id da loja onde deseja cadastrar o funcionario: ");
		    		try {
		    			loja = lojaAppService.recuperaUmaLoja(id_loja);
		    		} catch (ObjetoNaoEncontradoException e) {
		    			break;
		    		}
		    		nome = Console.readLine('\n' + "Digite o nome do funcion�rio: ");
		    		funcao = Console.readLine('\n' + "Digite a fun��o do funcion�rio: ");
		    		
		    		funcionario = new Funcionarios(nome, funcao, loja);
		    		
		    		codigo = funcionarioAppService.inclui(funcionario);
		    		
		    		System.out.println("Funcionario "+codigo+" cadastrada com sucesso!");
		    		break;
		    	}
		    	
		    	case 2:{
		    		codigo = Console.readInt('\n' + "Digite o codigo do funcion�rio que deseja remover: ");
		    		
					try {
					    funcionario = funcionarioAppService.recuperaUmFuncionario(codigo);
					} catch (ObjetoNaoEncontradoException e) {
					    break;
					}
	
					System.out.println('\n' + "C�digo = " + funcionario.getCodigo() 
											+ "\nNome = " + funcionario.getNome()
											+ "\nFun��o = "+ funcionario.getFuncao()
											+ "\nLoja = " + funcionario.getLoja().getId());
	
					String resp = Console.readLine('\n' + "Confirma a remo��o do funcion�rio?");
	
					if (resp.equals("s")) {
					    try {
						funcionarioAppService.exclui(funcionario.getCodigo());
						System.out.println('\n' + "Funcionario removido com sucesso!");
					    } catch (Exception e) {
					    	break;
					    }
					} else {
					    System.out.println('\n' + "Funcionario n�o removido.");
					}
					break;
		    	}
		    	
		    	case 3: {
		    		id_loja = Console.readInt('\n' + "Digite o id da loja cujos funcionarios ser�o listados: ");
		    		
		    		try{
		    			funcionarios = funcionarioAppService.recuperaFuncionarios(id_loja);
		    		} catch (Exception e){
		    			break;
		    		}
		    		for(Funcionarios f : funcionarios) {
		    			System.out.println('\n' + "C�digo = " + f.getCodigo() 
												+ "\nNome = " + f.getNome()
												+ "\nFun��o = "+ f.getFuncao()
												+ "\nLoja = " + f.getLoja().getId());
		    		}
		    		funcionarios.clear();
		    		break;
		    	}
		    	case 4:{
			    	continua = false;
			    	break;
			    }
			    default:{
			    	System.out.println("=========> Op��o inv�lida");
			    }
		    }
		}
		
	}
}
