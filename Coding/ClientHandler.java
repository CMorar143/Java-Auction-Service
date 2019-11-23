/**
 * Each client will interact with the server through the ClientHandler class.
 * This is where the auction timers are stored.
 * However, the auction timer needs to be a global timer
 * i.e. every client should see the same timer for each item in the auction.
 * Therefore, the timers are all static so they are accessible by 
 * all instances of the timer classes. 
 *
 * The MyTimerTask class is responsible for tracking the bid period 
 * of each item in the auction. Once this period expires,
 * this item is removed from the auction and the winner (if any) is announced.
 * Then the timer resets for the next item.
 *
 * The CheckTime class is responsible for providing the server with a countdown.
 * Every second, this checks whether or not the main bid period has expired. 
 * 
 * 
 * @author Cian Morar (C16460726) 
 * @date 21st November 2019
 * 
 */

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
                // This ensures that there is only ever one instance of the timers scheduled
                if (!MyTimerTask.hasAuctionStarted())
                {
                    timer.schedule(new MyTimerTask(auction), 10000, 10000);
                    checkTimer.schedule(new CheckTime(auction), 0, 1000);
                    MyTimerTask.setHasStarted(true);
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
                            if (item != null)
                            {
                                reply = "=> What would you like to bid? (Must be greater than the current bid): ";
                                objectOut.writeUTF(reply);
                                objectOut.flush();

                                // Read bid from client
                                float bid = objectIn.readFloat();

                                // Check if this is more than the previous bid
                                boolean bidPlaced = auction.placeBid(bid, c);

                                // If the bid was successful
                                // i.e. If the new bid was higher than the previous bid
                                if (bidPlaced)
                                {
                                    reply = "\n=> Congrats you are now the highest bidder with " + String.valueOf(bid) + "!";
                                    objectOut.writeUTF(reply);
                                    objectOut.flush();
                                    
                                    // Reset the auction timers after any client successfully makes a new bid
                                    timer.cancel();
                                    timer = new Timer();
                                    timer.schedule(new MyTimerTask(auction), 10000, 10000);
                                    CheckTime.timeRemaining = 10;

                                    System.out.println("\n=> New bid was placed. Resetting timer...");                        
                                }

                                // The bid was unsuccessful
                                else
                                {
                                    reply = "=> Your bid was unsuccessful! Please try again";
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
                            String itemName = objectIn.readUTF();
                            float startingBid = objectIn.readFloat();

                            // When the auction runs out of items, all timers are cancelled
                            // This checks to see if this new item being added is now the only one
                            // in the auction, in which case the timers need to be restarted as the list
                            // is no longer empty
                            if (!auction.areThereItems())
                            {
                                checkTimer = new Timer();
                                timer = new Timer();
                                timer.schedule(new MyTimerTask(auction), 10000, 10000);
                                checkTimer.schedule(new CheckTime(auction), 10, 1000);
                                CheckTime.isChecking = true;
                                CheckTime.timeRemaining = 10;
                            }

                            // Use the auction object to create and add this item to the auction
                            auction.addItem(itemName, startingBid);

                            // Demonstrate on the server side at the new item was added
                            System.out.print("\n");
                            System.out.println("=> All items currently in the auction:");

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
                            reply = "=> Thanks for taking part!\n=> Goodbye!";
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
                            reply = "=> Value must be between 1-4";
                            System.out.println(reply);
                            objectOut.writeUTF(reply);
                            objectOut.flush();
                        }
                    }
                }

                try {
                    s.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
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
        final Auction auction;
        static int timeRemaining;
        static boolean isChecking;

        public CheckTime(Auction auction)
        {
            timeRemaining = 10;
            isChecking = true;
            this.auction = auction;
        }

        public void run()
        {
            if (MyTimerTask.isAuctionOver())
            {
                if (auction.areThereItems())
                {
                    System.out.println("\nAuction has finished. Loading next item...");
                    MyTimerTask.setIsFinished(false);
                }

                else
                {
                    System.out.println("No more items!");
                    timer.cancel();
                    isChecking = false;
                    checkTimer.cancel();
                }
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
        private static boolean isFinished = false;
        private static boolean hasStarted = false;

        public MyTimerTask(Auction auction) 
        {
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
        }

        public static boolean isAuctionOver() 
        {
            return isFinished;
        }

        public static boolean hasAuctionStarted()
        {
            return hasStarted;
        }

        public static void setIsFinished(boolean state)
        {
            isFinished = state;
        }

        public static void setHasStarted(boolean state)
        {
            hasStarted = state;
        }

        public void auctionNextItem()
        {
            CheckTime.timeRemaining = 10;
            auction.announceWinner();
        }
    }
}