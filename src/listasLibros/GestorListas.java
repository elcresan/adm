package listasLibros;

import internet.ListaServer;

import java.util.ArrayList;

import apiGoogle.InterfazAPI;


public class GestorListas {
	ArrayList<ListaLibros> lista = new ArrayList<ListaLibros>(); 
	InterfazAPI api=new InterfazAPI();
	ListaServer servidor=new ListaServer();
	String usuarioActual;
	
	public GestorListas(String nombreDeUsuario) { //Constructor
		usuarioActual=nombreDeUsuario;
	}
	
	public void addListaVacia(String nombre){//No avisa si ya existe
		if(!existe(nombre)){
		ListaLibros lis = new ListaLibros(nombre);
		lista.add(lis);
		
		//Queda comprobar, si es posible, si ha sido bien agregada o no
		servidor.creaListaDeUsuario(nombre, usuarioActual);
		}
	}
	
	public void BorraLista(String nombreLista){
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista()==nombreLista)
				i=lista.size();
		}
		lista.remove(lis);
		//Queda comprobar, si es posible, si ha sido bien agregada o no
		servidor.borraListaDeUsuario(nombreLista, usuarioActual);
	}
	
	private boolean existe(String nombreLista) {//Sin servidor
		boolean ex = false;
		ListaLibros aux = null;
		if (!lista.isEmpty())
			for (int i=0; i<lista.size(); i++) { 
				aux = lista.get(i); 
				 	if (aux.getNombreLista()==nombreLista)
				 		ex = true;
				}
		return ex;
	}
	
	//Devuelve todos los libros de una lista
	public ArrayList<Libro> getListaDeLibros(String nombreLista){//Sin servidor
		if (lista==null){
			ArrayList<String> ids=new ArrayList<String>();
			ids=servidor.obtenerLibrosLista(nombreLista, usuarioActual);
			for(int i=0;i<ids.size();i++){
				//lista.add(object)
			}
		}
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista()==nombreLista)
				i=lista.size();
		}
		return lis.getListaLibros();
	}
	
	public void borraLibroDeLista(String isbn, String nombreLista){//id==isbn?
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista()==nombreLista)
				i=lista.size();
		}
		lis.borraLibroPorIsbn(isbn);//id==isbn?
		//Queda comprobar, si es posible, si ha sido bien agregada o no
		servidor.borraLibroDeLista(nombreLista, usuarioActual, isbn);
	}
	
	public void addLibroEnLista(String isbn, String nombreLista){//id==isbn?
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista()==nombreLista)
				i=lista.size();
		}
		
		
		//a�adir cosas al constructor en la clase libro!!!!
		Libro lib=new Libro(isbn);//idid==isbn?
		//a�adir cosas al constructor en la clase libro
		
		lis.addLibro(lib);
		//Queda comprobar, si es posible, si ha sido bien agregada o no
		servidor.borraLibroDeLista(nombreLista, usuarioActual, isbn);
	}
	
	//If null, sacarlo del servidor
	//Devuelve un array con los nombres de las listas
	public ArrayList<String> getNombresListas() {
		ArrayList<String> nombres = new ArrayList<String>(); 
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			nombres.add(lis.getNombreLista());
		}
		return nombres;
	}
	
	//////////////////////////////////
	///SERVIDOR
	///////////////////////
	
	/*
	 * Dos opciones, por una parte, la de las estructuras que he dejado aqui:
	 * 	b�sicamente es tratar como diferentes guardar y borrar lista
	 * 	asi se podr�a borrar cuando te de la gana y guardar... pues igual
	 *  el problema de esto es que es muy manual y habr�a que tener siempre
	 *  conexion a la red.
	 *  
	 *  Por otra parte, cosa que yo creo que es mejor idea pero no se si es posible:
	 *  Unir las dos funciones guardar y borrar listas para que �nicamente
	 *  se llame cuando se vaya a cerrar la aplicaci�n. Habria que detectar que hayan
	 *  o no habido cambios (esto es sencillo, con un flag), y ser�a una funcion
	 *  actualizar que ya llamase a guardar o borrar libros de ListaLibros, que ser�a
	 *  desde donde se accede al servidor.
	 *  La funcion esta tendr�a que ser llamada al cerrar la aplicaci�n (de aqui el no
	 *  sabes si esto es posible de hacer) cuando se hayan detectado cambios y haya
	 *  conexi�n a internet (cada x tiempo).
	 * 
	public void guardaLista(String nombreLista, String uname){//Necesitar� nombre de usuario
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista()==nombreLista)
				i=lista.size();
		}
		lis.guardaListaLibros(nombreLista,uname);//Necesitar� nombre de usuario
	}
	
	
	public void cargaListaLibros(){
		//No har�a falta identificador, se supone que esto se har�a al iniciar
		//la aplicaci�n

	}
	
	public void borraListaLibros(){
		//las dos opciones anteriores

	}*/
	
}
