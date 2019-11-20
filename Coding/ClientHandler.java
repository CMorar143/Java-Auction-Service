import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable 
{
    private static Timer timer = new Timer();
    private static Timer checkTimer = new Timer();

    static class CheckTime extends TimerTask 
    {
        static int num2 = 0;
        public void run()
        {
            // Announce winner of auction and move onto next item
            if (MyTimerTask.isAuctionOver())
            {
                System.out.println("WORKS");
                MyTimerTask.isFinished = false;
                // checkTimer.cancel();
            }

            else
            {
                System.out.println("working so far" + String.valueOf(num2));
                num2++;
            }
        }
    }

    final ObjectInputStream objectIn; 
    final ObjectOutputStream objectOut; 
    final Socket s;
    final Auction auction;
    private boolean exit;
    final ArrayList<String> menu;

    // Constructor 
    public ClientHandler(Socket s, Auction auction, ObjectInputStream objectIn, ObjectOutputStream objectOut)  
    {
        this.s = s;
        this.auction = auction;
        this.objectIn = objectIn;
        this.objectOut = objectOut;
        menu = auction.displayMenu();
        exit = false;
    }

    public void stop()
    {
        exit = true;
    }

    @Override
    public void run()  
    { 
        String reply; 
        while (!exit)  
        {
            try 
            {
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

                // MyTimerTask task = new MyTimerTask(auction);
                if (!MyTimerTask.hasStarted)
                {
                    timer.schedule(new MyTimerTask(auction), 8000, 8000);
                    checkTimer.scheduleAtFixedRate(new CheckTime(), 0, 1000);
                    MyTimerTask.hasStarted = true;
                }

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
                                timer.schedule(new MyTimerTask(auction), 8000, 8000);
                                
                                CheckTime.num2 = 0;

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
                            this.stop();
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
                }while(!exit);

                s.close();
                // ss.close();
            }
            catch (IOException e) {
                e.printStackTrace(); 
            }
        }

        System.out.println("CLOSED");

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
        private static int checker = 0;
        private static boolean isFinished;
        private static boolean hasStarted = false;

        public MyTimerTask(Auction auction) 
        {
            isFinished = false;
            this.auction = auction;
            item = auction.auctionItem();
        }

        @Override
        public void run() 
        {
            // hasStarted = true;
            // Announce winner of auction and move onto next item
            System.out.println("you entered the other timer class");
            auctionNextItem();
            System.out.println("passed auction next(" + String.valueOf(checker) + ")");
            checker++;
            isFinished = true;
        }

        public static boolean isAuctionOver() 
        {
            return isFinished;
        }

        public void auctionNextItem()
        {
            auction.AnnounceWinner();
            CheckTime.num2 = 0;
        }
    }
}