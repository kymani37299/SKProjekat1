package com.mare.jovan;

import java.util.Scanner;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.user.User;
import com.mare.jovan.user.UserPermission;

/**
 * Hello world!
 *
 */
public class App 
{
	private static final boolean LOCAL_STORAGE = false;
	private static Scanner sc;
	private static boolean appActive = true;
	private static IStorage storage;
	private static IConnection connection;
	private static String helpText = "help\r\n" + 
			"mkdir <ime>\r\n" + 
			"upload <src> <dest> -m=<description>\r\n" + 
			"delete <target>\r\n" + 
			"download <target> <dest>\r\n" + 
			"list -name=<nameFilter> -ext=<extFilter> -path=<pathFilter> -meta=<Y/N> -type=<DIR/FILE";
	
	private static void registerAdmin() {
		System.out.println("Dobrodosli na SkladisteApp.\nRegistracija\n-------------\nUpisite username:");
		String username = sc.nextLine();
		System.out.println("Upisite password: ");
		String password = sc.nextLine();
		// connection.createAdmin(user);
		UserPermission userPermission = new UserPermission();
		userPermission.create = true;
		userPermission.delete = true;
		userPermission.download = true;
		User admin = new User(username,password,userPermission);
		admin.setAdmin(true);
		connection.addUser(admin);
		connection.login(admin.getUsername(), admin.getPassword());
		System.out.println("Registracija je uspesno zavrsena!");
	}
	
	private static void requestLogin() {
		boolean result = false;
		do {
			System.out.println("Unesite username: ");
			String username = sc.nextLine();
			System.out.println("Upisite lozinku: ");
			String password = sc.nextLine();
			result = connection.login(username, password);
			if(!result) {
				System.out.println("Pogresna lozinka!");
			}
		} while(!result);
	}
	
	private static void processCommand(String command) {
		String params[] = command.split(" ");
		boolean result=true;
		if(params[0].length()==0) {
			return;
		} else if(params[0].equals("exit")) {
			appActive = false;
		} else if(params[0].equals("mkdir") && params.length==2) {
			result = storage.create(new Directory(params[1]));
		} else if(params[0].equals("upload") && params.length==3) {
			result = storage.upload(params[1], new File(params[2]));
		} else if(params[0].equals("delete") && params.length==2) {
			result = storage.delete(new File(params[1]));
		} else if(params[0].equals("download") && params.length==3) {
			result = storage.download(new File(params[1]), params[2]);
		} else if(params[0].equals("list")) {
			ListParams listParams = new ListParams();
			for(int i=1;i<params.length;i++) {
				String tmpParams[] = params[i].split("=");
				if(tmpParams.length==2) {
					if(tmpParams[0].equals("-name")) {
						listParams.setNameFilter(tmpParams[1]);
					} else if(tmpParams[0].equals("-ext")) {
						listParams.setExtFilter(tmpParams[1]);
					} else if(tmpParams[0].equals("-path")) {
						listParams.setPath(new File(tmpParams[1]));
					} else if(tmpParams[0].equals("-meta")) {
						listParams.setShowMetadata(tmpParams[1].equals("Y"));
					} else if(tmpParams[0].equals("-type")) {
						if(tmpParams[1].equals("DIR")) {
							listParams.setNameFilter("DIR");
						} else { 
							listParams.setNameFilter("FILE");
						}
					}
				}
			}
			for(File f : storage.list(listParams)) {
				if(listParams.isShowMetadata()) {
					System.out.println("[" + f.getMetadata() + "]");
				}
				System.out.println(f.getPath());
			}
			return;
		} else if(params[0].equals("adduser") && params.length==4) {
			UserPermission permissions = new UserPermission();
			permissions.create = params[3].charAt(0)=='1';
			permissions.delete = params[3].charAt(1)=='1';
			permissions.download = params[3].charAt(2)=='1';
			User user = new User(params[1],params[2],permissions);
			result = connection.addUser(user);
		} else if(params[0].equals("banuser") && params.length==2) {
			result = connection.banUser(params[1]);
		} else if(params[0].equals("logout")) {
			connection.logout();
		}
		else {
			if(!params[0].equals("help"))
				System.out.println("Invalid command. See help:");
			System.out.println(helpText);
			return;
		}
		
		if(result) {
			System.out.println("Uspesno izvrsena koamnda!");
		} else {
			System.out.println("Doslo je do greske prilikom izvrsavanja komande!");
		}
	}
	
    public static void main( String[] args )
    {
        sc = new Scanner(System.in);
        
        if(LOCAL_STORAGE) {
        	connection = new LocalConnection();
        } else {
        	connection = new DropboxConnection();
        }
        
        while(appActive) {
        	if(connection.noUsers() || !connection.isLoggedIn()) {
        		if(connection.noUsers()) {
        			registerAdmin();
        		} else {
        			requestLogin();
        		}
                storage = connection.getStorage();
                if(storage==null) {
                	System.out.println("Greska u ucitavanju skladista. Pokrenite ponovo aplikaciju!");
                	appActive = false;
                }
                System.out.println("Uspesno ucitavanje skladista. Unesite komandu: ");
            }
        	String line = sc.nextLine();
        	processCommand(line);
        }
        sc.close();
        
    }
}
