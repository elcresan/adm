package listasLibros;

import internet.ListaServer;

import java.util.ArrayList;
import java.util.HashSet;

import com.example.boox.MyBD;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import apiGoogle.InterfazAPI;

public class GestorListas {
	ArrayList<ListaLibros> lista = new ArrayList<ListaLibros>();
	InterfazAPI api=new InterfazAPI();
	ListaServer servidor=new ListaServer();
	String usuarioActual;
	ListaCompartibles shared;
	boolean datosActualizados = true;
	MyBD bd;

	public GestorListas(String nombreDeUsuario, Context context) { //Constructor
		usuarioActual=nombreDeUsuario;
		shared= new ListaCompartibles(nombreDeUsuario);
		bd = new MyBD(context,nombreDeUsuario);
		try{
			datosActualizados=ActualizarTodo();
		}catch(SQLiteException e){
			e.printStackTrace();
		}
	}

	public boolean EstanLosDatosActualizados(){
		return datosActualizados;
	}
	
	private boolean ActualizarTodo(){
		boolean correcto=true;
		ArrayList<String> listaNombresListas=null;
		listaNombresListas=servidor.obtenerListas(usuarioActual);
		if(listaNombresListas==null){
			actualizarTodoDeBD();
			return false;
		}
		for(int i=0;i<listaNombresListas.size();i++){//Cargamos nombres listas
			AddListaVacia(listaNombresListas.get(i));
			/*ArrayList<String> listaLibros;
			listaLibros=servidor.obtenerLibrosLista(listaNombresListas.get(i), usuarioActual);
			if(listaLibros==null)
				correcto=false;

			for(int j=0;j<listaLibros.size();j++){//Cargamos libros en listas
				Libro lib=bd.DetalleLibroId(listaLibros.get(j));
				if(lib==null){
					lib=api.ObtenerLibroPorId(listaLibros.get(j));
					if(lib!=null){
						try{
						bd.InsertarLibro(lib);
						}catch(SQLiteException e){
							
						}
						AddLibroEnLista(lib,listaNombresListas.get(i));
					}
					else
						correcto=false;
				}
				else AddLibroEnLista(lib,listaNombresListas.get(i));
			}*/
		}
		return correcto;
	}
	
	public void actualizarTodoDeBD(){ 
		lista = bd.ListadoListas();
	}

	public ArrayList<Libro> getListaAll(){
        ArrayList<Libro> all= new ArrayList<Libro>();
        ListaLibros todo = bd.ListaDeTodosLibros();
        all = todo.getListaLibros();
        return all;
    }

	//Si el nombre de la lista ya existe o hay problemas con el servidor, devuelve false
	public Boolean AddListaVacia(String nombre){
		Boolean correcto=false;
		if(datosActualizados==true){
			if(!existe(nombre)){
				ListaLibros lis = new ListaLibros(nombre);
				correcto=servidor.creaListaDeUsuario(nombre, usuarioActual);
				if(correcto){
					lista.add(lis);
					try{
					bd.CrearNuevaLista(nombre);
					}catch(SQLiteException e){
						e.printStackTrace();
					}
					return true;
				}
				
			}
		}
		else{
			return false;//Intentar descargar el modelo de internet?
		}
		return correcto;
	}

	public Boolean BorraLista(String nombreLista){
		Boolean correcto=false;
		if(datosActualizados==true){
			ListaLibros lis = null;
			for (int i=0; i<lista.size(); i++) { 
				lis = lista.get(i);
				if (lis.getNombreLista().equals(nombreLista))
					i=lista.size();
			}

			correcto = servidor.borraListaDeUsuario(nombreLista, usuarioActual);
			if (correcto){
				lista.remove(lis);
				bd.EliminarLista(nombreLista);
			}
		}else
			return false;//Intentar descargar el modelo de internet?
		return correcto;
	}

	private boolean existe(String nombreLista) {//Sin servidor
		boolean ex = false;
		ListaLibros aux = null;
		if (!lista.isEmpty())
			for (int i=0; i<lista.size(); i++) { 
				aux = lista.get(i); 
				if (aux.getNombreLista().equals(nombreLista))
					ex = true;
			}
		return ex;
	}

