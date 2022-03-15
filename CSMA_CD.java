


import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * @author BlackDeath47
 */
public class CSMA_CD implements Runnable,FREEINUSE{
    String Nomclient;
    Thread Client;
    int Npacket,MaxNpacket;
    private AtomicBoolean TransmissionSuccess;
    static int tfr=1000; //Tp transmission 1000ms
    Random rand = new Random();
    
    CSMA_CD(String threadname, int MaxNpacket) {
        Nomclient = threadname;
        Client = new Thread(this, Nomclient);
        Npacket = 1;
        this.MaxNpacket=MaxNpacket;
        TransmissionSuccess = new AtomicBoolean();
        Client.start();
    }
 
   
    private void Send() throws InterruptedException{
        Thread.sleep(rand.nextInt(tfr));//Simulation d'envoie qui dure tfr(temps de transmession)     
    }
    
    @Override
    public void run() {
         
        while (!TransmissionSuccess.get()) {
            
            while(Npacket <= MaxNpacket){
                    try {
                        if (Main.ChannelStatus == INUSE) {//Si le channal est occupé, attendre
                            int Stime = rand.nextInt(tfr,2*tfr);
                            System.out.println("Channal Busy! "+this.Nomclient+" va attendre(sleepMode for "+Stime+"ms)");
                            try {
                                Thread.sleep(Stime);
                            }
                            catch (InterruptedException e) {
                                System.out.println(("Interruption dans sleep()"));
                            }
                        }
                        else {//si le channal est libre
                            System.out.println(this.Nomclient + " Envoie le packet numero : " + this.Npacket);
                            if (Main.ChannelStatus == IDLE) {
                                Main.ChannelStatus = INUSE;
                                
                                Send();               
                                
                                System.out.println(" Packet numero " + this.Npacket + " de "+this.Nomclient + " envoyé!");  
                                
                                TransmissionSuccess.set(true);
                                this.Npacket++;
                                Main.ChannelStatus = IDLE;//Libérer le channal
                                
                            }
                            else {//Oops Collision!
                                
                                System.err.println("Collision de packet Numero " + this.Npacket + " de " +this.Nomclient+
                                        " Backing Off..");
                                
                                Main.NBcollisions++;
                                TransmissionSuccess.set(false);
                                Main.ChannelStatus = IDLE;
                                try {    
                                    int BackOffTime = rand.nextInt(500,tfr*Main.NBcollisions);    
                                    System.err.println("retransmession de packet "+this.Npacket+" de "+this.Nomclient+" apres "+BackOffTime+"ms");
                                    Thread.sleep(BackOffTime);       
                                } catch (InterruptedException e) {
                                    System.out.println("Interruption");
                                }
                            }
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        System.out.println(Nomclient + "Interruption");
                    }  
            }
        }      
    }
}
