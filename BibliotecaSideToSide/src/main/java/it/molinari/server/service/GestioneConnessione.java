package it.molinari.server.service;

import it.molinari.server.enums.*;
import it.molinari.server.token.Tokenizer;
import it.molinari.server.request.Request;
import it.molinari.server.response.Response;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;




public class GestioneConnessione 
{
	protected static final int PORT=1050;
	protected ServerSocket serverSocket;
	protected Socket clientSocket=null;//abbinamento per il client
	protected BufferedReader inputDaClient= null;
	protected PrintWriter cout=null;
	protected String str;
	protected String serverResponse;
	protected String userJson;
	protected ObjectMapper mapper = new ObjectMapper();
	protected GeneratoreJson converter =new GeneratoreJson();
	protected Tokenizer tokenizer = new Tokenizer();
	protected List<Object> lista = new ArrayList<Object>();
	protected ActionType actionType;
	protected GestioneCollezione engine = new GestioneCollezione();
	protected Request request;
	protected Response response;

	public GestioneConnessione()
	{
		
	}
	
	public GestioneConnessione(int port) throws IOException//RICHIESTA CONNESSIONE
	{
		try
		{
			this.mapper.registerModule(new JavaTimeModule());
			this.mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
			
			this.serverSocket = new ServerSocket(port);//server in ascolto 
			this.clientSocket= this.serverSocket.accept();
			System.out.println("Connessione con client: "+this.clientSocket);
			
			this.apriStreamDaClient();
			this.apriStreamPerClient();
			
		}
		catch(IOException e)
		{
			System.out.println(HttpStatus.BAD_REQUEST.getCodice()+" "+HttpStatus.BAD_REQUEST.getMessaggio());	
			System.out.println(e);
			this.chiudiStreams();
		}
	}
	
	private void apriStreamDaClient() throws IOException
	{
		InputStreamReader isr = new InputStreamReader(this.clientSocket.getInputStream());
		this.inputDaClient=new BufferedReader(isr);
	}
	
	private void apriStreamPerClient() throws IOException
	{
		OutputStreamWriter osw = new OutputStreamWriter(this.clientSocket.getOutputStream());
		BufferedWriter bw = new BufferedWriter(osw);
		this.cout= new PrintWriter(bw, true);
	}

	
	public void chiudiStreams() throws IOException
	{
		this.cout.close();
		this.inputDaClient.close();
		this.clientSocket.close();
		this.serverSocket.close();
	}
}