	//Devuelve todos los libros de una lista
	public ArrayList<Libro> GetListaDeLibros(String nombreLista){//Sin servidor
		//Puede que devuelva algo sin actualizar, no podemos cercionarnos
		
		
		ArrayList<String> listaLibros;
		listaLibros=servidor.obtenerLibrosLista(nombreLista, usuarioActual);
		if(listaLibros==null)
			return null;

		for(int j=0;j<listaLibros.size();j++){//Cargamos libros en listas
			Libro lib=bd.DetalleLibroId(listaLibros.get(j));
			if(lib==null){
				lib=api.ObtenerLibroPorId(listaLibros.get(j));
				if(lib!=null){
					try{
					bd.InsertarLibro(lib);
					}catch(SQLiteException e){
						
					}
					AddLibroEnLista(lib,nombreLista);
				}
				/*else
					return null;*/
			}
			else AddLibroEnLista(lib,nombreLista);
		}
		
		
		
		
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			if (lis.getNombreLista().equals(nombreLista))
				i=lista.size();
		}
		return lis.getListaLibros();
	}

	public Boolean BorraLibroDeLista(String idLibro, String nombreLista){
		Boolean correcto=false;
		if(datosActualizados==true){
			ListaLibros lis = null;
			for (int i=0; i<lista.size(); i++) { 
				lis = lista.get(i);
				if (lis.getNombreLista().equals(nombreLista))
					i=lista.size();
			}

			correcto = servidor.borraLibroDeLista(nombreLista, usuarioActual, idLibro);
			if (correcto){
				lis.borraLibroPorId(idLibro);
				try{
				bd.BorrarLibro(idLibro);
				}catch(SQLiteException e){
					e.printStackTrace();
				}
			}
		}else
			return false;//Intentar actualizar?
		return correcto;
	}

	public Boolean AddLibroEnLista(Libro lib, String nombreLista){
		Boolean correcto=false;
		if(datosActualizados==true){
			ListaLibros lis = null;
			for (int i=0; i<lista.size(); i++) { 
				lis = lista.get(i);
				if (lis.getNombreLista().equals(nombreLista))
					i=lista.size();
			}
			try{
				correcto = servidor.agregaLibroALista(nombreLista, usuarioActual, lib.getId());
			}catch(SQLiteException e){}
			if (correcto){
				try{
					lis.addLibro(lib);
					Libro libro=bd.DetalleLibroId(lib.id);
					if(libro==null) bd.InsertarLibro(lib);
					bd.InsertarLibroEnLista(nombreLista, lib);
					}catch(SQLiteException e){
					e.printStackTrace();
				}
			}
		}else
			return false; //Intentar actualizar?
		return correcto;
	}


	public ArrayList<String> getNombresListas() {
		//Tampoco podemos saber si devuelve lo correcto
		ArrayList<String> nombres = new ArrayList<String>(); 
		ListaLibros lis = null;
		for (int i=0; i<lista.size(); i++) { 
			lis = lista.get(i);
			nombres.add(lis.getNombreLista());
		}
		return nombres;
	}

	//Libros compartidos:

	public ArrayList<ParLibroUsuario> getListaCompletaDeLibrosCompartibles(){
		return shared.getListaCompartibles();
	} //Error si devuelve null

	public ArrayList<String> quienTieneElLibro(String IdLibro){
		return shared.quienTieneElLibro(IdLibro);
	} //Error si devuelve null

	public boolean AddLibroEnCompartibles(String IdLibro){
		if(datosActualizados==true){

			return shared.AddLibroUsuario(IdLibro);
		}
		else
			return false;
	} //Si false, ha habido error

	public boolean BorraLibroDeCompartibles(String IdLibro){
		if(datosActualizados==true)
			return shared.BorraLibroUsuario(IdLibro);
		else
			return false;
	} //Si false, ha habido error
}
