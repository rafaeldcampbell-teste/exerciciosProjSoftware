import java.util.ArrayList;
import java.util.List;

import corejava.Console;
import excecao.ObjetoNaoEncontradoException;
import modelo.Funcionarios;
import modelo.Lojas;
import modelo.Mesas;
import servico.FuncionarioAppService;
import servico.LojaAppService;
import servico.MesaAppService;
import util.JPAUtil;

public class PrincipalMesa {

	public static void main(String[] args) {
		int id;
		int id_loja;
		int codigo_funcionario;
		int numero;
		Funcionarios funcionario;
		Lojas loja;
		Mesas mesa;
		List<Mesas> mesas = new ArrayList<Mesas>();
		
		MesaAppService mesaAppService = new MesaAppService();
		LojaAppService lojaAppService = new LojaAppService();
		FuncionarioAppService funcionarioAppService = new FuncionarioAppService();
		
		boolean continua = true;
		while (continua) {
			System.out.println('\n' + "O que voc� deseja fazer?");
		    System.out.println('\n' + "1. Cadastrar uma mesa de uma loja");
		    System.out.println("2. Remover uma mesa de uma loja");
		    System.out.println("3. Listar todas as mesas de uma loja");
		    System.out.println("4. Listar todas as mesas de um funcionario");
		    System.out.println("5. Voltar");

		    int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 5:");
		    
		    switch (opcao) {
		    	case 1:{
		    		id_loja = Console.readInt('\n' + "Digite o id da loja onde deseja cadastrar a mesa: ");
		    		try {
		    			loja = lojaAppService.recuperaUmaLoja(id_loja);
		    		} catch (ObjetoNaoEncontradoException e) {
		    			System.out.println("=====> Loja n�o cadastrada!");
		    			JPAUtil.commitTransaction();
		    			break;
		    		}
		    		
		    		codigo_funcionario = Console.readInt('\n' + "Digite o c�digo do funcionario que ir� atender a mesa: ");
		    		try {
		    			funcionario = funcionarioAppService.recuperaUmFuncionario(codigo_funcionario);
		    		}catch (ObjetoNaoEncontradoException e) {
		    			System.out.println("=====> Funcion�rio n�o cadastrado!");
		    			JPAUtil.commitTransaction();
		    			break;
		    		} catch (Exception e) {
		    			break;
		    		}
		    		
		    		numero = Console.readInt('\n' + "Digite o n�mero da mesa: ");
		    		
		    		mesa = new Mesas(loja, funcionario, numero);
		    		
		    		id = mesaAppService.inclui(mesa);
		    		
		    		System.out.println("Mesa "+id+" cadastrada com sucesso!");
		    		break;
		    	}
		    	
		    	case 2:{
		    		id = Console.readInt('\n' + "Digite o id da mesa que deseja remover: ");
		    		
					try {
					    mesa = mesaAppService.recuperaUmaMesa(id);
					} catch (ObjetoNaoEncontradoException e) {
		    			System.out.println("=====> Mesa n�o cadastrada!");
		    			JPAUtil.commitTransaction();
					    break;
					}
	
					System.out.println('\n' + "Id = " + mesa.getId() 
											+ "\nLoja = " + mesa.getLoja().getId()
											+ "\nFuncionario = "+ mesa.getFuncionario().getCodigo()
											+ "\nNumero = "+ mesa.getNumero());
	
					String resp = Console.readLine('\n' + "Confirma a remo��o da mesa?");
	
					if (resp.equals("s")) {
					    try {
						mesaAppService.exclui(mesa.getId());
						System.out.println('\n' + "Mesa removida com sucesso!");
					    } catch (Exception e) {
					    	break;
					    }
					} else {
					    System.out.println('\n' + "Mesa n�o removida.");
					}
					break;
		    	}
		    	
		    	case 3: {
		    		id_loja = Console.readInt('\n' + "Digite o id da loja cujas mesas ser�o listadas: ");
		    		
		    		try{
		    			mesas = mesaAppService.recuperaMesasPorLoja(id_loja);
		    		} catch(ObjetoNaoEncontradoException objE) {
		    			System.out.println("Nenhuma mesa cadastrada!");
		    		} catch (Exception e){
		    			break;
		    		}
		    		for(Mesas m : mesas) {
		    			System.out.println('\n' + "Id = " + m.getId() 
										   	   	+ "\nLoja = " + m.getLoja().getId()
										   	   	+ "\nFuncionario = "+ m.getFuncionario().getCodigo()
												+ "\nNumero = "+ m.getNumero());
		    		}
		    		mesas.clear();
		    		break;
		    	}
		    	case 4: {
		    		codigo_funcionario = Console.readInt('\n' + "Digite o id do funcionario cujas mesas ser�o listadas: ");
		    		
		    		try{
		    			mesas = mesaAppService.recuperaMesasPorFuncionario(codigo_funcionario);
		    		} catch(ObjetoNaoEncontradoException objE) {
		    			System.out.println("Nenhuma mesa cadastrada!");
		    		}catch (Exception e){
		    			break;
		    		}
		    		for(Mesas m : mesas) {
		    			System.out.println('\n' + "Id = " + m.getId() 
										   	   	+ "\nLoja = " + m.getLoja().getId()
										   	   	+ "\nFuncionario = "+ m.getFuncionario().getCodigo()
												+ "\nNumero = "+ m.getNumero());
		    		}
		    		mesas.clear();
		    		break;
		    	}
		    	case 5:{
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
