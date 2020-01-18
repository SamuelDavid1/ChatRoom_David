package chatroom.Client_v0;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.Security;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import com.sun.net.ssl.internal.ssl.Provider;

import chatroom.commons.Configuration;
import chatroom.commons.Translator;
import chatroom.server.message.Login;
import chatroom.server.message.Logout;
import chatroom.server.message.ChangePassword;
import chatroom.server.message.CreateChatroom;
import chatroom.server.message.CreateLogin;
import chatroom.server.message.DeleteChatroom;
import chatroom.server.message.DeleteLogin;
import chatroom.server.message.JoinChatroom;
import chatroom.server.message.LeaveChatroom;
import chatroom.server.message.Message;
import chatroom.server.message.Ping;
import chatroom.server.message.SendMessage;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

public class Model{
	ServiceLocator serviceLocator;
	String ipAddress;
	String publicTest;
	SimpleStringProperty messageText = new SimpleStringProperty();
	String messageReceiveText;
	
	public String token;
	public String receiveResult;
	public String test = "Result";
	int Port;
	boolean secure;
	Socket socket;
	private final ObservableList<String> elements = FXCollections.observableArrayList();

	public void init(String ipAddress, int Port, boolean secure) {
	    this.ipAddress = ipAddress;
	    this.Port = Port;
	    this.secure = secure;
	}
	
	public void initialize() {
        new Thread(initializer).start();
    }
	
	final Task<Void> initializer = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            this.updateProgress(1,  6);

            // Create the service locator to hold our resources
            serviceLocator = ServiceLocator.getServiceLocator();
            this.updateProgress(2,  6);

            // Initialize the resources in the service locator
            serviceLocator.setLogger(configureLogging());
            this.updateProgress(3,  6);

            serviceLocator.setConfiguration(new Configuration());
            this.updateProgress(4,  6);

            String language = serviceLocator.getConfiguration().getOption("Language");
            serviceLocator.setTranslator(new Translator(language));
            this.updateProgress(5,  6);
            
            // ... more resources would go here...
            this.updateProgress(6,  6);

