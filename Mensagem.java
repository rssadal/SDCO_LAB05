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

import java.io.Serializable;

public class Mensagem implements Serializable {
    
	String mensagem;
	
	//Cliente -> Servidor
    public Mensagem(String mensagem, String opcao){    	
                
        setMensagem(mensagem,opcao);
        
    }
    //Servidor -> Cliente
    public Mensagem(String mensagem){
    	this.mensagem = new String(mensagem);
    }
    public String getMensagem(){
    	return this.mensagem;
    }
    public void setMensagem(String fortune, String opcao){
    	String mensagem="";
    	
    	switch(opcao){
	    	case "1": {
	        		
	    		mensagem += "{\n"+
	    		"\"method\": \"read\",\n"+
	    		"\"args\": [\"\"]\n"+
	    		"}";
	
				break;
			}
	    	case "2": {
	    		                		
	        		mensagem +="{\n"+
	        		"\"method\": \"write\",\n"+
	        		"\"args\": [\""+fortune+"\"]\n"+
	        		"}";    
	    			break;
    		}
    	}//fim switch
    	this.mensagem = mensagem;
    }
}
