import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable 
{
    private static Timer timer = new Timer();
    private static Timer checkTimer = new Timer();

    private static TimerTask check = new TimerTask() {
        int num2 = 0;
        public void run()
        {
            // Announce winner of auction and move onto next item
            if (MyTimerTask.isAuctionOver())
            {
                System.out.println("WORKS");
                checkTimer.cancel();
            }

            else
            {
                System.out.println("working so far" + String.valueOf(num2));
                num2++;
            }
        }
    };

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
                // MyTimerTask task = new MyTimerTask(auction);
                if (!MyTimerTask.hasStarted)
                {
                    timer.schedule(new MyTimerTask(auction), 60000);
                    // checkTimer.schedule(check, 0, 1000);
                }
                
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
                            // Send item info thats on sale
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
                                timer.cancel();
                                timer = new Timer();
                                // task = new MyTimerTask(auction);
                                timer.schedule(new MyTimerTask(auction), 6000);
                                System.out.println("got here (new timer)");
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

    static class MyTimerTask extends TimerTask  
    {
        private Item item;
        final Auction auction;
        private static boolean isFinished = false;
        private static boolean hasStarted = false;

        public MyTimerTask(Auction auction) 
        {
            this.auction = auction;
            item = auction.auctionItem();
        }

        @Override
        public void run() 
        {
            hasStarted = true;
            // Announce winner of auction and move onto next item
            System.out.println("you entered the other timer class");
            auctionNextItem();
            System.out.println("passed auction next()");
            isFinished = true;
        }

        public static boolean isAuctionOver() 
        {
            return isFinished;
        }

        public void auctionNextItem()
        {
            auction.AnnounceWinner();
        }
    }
}