            return null;
        }
    };
    
    private Logger configureLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.FINEST);

        // By default there is one handler: the console
        Handler[] defaultHandlers = Logger.getLogger("").getHandlers();
        defaultHandlers[0].setLevel(Level.INFO);

        // Add our logger
        Logger ourLogger = Logger.getLogger(serviceLocator.getAPP_NAME());
        ourLogger.setLevel(Level.FINEST);
        
        // Add a file handler, putting the rotating files in the tmp directory
        try {
            Handler logHandler = new FileHandler("%t/"
                    + serviceLocator.getAPP_NAME() + "_%u" + "_%g" + ".log",
                    1000000, 9);
            logHandler.setLevel(Level.FINEST);
            ourLogger.addHandler(logHandler);
        } catch (Exception e) { // If we are unable to create log files
            throw new RuntimeException("Unable to initialize log files: "
                    + e.toString());
        }

        return ourLogger;
    }	    
        
	
	public void connect(String ipAddress, int Port, boolean secure) {
		
		try {
        	if (secure) {
				// Registering the JSSE provider
				Security.addProvider(new Provider());
				
				// Specifying the Truststore details. This is needed if you have created a
				// truststore, for example, for self-signed certificates
				System.setProperty("javax.net.ssl.trustStore", "truststore.ts");
				System.setProperty("javax.net.ssl.trustStorePassword", "trustme");

				// Creating Client Sockets
				SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
				socket = sslsocketfactory.createSocket(ipAddress, Port);
				
				// The next line is entirely optional !!
				// The SSL handshake would happen automatically, the first time we send data.
				// Or we can immediately force the handshaking with this method:
				((SSLSocket) socket).startHandshake();
			} else {
				socket = new Socket(ipAddress, Port);
				
			}
        	
        	System.out.println(socket.isConnected());
        	Runnable r = new Runnable() {
        		@Override
        		public void run() {
        			try (BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()))){
        				while(true) {
        					String msg = null;
        					try {
        						msg = socketIn.readLine();
        						System.out.println("Received: " + msg);
        					} catch (IOException e) {
        						e.printStackTrace();
        					}
        					String[] parts = msg.split("\\|");
        					if(parts.length > 2) {
        						if(parts[0].equalsIgnoreCase(test)) {
        							if(parts[2] != null) {
        								try {
        									setToken(parts[2]);
        								} catch (Exception e) {
        									e.printStackTrace();
        								}
        							}
        						}
        					}
        					if(parts.length == 2) {
        						if(parts[1] != null) {
        							try {
        								setReceiveResult(parts[1]);
        							} catch (Exception e) {
        								e.printStackTrace();
        							}
        						}
        					}
        					if(parts.length == 4) {
        						if(parts[3] != null) {
        							try {
        								messageText.setValue(parts[3]);
        							} catch (Exception e) {
        								e.printStackTrace();
        							}
        						}
        					}
        					if(parts.length > 2) {
        						if (parts[2] != null) {
        							try {
        								String[] names = new String[] {parts[2]};
        							} catch (Exception e) {
        								e.printStackTrace();
        							}
        						} else {
        							System.out.println("leer :(");
        						}
        					}
        					
        					if (msg == null) break;
        				}
        			} catch (IOException e) {
        				e.printStackTrace();
        			}
        		}

				

				

        		
        	};
        	Thread t = new Thread(r);
        	t.start();
        	
        	String[] data = new String[] {"Ping"};
        	Message msg = new Ping(data);
        	msg.send(socket);
        	
        } catch (Exception e) {
            // TODO Auto-generated catch block
        }
	}
	
	public String createLogin(String username, String password) {
        String result = null;
	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"CreateLogin", username, password};
	    			result = "CreateLogin|" + username + "|" + password;
	    			Message msg = new CreateLogin(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);
		    		
		    		
	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	}
	    	
	    	
	    } 
	    
		return result;
	}
	
	public String Login(String username, String password) {
		String result = null;
	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"Login", username, password};
	    			result = "Login|" + username + "|" + password;
	    			Message msg = new Login(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String createChatroom(String token, String name, boolean isPublic) {
        String result = null;
        String publicTest = String.valueOf(isPublic);
	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"CreateChatroom", token, name, publicTest};
	    			result = "CreateChatroom|" + token + "|" + name + "|" + publicTest;
	    			Message msg = new CreateChatroom(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public void addNewElement(String element) {
		elements.add(element);
	}
	
	public void removeElement(String element) {
		elements.remove(element);
	}
	
	public ObservableList<String> getElements() {
		return elements;
	}
	
	public String joinChatroom(String token, String name, String username) {
        String result = null;

	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"JoinChatroom", token, name, username};
	    		result = "JoinChatroom|" + token + "|" + name + "|" + username;
	    			Message msg = new JoinChatroom(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String leaveChatroom(String token, String name, String username) {
        String result = null;

	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"LeaveChatroom", token, name, username};
	    		result = "LeaveChatroom|" + token + "|" + name + "|" + username;
	    			Message msg = new LeaveChatroom(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String deleteChatroom(String token, String name) {
        String result = null;

	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"DeleteChatroom", token, name};
	    		result = "DeleteChatroom|" + token + "|" + name;
	    			Message msg = new DeleteChatroom(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String sendMessage(String token, String destination, String message) {
        String result = null;

	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"SendMessage", token, destination, message};
	    		
	    			Message msg = new SendMessage(data);
	            	msg.send(socket);
	            	Thread.sleep(1000);
	            	result = this.getMessageText();
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String Logout() {
        String result = null;

	    if (socket != null) {
	    	try {
	    		String[] data = new String[] {"Logout"};
	    		result = "Logout";
	    			Message msg = new Logout(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
	public String getToken() {
		return token;
	}
	
	private void setToken(String token) {
		// TODO Auto-generated method stub
		this.token = token;
	}
	
	private void setReceiveResult(String receiveResult) {
		// TODO Auto-generated method stub
		this.receiveResult = receiveResult;
	}
	
	public String getReceiveResult() {
		return receiveResult;
	}
	
	public SimpleStringProperty messageTextProperty() {
		return messageText;
	}
	
	public String getMessageText() {
		return messageText.get();
	}

	public String deleteLogin(String token) {
		String result = null;
	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"DeleteLogin", token};
	    			result = "DeleteLogin|" + token;
	    			Message msg = new DeleteLogin(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}

	public String changePasswort(String token, String password) {
		String result = null;
	    if (socket != null) {
	    	
	    	try {
	    		String[] data = new String[] {"ChangePassword", token, password};
	    			result = "ChangePassword|" + token + "|" + password;
	    			Message msg = new ChangePassword(data);
	            	msg.send(socket);
		    		System.out.println("Sent: " + result);

	    	} catch (Exception e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    	} 
	    } 
	    
		return result;
	}
	
}
