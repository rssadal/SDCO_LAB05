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

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface IMensagem extends Remote {
    
    public Mensagem enviar(Mensagem mensagem) throws RemoteException;
    
}
