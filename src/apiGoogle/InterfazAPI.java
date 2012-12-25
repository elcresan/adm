package apiGoogle;

import java.util.ArrayList;
import java.util.Random;

import listasLibros.Libro;

public class InterfazAPI {
	
	Libro libro1;
	ArrayList<Libro> ListaDeLibros;
	
	public InterfazAPI(){
		////////  LIBROS DE PRUEBA  ///////////
		for (int i=1;i<6;i++){
			Random generator = new Random();
			ArrayList<String> autores=new ArrayList<String>();
			autores.add("Franz Beckenbauer");
			autores.add("David Albelda");
			ArrayList<String> categorias=new ArrayList<String>();
			categorias.add("Ficci�n");
			categorias.add("Aventuras");

			libro1=new Libro("00"+generator.nextInt());

			libro1.setAutores(autores);
			libro1.setCategorias(categorias);
			libro1.setDescripcion(" Descripci�n del libro bla bla bla \n Lineanueva bla bla bla Batman!");
			libro1.setEditorial("Editorial Lluis Vives");
			libro1.setFechaPublicacion("21 diciembre de 2012");
			libro1.setIdioma("Franchute");
			libro1.setNumeroDePaginas(435);
			libro1.setPais("Francia");
			libro1.setPuntuacionMedia((float) 7.23);
			libro1.setTitulo("The lord de los anillos");
			ListaDeLibros.add(libro1);
		}
		///////// FIN LIBROS DE PRUEBA ///////////
	}
	public Libro ObtenerLibroPorId(String Id){
		
		return libro1;	
	}
	
	public ArrayList<Libro> ObtenerListaLibrosPopulares(){
		
		return ListaDeLibros;
	}
public ArrayList<Libro> ObtenerListaLibrosPorAutor(String autor){
		
		return ListaDeLibros;
	}
public ArrayList<Libro> ObtenerListaLibrosPorBusqueda(String busqueda){
	//No se como funcionar� lo de la api, pero de alguna forma se podr�n buscar libros,
	//si hay que hacerlo por separado o no ya no lo s�, simplemente a�ade m�s funciones
	//si hace falta, en plan buscar por editorial o por cualquier campo que pueda ser util
	return ListaDeLibros;
}
	
	
}
