import java.net.*;
import java.io.*;
import java.util.*;

public class ClientHandler implements Runnable 
{
    // Timer tasks
    private static Timer timer = new Timer();
    private static Timer checkTimer = new Timer();

    private final ObjectInputStream objectIn; 
    private final ObjectOutputStream objectOut; 
    private final Socket s;
    private final Auction auction;
    private boolean exit;
    private final ArrayList<String> menu;

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

    // Gracefully close the client thread if they choose to do so
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
                Client c;

                // Loop until the client enters a username that doesn't exist
                while(true)
                {
                    username = objectIn.readUTF();

                    // If this username isnt already in use
                    if(!auction.doesClientExist(username))
                    {
                        c = new Client(username);
                        auction.addClient(c);
                        System.out.println("client added to auction");
                        objectOut.writeBoolean(true);
                        objectOut.flush();
                        break;
                    }

                    // If this username is already in use
                    else
                    {
                        System.out.println("User already exists");
                        objectOut.writeBoolean(false);
                        objectOut.flush();
                    }
                }

                // Send the menu to display on the client side
                objectOut.writeObject(menu);

                // Get the item thats currently on auction and send to client
                Item item = auction.auctionItem();
                objectOut.writeObject(item);

                // For the displayMenu
                int i = 0;

                // Only start the auction timers after the first client has joined
                // This ensures that there is only everone instance of the timers scheduled
                if (!MyTimerTask.hasStarted)
                {
                    timer.schedule(new MyTimerTask(auction), 4000, 4000);
                    checkTimer.schedule(new CheckTime(), 0, 1000);
                    MyTimerTask.hasStarted = true;
                    CheckTime.timeRemaining = 10;
                }

                while(!exit)
                {
                    i = objectIn.readInt();

                    switch (i)
                    {
                        // Client wants to bid
                        case 1:
                        {
                            objectOut.flush();
                            objectOut.reset();

                            // Resend the item currently on auction
                            item = auction.auctionItem();
                            objectOut.writeObject(item);
                            
                            // If there is still at least one item left
                            if(item != null)
                            {
                                reply = "What would you like to bid? (Must be greater than the current bid)";
                                objectOut.writeUTF(reply);
                                objectOut.flush();

                                // Read bid from client
                                float bid = objectIn.readFloat();

                                // Check if this is more than the previous bid
                                boolean bidPlaced = auction.placeBid(bid, c);

                                // If the bid was successful
                                // i.e. If the new bid was higher than the previous bid
                                if(bidPlaced)
                                {
                                    reply = "\nCongrats you are now the highest bidder with " + String.valueOf(bid) + "!";
                                    objectOut.writeUTF(reply);
                                    objectOut.flush();
                                    
                                    // Reset the auction timers after any client successfully makes a new bid
                                    timer.cancel();
                                    timer = new Timer();
                                    timer.schedule(new MyTimerTask(auction), 4000, 4000);
                                    CheckTime.timeRemaining = 10;

                                    System.out.println("\nNew bid was placed. Resetting timer...");
                                }

                                // The bid was unsuccessful
                                else
                                {
                                    reply = "Your bid was too low!";
                                    objectOut.writeUTF(reply);
                                    objectOut.flush();
                                }
                            }
                            break;
                        }

                        // Add another item to the auction list
                        case 2:
                        {
                            // Read in the item name and starting bid from client
                            // Use the auction object to create and add this item to the auction
                            String itemName = objectIn.readUTF();
                            float startingBid = objectIn.readFloat();
                            auction.addItem(itemName, startingBid);

                            // When the auction runs out of items, all timers are cancelled
                            // This checks to see if this new item being added is now the only one
                            // in the auction, in which case the timers need to be restarted as the list
                            // is no longer empty
                            if (!CheckTime.isChecking)
                            {
                                checkTimer = new Timer();
                                timer = new Timer();
                                timer.schedule(new MyTimerTask(auction), 4000, 4000);
                                checkTimer.schedule(new CheckTime(), 10, 1000);
                                CheckTime.isChecking = true;
                            }

                            // Demonstrate on the server side at the new item was added
                            System.out.print("\n");
                            System.out.println("All items currently in the auction:");

                            auction.listAuctionItems();
                            break;
                        }

                        // Display the list of auction items
                        case 3:
                        {
                            objectOut.flush();
                            objectOut.reset();

                            objectOut.writeObject(auction);
                            break;
                        }

                        // If the client chooses to terminate
                        case 4:
                        {
                            reply = "Thanks for taking part!\nGoodbye!";
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                            this.stop();
                            this.s.close();
                            auction.removeClient(c);
                            break;
                        }

                        // Ensures the input is between 1-4 inclusive
                        default:
                        {
                            reply = "Value must be between 1-4";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                        }
                    }
                }

                s.close();
            }
            catch (IOException e) {
                e.printStackTrace(); 
            }
        }

        // Closing resources
        try
        { 
            this.objectIn.close(); 
            this.objectOut.close();      
        }
        catch(IOException e) { 
            e.printStackTrace(); 
        }
    }

    // Used as a countdown to the end of the auction
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

    // Thread for when the auction finishes
    // Announces the winner of the current auction
    // and moves the auction onto the next item
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