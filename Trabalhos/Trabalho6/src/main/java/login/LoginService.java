package login;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dao.UsuarioDAO;
import modelo.Perfil;
import modelo.Usuario;

@Service
public class LoginService {
	@Autowired
	private UsuarioDAO usuarioDAO;
	
	private Usuario usuario;
	
	
	public boolean validaUsuario(String conta, String senha) {
		List<Usuario> usuarios = usuarioDAO.recuperaListaUsuarios(conta, senha);
		if(usuarios.size() != 1) {
			return false;
		}
		usuario = usuarios.get(0);
		return true;
	}
	
	public String getConta() {
		return usuario.getConta();
	}
	
	public ArrayList<String> getPerfis(){
		ArrayList<String> perfis = new ArrayList<String>();
		if(usuario == null) return null;
		List<Perfil> perfisDeUsuario = usuarioDAO.recuperaPerfis(usuario.getConta());
		for(Perfil p : perfisDeUsuario) {
			perfis.add(p.getPerfil());
		}
		return perfis;
	}

}
