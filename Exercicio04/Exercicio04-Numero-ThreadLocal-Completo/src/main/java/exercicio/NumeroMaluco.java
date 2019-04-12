package exercicio;

public class NumeroMaluco 
{	private static ThreadLocal<Double> numero = new ThreadLocal<Double>();
    public static double get() 
    {	Double n = numero.get();
    	//Thread.currentThread().getId()
    	if(n == null)
    	{	n = new Double(Math.random());
    		numero.set(n);
    	}
    	return n; 
    }

	/* A classe ThreadLocal prov� uma  forma  de  criar   vari�veis 
	 * com escopo de Thread.  A classe ThreadLocal  prov� uma op��o 
	 * entre   as   vari�veis   est�ticas   (cujos   valores    s�o 
	 * compartilhados  por  todos os  objetos  de uma  classe) e as
	 * vari�veis n�o est�ticas (que possuem valores diferentes para
	 * cada objeto). 
	 *
	 * Ao se  declarar um campo  est�tico para  armazenar um objeto
	 * ThreadLocal,  este  objeto  ThreadLocal  armazena  um  valor 
	 * diferente para cada thread.
	 * 
	 * O m�todo  set() designa o  valor  que ser�  armazenado  pela 
	 * objeto ThreadLocal para a thread que estiver em execu��o  no 
	 * momento.
	 *
	 * O  m�todo  get() recupera o  valor para a  thread  corrente, 
	 * armazenado no objeto ThreadLocal.
	 *
	 * Ao  se  chamar  o  m�todo  get()  pela  primeira vez sem ter 
	 * chamado o  m�todo set()  para  designar  um  valor, o m�todo 
	 * get() ir�  chamar o  m�todo  protected  initialValue()  para 
	 * obter   o   valor  inicial.    A  implementa��o  default  de 
	 * initialValue() retorna null.
	 */
}
