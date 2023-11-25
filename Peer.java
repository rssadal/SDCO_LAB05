/**
 * Lab05: Sistema P2P
 * 
 * Autor: Vítor Augusto Ozanick e Adalberto Teixeira Guedes
 * Ultima atualizacao: 25/11/2023
 * 
 * Referencias: 
 * https://docs.oracle.com/javase/tutorial/essential/io
 * http://fortunes.cat-v.org/
 */

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class Peer implements IMensagem {

	ArrayList<PeerLista> alocados;

	public Peer() {
		alocados = new ArrayList<>();
	}
	
	@Override
	public Mensagem enviar(Mensagem mensagem) throws RemoteException {
		Mensagem resposta;
		try {
			System.out.println("Mensagem recebida: " + mensagem.getMensagem());
			resposta = new Mensagem(parserJSON(mensagem.getMensagem()));
		} catch (Exception e) {
			e.printStackTrace();
			resposta = new Mensagem("{\n" + "\"result\": false\n" + "}");
		}
		return resposta;
	}

	public String parserJSON(String json) {
		String result = "false";

		String fortune = "-1";

		String[] v = json.split(":");
		System.out.println(">>>" + v[1]);
		String[] v1 = v[1].split("\"");
		System.out.println(">>>" + v1[1]);
		if (v1[1].equals("write")) {
			String[] p = json.split("\\[");
			System.out.println(p[1]);
			String[] p1 = p[1].split("]");
			System.out.println(p1[0]);
			String[] p2 = p1[0].split("\"");
			System.out.println(p2[1]);
			fortune = p2[1];

			// Write in file
			Principal pv2 = new Principal();
			pv2.write(fortune);
		} else if (v1[1].equals("read")) {
			// Read file
			Principal pv2 = new Principal();
			fortune = pv2.read();
		}

		result = "{\n" + "\"result\": \"" + fortune + "\"" + "}";
		System.out.println(result);

		return result;
	}

	public void iniciar() {

		try {
			
			List<PeerLista> listaPeers = new ArrayList<>();
			for (PeerLista peer : PeerLista.values())
				listaPeers.add(peer);

			Registry servidorRegistro;
			try {
				servidorRegistro = LocateRegistry.createRegistry(1099);
			} catch (java.rmi.server.ExportException e) { 
				System.out.print("Registro jah iniciado. Usar o ativo.\n");
			}
			servidorRegistro = LocateRegistry.getRegistry(); 
			String[] listaAlocados = servidorRegistro.list();
			for (int i = 0; i < listaAlocados.length; i++)
				System.out.println(listaAlocados[i] + " ativo.");

			SecureRandom sr = new SecureRandom();
			PeerLista peer = listaPeers.get(sr.nextInt(listaPeers.size()));

			int tentativas = 0;
			boolean repetido = true;
			boolean cheio = false;
			while (repetido && !cheio) {
				repetido = false;
				peer = listaPeers.get(sr.nextInt(listaPeers.size()));
				for (int i = 0; i < listaAlocados.length && !repetido; i++) {

					if (listaAlocados[i].equals(peer.getNome())) {
						System.out.println(peer.getNome() + " ativo. Tentando proximo...");
						repetido = true;
						tentativas = i + 1;
					}

				}
				// System.out.println(tentativas+" "+listaAlocados.length);

				// Verifica se o registro estah cheio (todos alocados)
				if (listaAlocados.length > 0 && tentativas == listaPeers.size()) {
					cheio = true;
				}
			}

			if (cheio) {
				System.out.println("Sistema cheio. Tente mais tarde.");
				System.exit(1);
			}

			IMensagem skeleton = (IMensagem) UnicastRemoteObject.exportObject(this, 0); 
			
			servidorRegistro.rebind(peer.getNome(), skeleton);
			System.out.print(peer.getNome() + " Servidor RMI: Aguardando conexoes...");

			
			new ClienteRMI().iniciarCliente();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
        Peer servidor = new Peer();
        servidor.iniciar();

	    }
}
