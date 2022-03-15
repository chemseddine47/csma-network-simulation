/**
 *
 * @author BlackDeath47
 */

import java.util.Scanner;

interface FREEINUSE{ 
    int IDLE = 0;//libre  
    int INUSE = 1;//occupé
 }
/*
Pour la simulation on utilise 3 stations pour transmettre 2 paquets chacune. 
avec option de choisir quel protocole suivait (CSMA/CD, CSMA/CA)
CSMA/CA: La station envoie un paquet et attend pour un ACK (accusé de réception), pour 
cela on génère une valeur aléatoire à la variable "ACK_RECIEVED'" pour dire que là 
station a reçu le ACK ou non, donc ça se peut que la simulation dans ce mode prenne longtemps
*/

public class Main implements FREEINUSE { 
    
    static int NBstations = 3;
    static int NBPacket = 2;
    static int NBcollisions;
    static int ChannelStatus = IDLE;//Le channal est libre au debut de la simulation
    
   public static void main (String args[]) throws InterruptedException{
       
       System.out.println("Pour cette simulation de CSMA on va utiliser 3 Stations avec "
               + "deux packets a envoyer pour chaque Station ");
        
       Scanner sc = new Scanner(System.in);
       
       System.out.print("enter 1 pour CSMA/CD, 2 pour CSMA/CA: "); 
       int choix = sc.nextInt();
       
       if(choix == 1){//CSMA/CD
           
           CSMA_CD[] ClientListe =new CSMA_CD[NBstations+1];//Créer une liste de clients 
            for(int i = 1;i<=NBstations;i++){//Créer les clients (3)
                 ClientListe[i] = new CSMA_CD("Station "+ Integer.toString(i),NBPacket);}
            
            for(int i = 1;i<=NBstations;i++){//Attendre que lesthreads se termine 
                ClientListe[i].Client.join();}
            
       }else if (choix ==2){//CSMA/CA
           
           CSMA_CA[] ClientListe =new CSMA_CA[NBstations+1];
            for(int i = 1;i<=NBstations;i++){//Créer les clients (3)
                 ClientListe[i] = new CSMA_CA("Station "+ Integer.toString(i),NBPacket);}
            for(int i = 1;i<=NBstations;i++){
                ClientListe[i].Client.join();}
       }
       
        System.out.println("Fin Transmission avec "+NBcollisions+" Collisions"); 
   }
    
}
