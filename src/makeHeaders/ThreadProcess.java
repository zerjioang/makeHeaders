package makeHeaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Classes;
import model.Method;
import model.NameSpace;

public class ThreadProcess extends Thread{

	private final File[] files;
	private final String type;
	@SuppressWarnings("unused")
	private String fileData;
	private String language;
	private String extension;
	
	public ThreadProcess(File[] files, String type) {
		this.files = files;
		this.type = type;
	}

	public void run(){
		Controller.getInstance().setStatus("Processing "+files.length+" files");
		for (File f : files) {
			Controller.getInstance().setStatus("Detecting filetype of "+f.getName());
			detectData(f);
			Controller.getInstance().setStatus("Processing "+f.getName());
			fileData = getFileData(f);
			System.out.println(type+" "+f.getName());
			if(type.equals(DandDLabel.CODE)){
				generateCodeFromHeaders(f);
			}
			else if(type.equals(DandDLabel.HEADER)){
				generateHeadersFromCode(f);
			}
		}
	}

	private void detectData(File f) {
		
		String name = f.getName();
		name = name.toLowerCase();
		
		if(name.endsWith(".h")){
			extension = ".h";
		}
		else if(name.endsWith(".c")){
			extension = ".c";
		}
		else if(name.endsWith(".cpp")){
			extension = ".cpp";
		}
	}

	private String getFileData(File f) {
		String data = "";
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			Controller.getInstance().setStatus("Reading "+f.getName());
			while((linea=br.readLine())!=null){
				data+=linea;
				if(!linea.startsWith("/*") && !linea.startsWith("//") && ( linea.contains("typedef struct") || linea.contains("struct"))){
					language = "C";
					break;
				}
				else if(!linea.startsWith("/*") && !linea.startsWith("//") && (linea.contains("namespace ") || linea.contains("class ") || linea.contains("private:")  || linea.contains("public:") ) ){
					language = "CPP";
					break;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){
				System.out.println(e2.getMessage());
			}
		}
		return data;
	}

	private void generateHeadersFromCode(File f) {
		
		String name = f.getName();
		name = name.toLowerCase();
		
		if(name.equals("main.c") || name.equals("main.cpp")){
			Controller.getInstance().setStatus("Main files are not allowed");
		}
		else if(extension.equals(".c")){
			Controller.getInstance().setStatus("Generating header files - C");
			generateCHeaders(f);
		}
		else if(extension.equals(".cpp")){
			Controller.getInstance().setStatus("Generating header files - C++");
			generateCPPHeaders(f);
		}
		else{
			showInvalidInputFileError();
		}
	}

	private void generateCodeFromHeaders(File f) {
		
		if(extension.equals(".h")){
			String language = getLanguage();
			if(language.equals("C")){
				Controller.getInstance().setStatus("Generating body files from headers - C");
				System.out.println("Generating body files from headers - C");
				generateCCode(f);
			}
			else if(language.equals("CPP")){
				Controller.getInstance().setStatus("Generating body files from headers - CPP");
				System.out.println("Generating body files from headers - CPP");
				generateCPPCode(f);
			}
			else{
				showInvalidInputFileError();
			}
		}
		else{
			showInvalidInputFileError();
		}
	}

	private void showInvalidInputFileError() {
		if(files.length>1){
			Controller.getInstance().setStatus("Invalid input files");
		}
		else{
			Controller.getInstance().setStatus("Invalid input file");
		}
	}

