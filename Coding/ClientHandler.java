import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable 
{ 
    final ObjectInputStream objectIn; 
    final ObjectOutputStream objectOut; 
    final Socket s;
    final Auction auction;
    boolean fact;
    final ArrayList<String> menu;

    // Constructor 
    public ClientHandler(Socket s, Auction auction, ObjectInputStream objectIn, ObjectOutputStream objectOut)  
    {
        this.s = s;
        this.auction = auction;
        this.objectIn = objectIn;
        this.objectOut = objectOut;
        menu = auction.displayMenu();
        fact = true;
    } 
  
    @Override
    public void run()  
    { 
        String reply; 
        while (fact)  
        { 
            try 
            { 
                // MyTimerTask task = new MyTimerTask(auction.auctionItem());
                // checkTimer.schedule(check, 0, 1000);
                // timer.schedule(task, 6000);
                // System.out.println("winner declared here" + sold.getHighestBidder().getUsername());
                // auction.startTimer();
                // auction.getTimeRemaining();

                reply = "Enter your username and password\n";
                System.out.println(reply);
                objectOut.writeUTF(reply);
                objectOut.flush();

                String username, password;
                username = objectIn.readUTF();
                password = objectIn.readUTF();

                Client c = new Client(username, password);
                boolean clientAdded = auction.addClient(c);
                if(clientAdded)
                {
                    System.out.println("client added to auction");
                }

                else
                {
                    System.out.println("User already exists");
                }

                objectOut.writeObject(menu);

                Item item = auction.auctionItem();
                objectOut.writeObject(item);

                int i = 0;

                do
                {
                    i = objectIn.readInt();
                    // Check timer
                    switch (i)
                    {
                        case 1:
                        {
                            // Check timer
                            objectOut.flush();
                            objectOut.reset();
                            // Send item infor thats on sale
                            item = auction.auctionItem();
                            objectOut.writeObject(item);
                            reply = "What would you like to bid? (Must be greater than the current bid)\n";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();

                            float bid = objectIn.readFloat();
                            // Check timer
                            boolean bidPlaced = auction.placeBid(bid, c);

                            if(bidPlaced)
                            {
                                reply = "Congrats you are now the highest bidder!\n";
                                System.out.println(reply);
                                objectOut.writeUTF(reply);
                                objectOut.flush();
                                // auction.stopTimers();
                            }

                            else
                            {
                                reply = "That didn't work!\n";
                                System.out.println(reply);
                                objectOut.writeUTF(reply);
                                objectOut.flush();
                            }

                            break;
                        }

                        case 2:
                        {
                            // auction.listAuctionItems();
                            reply = "You want to create a new auction\n";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                            // try {
                            //  auction = (Auction) objectIn.readObject();
                            // } catch(Exception e) {
                            //  System.out.println(e);
                            // }
                            String itemName = objectIn.readUTF();
                            float startingBid = objectIn.readFloat();
                            auction.addItem(itemName, startingBid);
                            // objectOut.writeObject(auction);
                            auction.listAuctionItems();
                            System.out.println("test2 should be above!\n");
                            break;
                        }

                        case 3:
                        {
                            reply = "You want to leave auction\n";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                            this.s.close();
                            break;
                        }

                        default:
                        {
                            reply = "You entered invalid input\n";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                        }
                    }
                    // objectOut.writeObject(auction);
                }while(i < 6);

                s.close();
                // ss.close();
            

                // static class MyTimerTask extends TimerTask  
                // {
                //     private Item item;
                //     private static boolean isFinished = false;

                //     public MyTimerTask(Item item) 
                //     {
                //         this.item = item;
                //     }

                //     @Override
                //     public void run() 
                //     {
                //         // Announce winner of auction and move onto next item
                //         System.out.println("you entered the other timer class");
                //         // returnItem();
                //         isFinished = true;
                //     }

                //     public static boolean isAuctionOver() 
                //     {
                //         return isFinished;
                //     }

                //     // public Item returnItem()
                //     // {

                //     // }
                // }
            }
            catch (IOException e) { 
                e.printStackTrace(); 
            } 
        }

        try
        { 
            // closing resources 
            this.objectIn.close(); 
            this.objectOut.close();      
        }
        catch(IOException e) { 
            e.printStackTrace(); 
        }
    }
}