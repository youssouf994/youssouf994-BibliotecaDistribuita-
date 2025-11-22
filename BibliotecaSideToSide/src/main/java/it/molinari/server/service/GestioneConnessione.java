package it.molinari.server.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import it.molinari.server.httpStatus.enums.*;

public class GestioneConnessione 
{
	private static final int PORT=1050;
	ServerSocket serverSocket;
	Socket clientSocket=null;//abbinamento per il client
	BufferedReader inputDaClient= null;
	PrintWriter cout=null;
	private String str;
	
	public GestioneConnessione() throws IOException//RICHIESTA CONNESSIONE
	{
		try
		{
			ServerSocket serverSocket = new ServerSocket(PORT);//server in ascolto 
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

	public void payload() throws IOException
	{
		try
		{
			while(true)
			{
				this.str=this.inputDaClient.readLine();
				if(str.equals("end)"))
				{
					break;
				}
				else
				{
					this.cout.println(HttpStatus.OK.getCodice()+" "+HttpStatus.OK.getMessaggio());//restituisce ok e avvio schermata login
					//logica di business verso gestione
					
					//che scarica la response effettiva in this.cout
				}
			}
		}
		catch(IOException e)
		{
			System.out.println(HttpStatus.BAD_REQUEST.getCodice()+" "+HttpStatus.BAD_REQUEST.getMessaggio());	
			System.out.println(e);
			this.chiudiStreams();
		}
	}

	private void chiudiStreams() throws IOException
	{
		this.cout.close();
		this.inputDaClient.close();
		this.clientSocket.close();
		this.serverSocket.close();
	}
}
