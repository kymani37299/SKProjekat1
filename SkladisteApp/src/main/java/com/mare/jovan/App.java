package com.mare.jovan;

import java.util.Scanner;

import com.mare.jovan.file.Directory;
import com.mare.jovan.file.File;
import com.mare.jovan.file.FileType;
import com.mare.jovan.user.User;
import com.mare.jovan.user.UserPermission;

/**
 * Hello world!
 *
 */
public class App 
{
	
	private static final String configPath = "connection.cfg";

	private static Scanner sc;
	private static boolean appActive = true;
	private static IStorage storage;
	private static IConnection connection;
	private static String helpText = "mkdir <name>\r\n" + 
			"upload <source> <destination> -m=<description>\r\n" + 
			"delete <target>\r\n" + 
			"download <target> <destination>\r\n" + 
			"list -name=<nameFilter> -ext=<extFilter> -path=<pathFilter> -meta=<Y/N> -type=<DIR|FILE>\r\n" + 
			"adduser <username> <password> <0|1:create><0|1:delete><0|1:download>\r\n" + 
			"banuser <username>\r\n" + 
			"logout\r\n" + 
			"exit";
	
	private static void registerAdmin() {
		System.out.println("Welcome to SkladisteApp.\nRegistration\n-------------\nInsert username:");
		String username = sc.nextLine();
		System.out.println("Insert password: ");
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
		System.out.println("Registration successful!");
	}
	
	private static void requestLogin() {
		boolean result = false;
		do {
			System.out.println("Welcome to SkladisteApp.\nLogin\n-------------\nInsert username:");
			String username = sc.nextLine();
			System.out.println("Insert password: ");
			String password = sc.nextLine();
			result = connection.login(username, password);
			if(!result) {
				System.out.println("Wrong credentials!");
			}
		} while(!result);
	}
	
   private static void printResult(EProcessResult result) {
	   if(result!=null) {
		   switch(result){
	       	case DEST_NOT_VALID:
	       		System.out.println("Destination path not valid.");	break;
			case SOURCE_NOT_VALID:
				System.out.println("Source path not valid.");	break;
			case DENIED_ACCESS:
				System.out.println("User doesn't have permission.");	break;
			case EXTENSION_FORBIDDEN:
				System.out.println("Invalid file extension.");	break;
			case PROCESS_FAILED:
				System.out.println("Process failed.");	break;
			case PROCESS_SUCCESS:
				System.out.println("Process is successful");	break;
			case USER_NOT_FOUND:
				System.out.println("User not found."); break;
			default:
				System.out.println("Humans failed.");
       }
   }
    
}
	
	private static void processCommand(String command) {
		String params[] = command.split(" ");
		EProcessResult result=null;
		if(params[0].length()==0) {
			return;
		} else if(params[0].equals("exit")) {
			appActive = false;
		} else if(params[0].equals("mkdir") && params.length==2) {
			result = storage.create(new Directory(params[1]));
		} else if(params[0].equals("upload") && params.length>=3) {
			File f = new File(params[2]);
			if(params.length>3) {
				String desc = params[3].substring(3);
				for(int i=4;i<params.length;i++) desc+=params[i]+" ";
				f.getMetadata().setDescription(desc);
			}
			result = storage.upload(params[1], f);
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
							listParams.setTypeFilter(FileType.Directory);
						} else { 
							listParams.setTypeFilter(FileType.File);
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
			if(user.getPermission().create)
				System.out.println("create");
			if(user.getPermission().delete)
				System.out.println("delete");
			if(user.getPermission().download)
				System.out.println("download");
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
		
		printResult(result);
	}
	
    public static void main( String[] args )
    {
        sc = new Scanner(System.in);
        if(args.length>0) {
        	connection = ConnectionFactory.getConnection(configPath,args[0]);
        } else {
        	connection = ConnectionFactory.getConnection(configPath);
        }
        
        if(connection==null) {
        	System.out.println("Program initialization failed. Please check config file!");
        	System.exit(0);
        }
        
        appActive = true;
        
        while(appActive) {
        	if(connection.noUsers() || !connection.isLoggedIn()) {
        		if(connection.noUsers()) {
        			registerAdmin();
        		} else {
        			requestLogin();
        		}
                storage = connection.getStorage();
                if(storage==null) {
                	System.out.println("Storage initialization failed. Please restart the app.");
                	appActive = false;
                }
                System.out.println("Please,type the command(type help for help):");
            }
        	String line = sc.nextLine();
        	processCommand(line);
        }
        sc.close();
        
    }
}
