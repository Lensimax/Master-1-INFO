/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
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

import java.io.*;
import java.net.*;

public class RegistryClient {
    public static void main(String[] args) throws Exception {

        if (args.length != 2) {
            System.err.println(
                "Usage: java EchoClient <host name> <port number>");
            System.exit(1);
        }

        String hostName = args[0];
        int portNumber = Integer.parseInt(args[1]);

        try (
             Socket echoSocket = new Socket(hostName, portNumber);
             ObjectOutputStream objectOutput = new ObjectOutputStream(echoSocket.getOutputStream());
             ObjectInputStream objectInput = new ObjectInputStream(echoSocket.getInputStream());
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))
             ) {
                String userInput;
                String typeOperation;
                String name, phoneNumber;
                Object inputObject;
                while (true) {
                    System.out.println("Choisissez une opération:\n - ajout: ajouter personne \n - num: recuperer numero personne \n - lister: lister le repertoire\n - pers: recuperer une personne" );
                    if ((userInput = stdIn.readLine()) != null) {

                        typeOperation = userInput;

                    switch(typeOperation){

                    case "ajout":
                        System.out.println("Entrez le nom de la personne");
                        if((userInput = stdIn.readLine()) != null){
                            name = userInput;
                        } else {
                            break;
                        }

                        System.out.println("Entrez le numero de la personne");
                        if((userInput = stdIn.readLine()) != null){
                            phoneNumber = userInput;
                        } else {
                            break;
                        }

                        objectOutput.writeObject(typeOperation);

                        /* envoi de l'object person au serveur */
                        objectOutput.writeObject(new Person(name, phoneNumber));


                        break;
                    case "num":
                        System.out.println("Entrez le nom de la personne");
                        if((userInput = stdIn.readLine()) != null){
                            name = userInput;
                        } else {
                            break;
                        }

                        objectOutput.writeObject(typeOperation);
                        objectOutput.writeObject(name);

                        inputObject = objectInput.readObject();

                        if(inputObject != null){

                            System.out.println("Numéro de la personne "+name+": "+(String)inputObject);
                        } else {

                            System.out.println("Ce nom n'est pas présent dans le répertoire");
                        }

                        break;
                    case "lister":

                        objectOutput.writeObject(typeOperation);

                        System.out.println("Répertoire:");
                        while((inputObject = objectInput.readObject()) != null){
                            System.out.println((Person)inputObject);
                        }

                        break;
                    case "pers":
                        System.out.println("Entrez le nom de la personne");
                        if((userInput = stdIn.readLine()) != null){
                            name = userInput;
                        } else {
                            break;
                        }

                        objectOutput.writeObject(typeOperation);
                        objectOutput.writeObject(name);

                        inputObject = objectInput.readObject();

                        if(inputObject != null){

                            System.out.println("Person: ("+(Person)inputObject+")");
                        } else {

                            System.out.println("Ce nom n'est pas présent dans le répertoire");
                        }

                        break;
                    default:
                        System.out.println("Mauvaise commande");
                        break;
                    }

                } else {
                    break;
                }
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
        }
    }
}
