/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.net.*;
import java.io.*;
import java.util.Iterator;

public class RegistryServer {

    public static void main(String[] args) throws Exception {
        Calculator calculator = new Calculator();
        if (args.length != 1) {
            System.err.println("Usage: java EchoServer <port number>");
            System.exit(1);
        }

        Registry registry = new Registry();

        int portNumber = Integer.parseInt(args[0]);

        try (
            ServerSocket serverSocket = new ServerSocket(Integer.parseInt(args[0]));
            Socket clientSocket = serverSocket.accept();
            ObjectOutputStream objectOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream objectInput = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            String inputLine;
            String typeCommand;
            Object inputObject;

            System.out.println("Connected");

            while (true) {
                try {

                if((inputLine = (String) objectInput.readObject()) != null){
                    typeCommand = inputLine;

                    switch(typeCommand){

                    case "ajout":

                        try {

                            if( (inputObject = objectInput.readObject()) != null){
                                if(inputObject instanceof Person){
                                    registry.add((Person)inputObject);
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }

                        } catch(Exception e){
                            System.err.println("Erreur lors de la reception de l'objet");
                        }
                        // a enlever
                        System.out.println(registry);

                        break;
                    case "num":
                            inputObject = objectInput.readObject();
                            if(inputObject != null){
                                String tel = registry.getPhone((String) inputObject);
                                objectOutput.writeObject(tel);
                            } else {
                                objectOutput.writeObject(null);
                            }
                        break;
                        case "lister":
                            Iterator it = registry.getAll().iterator();

                            while(it.hasNext()){
                                objectOutput.writeObject(it.next());
                            }

                            /* fin du repertoire */
                            objectOutput.writeObject(null);
                        break;
                        case "pers":
                            inputObject = objectInput.readObject();
                            if(inputObject != null){
                                Person p = registry.search((String) inputObject);
                                objectOutput.writeObject(p);
                            } else {
                                objectOutput.writeObject(null);
                            }

                        break;
                    default:
                        objectInput.readUTF(); /* hardcore jusqu'a la mort :) */
                        break;
                    }
                } else {
                    break;
                }
                } catch(Exception e){
                    System.err.println("MERDE!");
                }
            }

            System.out.println("I'm ending...");
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}
