package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import excecao.InfraestruturaException;

public class JPAUtil {
    private static EntityManagerFactory emf;
    private static EntityManager staticEntityManager;
    private static EntityTransaction staticTransaction;
    private static int transactionCounter = 0;

    static {
	try {
	    emf = Persistence.createEntityManagerFactory("exercicio");
	} catch (Throwable e) {
	    e.printStackTrace();
	    System.out.println(">>>>>>>>>> Mensagem de erro: " + e.getMessage());
	    throw e;
	}
    }

    public static void beginTransaction() { // System.out.println("Vai criar transacao");
		
    	if(staticEntityManager == null) {
			getEntityManager();
		}
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Entrou em beginTransaction()");
		if(transactionCounter == 1) {
			try {
			    if (staticTransaction == null) {
			    	staticTransaction = staticEntityManager.getTransaction(); //garantindo que a vari�vel tem uma transa��o
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Criou a transa��o");
			    	
			    } 
			    staticTransaction.begin();
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> abrindo transa��o");
			} catch (RuntimeException ex) {
				System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> erro ao abrir transa��o");
			    throw new InfraestruturaException(ex);
			}
		}
		if(transactionCounter > 0) {
			transactionCounter += 1;
		}else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> abrindo transa��o sem entity manager");
		}
    }

    public static EntityManager getEntityManager() { // System.out.println("Abriu ou recuperou sess�o");
		// Abre uma nova Sess�o, se for a primeira execu��o
		try {
		    if (staticEntityManager == null) {
		    	staticEntityManager = emf.createEntityManager();
		    	transactionCounter += 1;
		    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Criou o entity manager");
		    }
		} catch (RuntimeException ex) {
		    throw new InfraestruturaException(ex);
		}
		return staticEntityManager;
    }

    public static void commitTransaction() {
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Entrou em commitTransaction");
		transactionCounter -= 1;
		if(transactionCounter == 1) {
			try {
			    if (staticTransaction != null && staticTransaction.isActive()) {
			    	staticTransaction.commit();
			    	staticTransaction = null;
			    	transactionCounter -= 1;
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Comitou transacao");
			    }
			} catch (RuntimeException ex) {
			    try {
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> erro ao fechar transa��o, executando rollback");
			    	rollbackTransaction();
			    } catch (RuntimeException e) {
			    }
		
			    throw new InfraestruturaException(ex);
			}
		}else {
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Aguardando outros usos da transa��o");
		}
    }

    public static void rollbackTransaction() {
		System.out.println("Vai efetuar rollback de transacao");
		try {
		    if (staticTransaction != null && staticTransaction.isActive()) {
		    	staticTransaction.rollback();
		    	staticTransaction = null;
		    }
		} catch (RuntimeException ex) {
		    throw new InfraestruturaException(ex);
		} finally {
		    closeEntityManager();
		}
    }

    public static void closeEntityManager() { // System.out.println("Vai fechar sess�o");

		System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> entrou em closeEntityManager");
		if(transactionCounter == 0) {
			try {
			    if (staticEntityManager != null && staticEntityManager.isOpen()) {
			    	staticEntityManager.close();
			    	staticEntityManager = null;
			    	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Fechou o entity manager");
			    }
			    
			    if (staticTransaction != null && staticTransaction.isActive()) {
					rollbackTransaction();
					throw new RuntimeException("EntityManager sendo fechado com transa��o ativa.");
			    }
			} catch (RuntimeException ex) {
			    throw new InfraestruturaException(ex);
			}
	    }else {

			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Aguardando o fim de todas as execu��es");
	    }
    }
}
