import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable 
{
    private static Timer timer = new Timer();
    private static Timer checkTimer = new Timer();

    static class CheckTime extends TimerTask 
    {
        static int timeRemaining = 10;
        static boolean isChecking = true;

        public void run()
        {
            if (MyTimerTask.isAuctionOver())
            {
                System.out.println("\nAuction has finished. Loading next item...");
                MyTimerTask.isFinished = false;
            }

            else
            {
                System.out.println("Time Remaining: " + String.valueOf(timeRemaining));
                timeRemaining--;
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
                reply = "Enter your username\n";
                objectOut.writeUTF(reply);
                objectOut.flush();

                String username;
                username = objectIn.readUTF();

                Client c = new Client(username);
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

                if (!MyTimerTask.hasStarted)
                {
                    timer.schedule(new MyTimerTask(auction), 4000, 4000);
                    checkTimer.schedule(new CheckTime(), 0, 1000);
                    MyTimerTask.hasStarted = true;
                    CheckTime.timeRemaining = 10;
                }

                do
                {
                    i = objectIn.readInt();

                    switch (i)
                    {
                        case 1:
                        {
                            objectOut.flush();
                            objectOut.reset();

                            // Send item info thats on sale
                            item = auction.auctionItem();
                            objectOut.writeObject(item);
                            reply = "What would you like to bid? (Must be greater than the current bid)";
                            objectOut.writeUTF(reply);
                            objectOut.flush();

                            float bid = objectIn.readFloat();

                            boolean bidPlaced = auction.placeBid(bid, c);

                            if(bidPlaced)
                            {
                                reply = "\nCongrats you are now the highest bidder with " + String.valueOf(bid) + "!";
                                objectOut.writeUTF(reply);
                                objectOut.flush();
                                timer.cancel();
                                
                                timer = new Timer();
                                timer.schedule(new MyTimerTask(auction), 4000, 4000);
                                
                                CheckTime.timeRemaining = 10;

                                System.out.println("\nNew bid was placed. Resetting timer...");
                            }

                            else
                            {
                                reply = "That didn't work!";
                                objectOut.writeUTF(reply);
                                objectOut.flush();
                            }
                            break;
                        }

                        case 2:
                        {
                            String itemName = objectIn.readUTF();
                            float startingBid = objectIn.readFloat();
                            auction.addItem(itemName, startingBid);

                            if (!CheckTime.isChecking)
                            {
                                checkTimer = new Timer();
                                timer = new Timer();
                                checkTimer.schedule(new CheckTime(), 0, 1000);
                                timer.schedule(new MyTimerTask(auction), 10000, 10000);
                                CheckTime.isChecking = true;
                            }

                            auction.listAuctionItems();
                            break;
                        }

                        case 3:
                        {
                            reply = "Thanks for taking part!\nGoodbye!";
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                            this.stop();
                            this.s.close();
                            auction.removeClient(c);
                            break;
                        }

                        default:
                        {
                            reply = "You entered invalid input";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                        }
                    }
                } while(!exit);

                s.close();
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
        final Auction auction;
        private static boolean isFinished;
        private static boolean hasStarted = false;

        public MyTimerTask(Auction auction) 
        {
            isFinished = false;
            this.auction = auction;
        }

        @Override
        public void run() 
        {
            if (auction.areThereItems())
            {
                auctionNextItem();
                isFinished = true;
            }

            else
            {
                System.out.println("No more items!");
                checkTimer.cancel();
                timer.cancel();
                CheckTime.isChecking = false;
            }
        }

        public static boolean isAuctionOver() 
        {
            return isFinished;
        }

        public void auctionNextItem()
        {
            CheckTime.timeRemaining = 10;
            auction.AnnounceWinner();
        }
    }
}