package exercicio;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.LockModeType;
import javax.persistence.OptimisticLockException;

public class ProdutoDAOImpl implements ProdutoDAO
{	
	public long inclui(Produto umProduto) 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	// transiente - objeto novo: ainda n�o persistente
			// persistente - apos ser persistido 
			// destacado - objeto persistente n�o vinculado a um entity manager
		
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			em.persist(umProduto);
			
			tx.commit();
			return umProduto.getId();
		} 
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		    	{	tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public void altera(Produto umProduto) throws ProdutoNaoEncontradoException
	{	EntityManager em = null;
		EntityTransaction tx = null;
		Produto produto = null;
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();
			
			produto = em.find(Produto.class, umProduto.getId(), LockModeType.PESSIMISTIC_WRITE);
			
			if(produto == null)
			{	tx.rollback();
				throw new ProdutoNaoEncontradoException("Produto n�o encontrado");
			}
			em.merge(umProduto);
			tx.commit();
		} 
==>		catch(OptimisticLockException e)  // sub-classe de RuntimeException
		{   
			if (tx != null)
		    {   tx.rollback();
		    }
==>			throw new EstadoDeObjetoObsoletoException();
		}
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		        {   tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public void exclui(long numero) throws ProdutoNaoEncontradoException 
	{	EntityManager em = null;
		EntityTransaction tx = null;
		
		try
		{	
			em = FabricaDeEntityManager.criarSessao();
			tx = em.getTransaction();
			tx.begin();

			Produto produto = em.find(Produto.class, numero, LockModeType.PESSIMISTIC_WRITE);
			
			if(produto == null)
			{	tx.rollback();
				throw new ProdutoNaoEncontradoException("Produto n�o encontrado");
			}

			// COMO PARA REMOVER UM PRODUTO NA JPA � PRECISO PRIMEIRAMENTE
			// RECUPER�-LO, QUANDO O  PRODUTO �  RECUPERADO SEU N�MERO  DE
			// VERS�O  J�  ATUALIZADO  VEM  JUNTO,  O  QUE  FAZ  COM QUE O 
			// CONTROLE DE VERS�O N�O FUNCIONE SE A REMO��O ACONTECER AP�S 
			// UMA ATUALIZA��O, OU SE OCORREREM DUAS REMO��ES EM  PARALELO 
			// DO MESMO PRODUTO.
			
			// LOGO, A  EXCE��O  OptimisticLockException NUNCA  ACONTECER�
			// NO CASO DE REMO��ES.

			em.remove(produto);

			tx.commit();
		} 
		catch(RuntimeException e)
		{   
			if (tx != null)
		    {   try
		        {	tx.rollback();
		        }
		        catch(RuntimeException he)
		        { }
		    }
		    throw e;
		}
		finally
		{   em.close();
		}
	}

	public Produto recuperaUmProduto(long numero) throws ProdutoNaoEncontradoException
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeEntityManager.criarSessao();

			Produto umProduto = em.find(Produto.class, numero);
			
			// Caracter�sticas no m�todo find():
			// 1. � gen�rico: n�o requer um cast.
			// 2. Retorna null caso a linha n�o seja encontrada no banco.

			if(umProduto == null)
			{	throw new ProdutoNaoEncontradoException("Produto n�o encontrado");
			}
			return umProduto;
		} 
		finally
		{   em.close();
		}
	}

	@SuppressWarnings("unchecked")
	public List<Produto> recuperaProdutos()
	{	EntityManager em = null;
		
		try
		{	em = FabricaDeEntityManager.criarSessao();

			List<Produto> clientes = em
				.createQuery("select p from Produto p order by p.id")
				.getResultList();

			// Retorna um List vazio caso a tabela correspondente esteja vazia.
			
			return clientes;
		} 
		finally
		{   em.close();
		}
	}
}