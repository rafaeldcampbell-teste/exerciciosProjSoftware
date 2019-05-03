import java.util.List;

import corejava.Console;
import excecao.DataDeLanceInvalidaException;
import excecao.LanceNaoEncontradoException;
import excecao.ProdutoNaoEncontradoException;
import excecao.ValorDeLanceInvalidoException;
import modelo.Lance;
import modelo.Produto;
import servico.LanceAppService;
import servico.ProdutoAppService;
import util.Util;

public class PrincipalLance {
    public static void main(String[] args) {
	double valor;
	String dataCriacao;

	Produto umProduto;
	Lance umLance;

	ProdutoAppService produtoAppService = new ProdutoAppService();
	LanceAppService lanceAppService = new LanceAppService();

	boolean continua = true;
	while (continua) {
	    System.out.println('\n' + "O que voc� deseja fazer?");
	    /* ==> */ System.out.println('\n' + "1. Cadastrar um lance de um produto");
	    /* ==> */ System.out.println("2. Remover um lance");
	    System.out.println("3. Recuperar todos os lances");
	    System.out.println("4. Sair");

	    int opcao = Console.readInt('\n' + "Digite um n�mero entre 1 e 4:");

	    switch (opcao) {
	    case 1: {
		long idProduto = Console.readInt('\n' + "Informe o n�mero do produto: ");

		try {
		    umProduto = produtoAppService.recuperaUmProduto(idProduto);
		} catch (ProdutoNaoEncontradoException e) {
		    System.out.println('\n' + e.getMessage());
		    break;
		}

		valor = Console.readDouble('\n' + "Informe o valor do lance: ");
		dataCriacao = Console.readLine("Informe a data de emiss�o do lance: ");

		umLance = new Lance(valor, Util.strToCalendar(dataCriacao), umProduto);

		try
		/* ==> */ {
		    lanceAppService.inclui(umLance);

		    System.out.println('\n' + "Lance adicionado com sucesso");
		} catch (ProdutoNaoEncontradoException | ValorDeLanceInvalidoException
			| DataDeLanceInvalidaException e) {
		    System.out.println(e.getMessage());
		}

		break;
	    }

	    case 2: {
		int resposta = Console.readInt('\n' + "Digite o n�mero do lance que voc� deseja remover: ");

		try {
		    umLance = lanceAppService.recuperaUmLance(resposta);
		} catch (LanceNaoEncontradoException e) {
		    System.out.println('\n' + e.getMessage());
		    break;
		}

		System.out.println('\n' + "N�mero = " + umLance.getId() + "    Valor = " + umLance.getValor()
			+ "    Data de Emiss�o = " + umLance.getDataCriacaoMasc());

		String resp = Console.readLine('\n' + "Confirma a remo��o do lance?");

		if (resp.equals("s")) {
		    try
		    /* ==> */ {
			lanceAppService.exclui(umLance.getId());
			System.out.println('\n' + "Lance removido com sucesso!");
		    } catch (LanceNaoEncontradoException e) {
			System.out.println(e.getMessage());
		    }
		} else {
		    System.out.println('\n' + "Lance n�o removido.");
		}

		break;
	    }

	    case 3: {
		List<Lance> arrayLances = lanceAppService.recuperaLances();

		if (arrayLances.size() == 0) {
		    System.out.println('\n' + "Nao h� lances cadastrados.");
		    break;
		}

		System.out.println("");
		for (Lance lance : arrayLances) {
		    System.out.println("N�mero = " + lance.getId() + "  Valor = " + lance.getValor()
			    + "  Data de Emiss�o = " + lance.getDataCriacaoMasc());
		}

		break;
	    }

	    case 4: {
		continua = false;
		break;
	    }

	    default:
		System.out.println('\n' + "Op��o inv�lida!");
	    }
	}
    }
}
