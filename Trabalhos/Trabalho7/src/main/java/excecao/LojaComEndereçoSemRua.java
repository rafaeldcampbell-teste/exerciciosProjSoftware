package excecao;

public class LojaComEndere�oSemRua extends RuntimeException {
    private final static long serialVersionUID = 1;

    public LojaComEndere�oSemRua(Exception e) {
    	super(e);
    }
    
    public LojaComEndere�oSemRua(String e) {
    	super(e);
    }
}