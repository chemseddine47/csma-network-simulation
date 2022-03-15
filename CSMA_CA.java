

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author BlackDeath47
 */
public class CSMA_CA implements Runnable,FREEINUSE{
    String Nomclient;
    Thread Client;
    int Npacket,MaxNpacket;
    boolean ACK_Recieved;
    int TTL = 1000;
    int tfr= 1000;
    Random rand = new Random();
    private AtomicBoolean TransmissionSuccess;
    
    
    public CSMA_CA(String Nomclient, int MaxNpacket) {
        this.Nomclient = Nomclient;
        this.MaxNpacket = MaxNpacket;
        Client = new Thread(this, Nomclient);
        Npacket = 1;
        TransmissionSuccess = new AtomicBoolean();
        Client.start();
        
    }
    
    private void Send() throws InterruptedException{
        Thread.sleep(rand.nextInt(tfr)); 
    }

    @Override
    public void run() {
        
        while (!TransmissionSuccess.get()) {
            
            while(Npacket<=MaxNpacket){
                
            try{
                if (Main.ChannelStatus == INUSE) {
                    int Stime = rand.nextInt(tfr,2*tfr);
                    System.out.println("Channal Busy! "+this.Nomclient+" va attendre(sleepMode for "+Stime+"ms)");
                    try {
                       Thread.sleep(Stime);
                       }
                       catch (InterruptedException e) {
                                System.out.println(("Interruption dans sleep()"));
                        }
                }else {
                    System.out.println(this.Nomclient + " Envoie le packet numero : " + this.Npacket);
                    if (Main.ChannelStatus == IDLE) {
                        Main.ChannelStatus = INUSE;
                        Send(); 
                        System.out.println(" Packet numero " + this.Npacket + " de "+this.Nomclient + " envoyé! Attente de ACK");
                        
                        //Thread.sleep(TTL);
                        
                        ACK_Recieved = rand.nextBoolean();//génèrer une Valeur aleatoire pour le test
                        
                        if(ACK_Recieved){
                            System.out.println(" Packet: " + this.Npacket + " de "+this.Nomclient + " envoyé avec success!");
                            this.Npacket++;
                            Main.ChannelStatus = IDLE;//Libérer le channal
                            TransmissionSuccess.set(true);
                                    
                        }else {
                            System.err.println(" Coolision de Packet: " + this.Npacket + " de "+this.Nomclient + " BackingOff..");
                            Main.NBcollisions++;
                            TransmissionSuccess.set(false);
                            Main.ChannelStatus = IDLE;
                            try {    
                                int BackOffTime = rand.nextInt(tfr);    
                                System.err.println("retransmession de packet "+this.Npacket+" de "+this.Nomclient+" apres "+BackOffTime+"ms");
                                Thread.sleep(BackOffTime);       
                            } catch (InterruptedException e) {
                                System.out.println("Interruption");
                            }
                        } 
                    }     
                }
                } catch (InterruptedException e) {
                        System.out.println(Nomclient + "Interruption");
                    }
            }
    }
}
}