	private void generateCPPHeaders(File f) {

		NameSpace ns = new NameSpace();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;

			String CPPMethodEr = "((?:[a-zA-Z_][a-zA-Z0-9_]*)(\\s)*)*(:)(:)((?:[a-zA-Z_~][a-zA-Z0-9_~]*)\\(.*\\)((\\s)*(?:[a-zA-Z_][a-zA-Z0-9_]*)(\\s)*)*)";
		    Pattern p = Pattern.compile(CPPMethodEr,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

			while((linea=br.readLine())!=null){
				
				Matcher m = p.matcher(linea);
			    if (m.find())
			    {
			        String className=m.group(1);
			        String methodName=m.group(5);
			        int pos = NameSpace.getClassIndex(className);
			        if(pos==-1){
			        	Classes c = new Classes(className);
			        	ns.getClasses().add(c);
			        	NameSpace.addClassIndex(className, ns.getClasses().size()-1);
			        	int last = ns.getClasses().size()-1;
			        	ns.getClasses().get(last).getMethod().add(new Method(methodName));
			        }
			        else{
			        	ns.getClasses().get(pos).getMethod().add(new Method(methodName));
			        }
					System.out.print(methodName+"\n");
			    }
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){
				System.out.println(e2.getMessage());
				e2.printStackTrace();
			}
		}
		FileWriter fichero = null;
		PrintWriter pw = null;
		try
		{
			String codeFileName = f.getName();
			codeFileName = codeFileName.replace(".cpp", ".h");
			fichero = new FileWriter(codeFileName);
			pw = new PrintWriter(fichero);
			//write in the file
			pw.println("/*");
			pw.println(" * "+codeFileName);
			pw.println(" *");
			pw.println(" * Created on: "+new SimpleDateFormat().format(new Date()).toString());
			pw.println(" * Author: makeHeaders");
			pw.println(" */");
			pw.println("");
			//insert include declarations
			String def = codeFileName.toUpperCase();
			def = def.replace(".", "_");
			def.concat("_");
			pw.println("#ifndef "+def);
			pw.println("#define "+def);
			//pw.println("#include <stdlib.h>");
			pw.println("");
			pw.println("// Add your includes here");
			pw.println("//");
			pw.println("// End of includes declaration");
			pw.println("");
			pw.println("namespace /*namespaceName*/");
			pw.println("{");
			ArrayList<Classes> c = ns.getClasses();
			for (Classes cls : c) {
				ArrayList<Method> m = cls.getMethod();
				pw.println("");
				pw.println("\tclass "+cls.getName());
				pw.println("\t{");
				pw.println("\tprivate:");
				pw.println("\t\t //Declare your private methods and variables here");
				pw.println("\tpublic:");
				pw.println("\t\t //Declare your own public methods");
				for (Method method : m) {
					String name = method.getName();
					name = name.replace(codeFileName.substring(0, codeFileName.indexOf("."))+"::", "");
					pw.println("\t\t"+name+";");
				}
				pw.println("\t};");
			}
			pw.println("}");
			pw.println("");
			pw.println("#endif /* "+def+" */");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		NameSpace.clear();
	}

	private void generateCHeaders(File f) {

		Classes c = new Classes();
		FileReader fr = null;
		BufferedReader br = null;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;

			String methodRE = "((([a-zA-Z][a-zA-Z0-9_]*))(\\s+))+((?:[a-zA-Z_~][a-zA-Z0-9_]*))(\\(.*\\))";

		    Pattern p = Pattern.compile(methodRE,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

			while((linea=br.readLine())!=null){
				Matcher m = p.matcher(linea);
				if (m.find())
				{
					/*String var1=m.group(1).trim();
					String var4=m.group(4);
					String var5=m.group(5);
					String var6=m.group(6);
					String name = var1+var4+var5+var6;*/
					String name = m.group(0);
					c.getMethod().add(new Method(name));
					System.out.print(name+"\n");
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){
				System.out.println(e2.getMessage());
				e2.printStackTrace();
			}
		}
		System.out.println("Detected methods: "+c.getMethod().size());
		FileWriter fichero = null;
		PrintWriter pw = null;
		try
		{
			String codeFileName = f.getName();
			codeFileName = codeFileName.replace(".c", ".h");
			fichero = new FileWriter(codeFileName);
			pw = new PrintWriter(fichero);
			//write in the file
			pw.println("/*");
			pw.println(" * "+codeFileName);
			pw.println(" *");
			pw.println(" * Created on: "+new SimpleDateFormat().format(new Date()).toString());
			pw.println(" * Author: makeHeaders");
			pw.println(" */");
			pw.println("");
			//insert include declarations
			String def = codeFileName.toUpperCase();
			def = def.replace(".", "_");
			def.concat("_");
			pw.println("#ifndef "+def);
			pw.println("#define "+def);
			//pw.println("#include <stdlib.h>");
			pw.println("");
			pw.println("// Add your includes here");
			pw.println("//");
			pw.println("// End of includes declaration");
			pw.println("");
			pw.println("typedef struct");
			pw.println("{");
			pw.println("\t//Define your structure here");
			String className = codeFileName.replace(".h", "");
			String first = String.valueOf(className.charAt(0));
			first = first.toUpperCase();
			className = first+className.substring(1);
			pw.println("}"+className+";");
			pw.println("");
			ArrayList<Method> mthds = c.getMethod();
			for (Method m : mthds) {
				pw.println(m.getName()+";");
				pw.println("");
			}
			pw.println("#endif /* "+def+" */");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private void generateCPPCode(File f) {
		//process .h file
		ArrayList<NameSpace> p = new ArrayList<NameSpace>();

		FileReader fr = null;
		BufferedReader br = null;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			while((linea=br.readLine())!=null){
				//extract methods of the header file
				if(linea.startsWith("namespace")){
					//save namespace name
					NameSpace pack = new NameSpace();
					pack.setName(linea);
					System.out.println(linea);
					p.add(pack);
				}
				else if(linea.contains("class ") && !linea.startsWith("/*") && !linea.startsWith("//")){
					int lastPackage = p.size()-1;
					Classes c =new Classes();
					c.setName(linea.substring(linea.indexOf(" ")).trim());
					c.setName(c.getName().replace("{", ""));
					System.out.println(c.getName());
					System.out.println(lastPackage);
					if(lastPackage<0){
						//there are no namespace declarations
						p.add(new NameSpace());
						p.get(0).getClasses().add(c);
					}
					else{
						p.get(lastPackage).getClasses().add(c);
					}
				}
				else if(linea.endsWith(";") && linea.contains("(") && linea.contains(")")){
					linea = linea.substring(0, linea.length()-1);
					linea = linea.substring(1);
					int lastPackage = p.size()-1;
					int lastClass = p.get(lastPackage).getClasses().size()-1;
					Method m = new Method(linea);
					System.out.println(m.getName());
					//Add method prefixe
					String str = m.getName();
					int openParenthesis = str.indexOf("(");
					int spacePos = str.substring(0, openParenthesis).lastIndexOf(" ");
					String className = p.get(lastPackage).getClasses().get(lastClass).getName();
					if(spacePos==-1){
						//there are no spaces on method name
						str = "\t"+className+"::"+str.substring(1);
					}
					else{
						String left = str.substring(0, spacePos);
						String right = str.substring(spacePos+1);
						str = left+" "+className+"::"+right;
					}
					System.out.println(str);
					m.setName(str);
					p.get(lastPackage).getClasses().get(lastClass).getMethod().add(m);
				};
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){
				System.out.println(e2.getMessage());
				e2.printStackTrace();
			}
		}
		/*System.out.println("Detected namespaces: "+methodNames.size());
		System.out.println("Detected classes: "+classes);
		System.out.println("detected methods: "+methods);*/
		//generate .cpp file
		FileWriter fichero = null;
		PrintWriter pw = null;
		try
		{
			String codeFileName = f.getName();
			codeFileName = codeFileName.replace(".h", ".cpp");
			fichero = new FileWriter(codeFileName);
			pw = new PrintWriter(fichero);

			//writes information about file itself
			pw.println("/*");
			pw.println(" * "+codeFileName);
			pw.println(" *");
			pw.println(" * Created on: "+new SimpleDateFormat().format(new Date()).toString());
			pw.println(" * Author: makeHeaders");
			pw.println(" */");
			pw.println("");
			//insert include declarations
			pw.println("#include \""+f.getName()+"\"");
			//pw.println("#include <iostream>");
			pw.println("// Add your includes here");
			pw.println("//");
			pw.println("// End of includes declaration");
			pw.println("");
			//insert detected namespaces
			for (NameSpace packages : p) {
				if(packages.getName()!=null){
					//namespace declared
					pw.println(packages.getName());
					pw.println("{");
				}
				ArrayList<Classes> cls = packages.getClasses();
				System.out.println("Clases: "+cls.size());
				for (Classes c : cls) {
					ArrayList<Method> m = c.getMethod();
					System.out.println("Metodos: "+m.size());
					codeFileName.replace(".cpp", "");
					for (Method method : m) {
						pw.println(method.getName());
						pw.println("\t{");
						pw.println("\t\t/*Add code body here*/");
						pw.println("\t}");
						pw.println("");
					}
				}
				if(packages.getName()!=null){
					//namespace declared
					pw.println("}");
					pw.println("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//System.out.println(e.getMessage());
		} finally {
			try {
				// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				e2.printStackTrace();
				System.out.println(e2.getMessage());
			}
		}
		Controller.getInstance().setStatus("Processing Completed");
	}

	private void generateCCode(File f) {
		//process .h file
		Classes classes = new Classes();
		FileReader fr = null;
		BufferedReader br = null;
		int methods=0;
		try {
			// Apertura del fichero y creacion de BufferedReader para poder
			// hacer una lectura comoda (disponer del metodo readLine()).
			fr = new FileReader (f);
			br = new BufferedReader(fr);
			// Lectura del fichero
			String linea;
			while((linea=br.readLine())!=null){
				//extract methods of the header file
				if(linea.endsWith(";") && linea.contains("(") && linea.contains(")")){
					linea = linea.substring(0, linea.length()-1);
					classes.getMethod().add(new Method(linea));
					methods++;
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}finally{
			// En el finally cerramos el fichero, para asegurarnos
			// que se cierra tanto si todo va bien como si salta 
			// una excepcion.
			try{                    
				if( null != fr ){   
					fr.close();     
				}                  
			}catch (Exception e2){
				System.out.println(e2.getMessage());
			}
		}
		System.out.println("detected methods: "+methods);
		//generate .c file
		FileWriter fichero = null;
		PrintWriter pw = null;
		try
		{
			String codeFileName = f.getName();
			codeFileName = codeFileName.replace(".h", ".c");
			fichero = new FileWriter(codeFileName);
			pw = new PrintWriter(fichero);

			pw.println("/*");
			pw.println(" * "+codeFileName);
			pw.println(" *");
			pw.println(" * Created on: "+new SimpleDateFormat().format(new Date()).toString());
			pw.println(" * Author: makeHeaders");
			pw.println(" */");
			pw.println("");
			//insert include declarations
			pw.println("#include \""+f.getName()+"\"");
			//pw.println("#include <stdlib.h>");
			pw.println("// Add your includes here");
			pw.println("//");
			pw.println("// End of includes declaration");
			pw.println("");
			//insert detected methods
			ArrayList<Method> m = classes.getMethod();
			for (Method method : m) {
				pw.println(method.getName());
				pw.println("{");
				pw.println("\t");
				pw.println("}");
				pw.println("");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				// Nuevamente aprovechamos el finally para 
				// asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				System.out.println(e2.getMessage());
			}
		}
		Controller.getInstance().setStatus("Processing Completed");
	}

	private String getLanguage() {
		Controller.getInstance().setStatus("Detecting file language");
		return language;
	}
}
