import java.nio.file.*;
import java.io.*;
import java.util.*;
import java.lang.*;

class Main {
  public static void main(String[] args) {
    // Starts the program off taking the user's name as keyboard input
    // then prints the title and loads the Main Menu
    
    ArrayList<String> winner = getWinner();
    Scanner input = new Scanner(System.in);
    System.out.print("\n\nHey there!  What's your name? ");
    String name = input.nextLine();
    System.out.println("\n");
    printTitle(winner);
    mainMenu(name, winner);
  
  }

  public static void mainMenu(String name, ArrayList<String> winner) {
    // Prints Main Menu and takes player selection input
    
    Scanner input = new Scanner(System.in);
    boolean mainMenuLoop = true;
    
    // Start of while loop for main menu
    while(mainMenuLoop) {
      // Print main menu on display
      
      System.out.println("MAIN MENU");
      System.out.println("---------");
      System.out.println("1.  Play game");
      System.out.println("2.  Add/delete words");
      System.out.println("3.  Quit\n");
      System.out.print("Enter # of selection: ");
      
      // Take player input for main menu selection
      int selection = 0;
      
      try {
        selection = input.nextInt();
      }
      catch (InputMismatchException e) {
        System.out.println("Invalid selection. Please enter a valid number.");
        input.nextLine();
        continue;
      }      
      
      // if Player chooses 1 to play the main game
      if(selection == 1) {
        game(name);
        continue;
      }

      // if Player chooses 2 to add/delete words from the list
      else if(selection == 2) {
        listMenu();
        continue;
      }

      // if Player chooses 3 to quit
      else if(selection == 3) {
        boolean selectionIsValid = false;
        while(!selectionIsValid) {
          // Checks to make sure player wants to quit and takes input as Y/N
          System.out.print("\nAre you sure you want to quit? (Y/N) ");
          Scanner response = new Scanner(System.in);
          String quitResponse = response.nextLine();
          // If player chooses to quit, end the game
          if(quitResponse.charAt(0) == 'Y' || quitResponse.charAt(0) == 'y') {
            System.out.println("\nSorry to see you go " + name + "...  Thanks for playing! :)\n");
            selectionIsValid = true;
            System.exit(0);
          }
          // If player chooses not to quit, return to the Main Menu
          else if(quitResponse.charAt(0) == 'N' || quitResponse.charAt(0) == 'n') {
            System.out.println("\n");
            selectionIsValid = true;
            continue;
          }
          else {
            System.out.println("\nPlease make a valid selection.\n");
          }
        }
      }
        
      // if Player makes an invalid selection - goes back to the top of the loop, reprinting the menu
      // and asking for selection again
      else {
        System.out.println("\n\nPlease make a valid selection.\n\n");        
      }
    }
  }
  
  
  public static void listMenu() {
    // Prints list menu for player to add/delete words from wordlist.txt
    // and takes selection input from player
    
    Scanner input = new Scanner(System.in);
    boolean loop = true;
    while(loop){
      // Print list menu on display
      System.out.println("\n\nLIST MENU");
      System.out.println("---------");
      System.out.println("1.  View words");
      System.out.println("2.  Add word");
      System.out.println("3.  Delete word");
      System.out.println("4.  Return to Main Menu\n");
      System.out.print("Enter # of selection: ");
      
      // Take player input for list menu selection
      int selection = 0;
      try {
        selection = input.nextInt();
        input.nextLine();
      }
      catch(InputMismatchException e) {
        System.out.println("Invalid selection.  Please enter a valid number.");
        input.nextLine();
        continue;
      }
            
      // if player chooses 1 to view word list
      if(selection == 1) {
        System.out.println("\n");
        printWords(); 
      }

      // if player chooses 2 to add word
      else if(selection == 2) {
        System.out.print("\n\nWhat word would you like to add to the list? (type word) ");
        String wordToAdd = input.nextLine();
        addWord(wordToAdd);
        continue;        
      }
        
      // if player chooses 3 to delete word
      else if(selection == 3) {
        // First print the list of words
        System.out.println("\n");
        deleteWord();
        continue;
      }

      // if player chooses 4 to return to main menu
      else if(selection == 4) {
        System.out.println("\n");
        loop = false;
      }

      // if player makes an invalid selection
      else {
      System.out.println("\n\nPlease make a valid selection.");
      }
    }
  }
  
  public static void game(String name) {
    // Main game method
    
    ArrayList<String> wordList = getWordList();

    // Ends game() and returns to Main Menu if word list is empty
    if (wordList.isEmpty()) {
      System.out.println("\n\nThe word list is empty.  Please add words to play.  Returning to Main Menu...\n\n");
      return;
    }

    // Variable declarations and initializations
    Scanner input = new Scanner(System.in);
    int wrongGuesses = 0;
    boolean gameOver = false;
    String gameWord = pickWord();
    int wordLength = gameWord.length();
    ArrayList<String> winner = getWinner();
    ArrayList<Character> incorrectGuesses = new ArrayList<>();
    ArrayList<Character> correctGuesses = new ArrayList<>();
    ArrayList<String> word = new ArrayList<>(wordLength);
    ArrayList<Boolean> wordStatus = new ArrayList<>(wordLength);
    ArrayList<String> playerStatus = new ArrayList<>(6);
    
    // for loops to fill Arraylists wordStatus and playerStatus
    for(int counter = 0; counter <= wordLength - 1; counter++) {
      wordStatus.add(false);
    }
    for(int counter = 0; counter <= 5; counter++) {
      playerStatus.add(" ");
    }
    
    // Start of for loop to load word into ArrayList<String> word letter by letter
    for(int counter = 0; counter <= wordLength - 1; counter++) {
      word.add(Character.toString(gameWord.charAt(counter)));
    }

    System.out.println("\n\nLet's play Hangman, " + name + "! :)\n\nHere's your word...");

    // Start of while loop for main gameplay loop (while !gameOver)
    while(playerStatus.get(5) != "X") {
      boolean isGuessed = false;
      boolean isCorrect = false;
      printBoard(playerStatus, word, wordStatus, correctGuesses, incorrectGuesses);
      System.out.print("Guess a letter: ");
      String guessAsString = input.nextLine();

      // if player inputs nothing for letter, continue loop
      if(guessAsString == ""){
        continue;
      }

      // Convert user's input to uppercase and make char guess = the first character typed
      guessAsString = guessAsString.toUpperCase();
      char guess = guessAsString.charAt(0);
      
      if(incorrectGuesses.isEmpty() == false) {
        for(int counter = 0; counter <= incorrectGuesses.size() - 1; counter++) {
          if(guess == incorrectGuesses.get(counter)) {
            isGuessed = true;
            System.out.println("\n\nYou have already guessed the letter " + guess + ".  Please try again.\n");
          }
        }
      }
      if(correctGuesses.isEmpty() == false) {
        for(int counter = 0; counter <= correctGuesses.size() - 1; counter++) {
          if(guess == correctGuesses.get(counter)) {
            isGuessed = true;
            System.out.println("\n\nYou have already guessed the letter " + guess + ".  Please try again.\n");    
          }
        }
      }
      if(isGuessed) {
        isGuessed = false;
        continue;
      }
      
      // Beginning of for loop to search word for guess
      for(int counter = 0; counter <= wordLength - 1; counter++) {
        if(word.get(counter).equals(Character.toString(guess))) {
          boolean status = wordStatus.set(counter, true);
          isCorrect = true;
        }
      }

      if(!isCorrect) {
        String status = playerStatus.set(wrongGuesses, "X");
        incorrectGuesses.add(guess);
        wrongGuesses++;
      }

      else {
        correctGuesses.add(guess);
        System.out.println("\n\nGood choice " + name + "!\n\n");
      }
      
      // If player wins, congratulate them, have them input their "message," and return to Main Menu
      if(isWinner(word, wordStatus)) {
        System.out.println("Congratulations " + name + "!  You guessed the word!\n");
        System.out.print("What would ye tell the people, oh bravest " + name + "? ");
        String message = input.nextLine();
        setWinner(name, message);
        System.out.println("\nThe world shall know your message, " + name + "!");
        gameOver = true;
        System.out.println("\n");
        // Print title before returning to mainMenu()
        winner = getWinner();
        printTitle(winner);
        break;
      }

      // If player loses (guesses incorrectly 6 times), announce this and make player press ENTER to return to Main Menu
      if(isLoser(playerStatus)) {
        System.out.println("\nI'm sorry, " + name + ", but you didn't survive this one.  Better luck next time!\n");
        System.out.println("Press ENTER to return to the Main Menu.\n");
        // Pause until player presses ENTER
        input.nextLine();
        gameOver = true;
        // Print title before returning to mainMenu()
        printTitle(winner);
        break;
      }
  
    }
  }

  public static ArrayList<String> getWinner() {
    // Retrieves most recent winner and winner's message from winner.txt
    // Returns ArrayList<String> winner
    // Element 0 of winner is the winner's name.  Element 1 is the message.
    
    ArrayList<String> winner = new ArrayList<>();
    try {
      File winnerFile = new File("winner.txt");
      Scanner fileScanner = new Scanner(winnerFile);
      
      // Using Scanner, load ArrayList<String> winner with contents of winner.txt
      // line by line with each line going into a new element in the ArrayList.
      // This should end with element 0 being the name of the winner and element
      // 1 being that winner's message.
      while(fileScanner.hasNextLine()) {
        winner.add(fileScanner.nextLine());
      }
    // Close Scanner 
    fileScanner.close();
    }
    catch(FileNotFoundException e) {
      System.out.println("\nFile not found.\n");
    }

    return winner;
  }

  public static void setWinner(String name, String message) {
    // Sets most recent winner and winner message
    // Writes this information to winner.txt
    // First line of winner.txt is the winner's name.  Second line is the message.

    File winner = new File("winner.txt");
    try {
    // Write winner's name and message to winner.txt
    // Element 0 is name and element 1 is message
    Files.writeString(winner.toPath(), name + "\n" + message, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }
    catch(IOException ex) {
      System.out.println("Error: " + ex);
    }
}

  public static boolean isWinner(ArrayList<String> word, ArrayList<Boolean> wordStatus) {
    // Returns if player won (true/false)

    // winner defaults to true
    boolean winner = true;
    int size = wordStatus.size();
    
    // for loop to iterate over wordStatus to see if any element is false (letter has not been guessed yet)
    for (int counter = 0; counter <= size - 1; counter++) {
      // if any element is false, set winner to false
      if(wordStatus.get(counter) == false) {
        winner = false;
      }
    }

    return winner;
  }

  public static boolean isLoser(ArrayList<String> playerStatus) {
    // Returns true/false if player has lost on this turn by checking for
    // the value 'X' in playerStatus(5), which will only have this value if
    // the player has guessed incorrectly 6 times or more.
    
    if(playerStatus.get(5) == "X") {
      return true;
    }
    else {
      return false;
    }
  }
    
  public static ArrayList<String> getWordList() {
    // Converts list of words in wordlist.txt to ArrayList<String> words
    // Returns ArrayList<String> words
    
    ArrayList<String> words = new ArrayList<>();
    try {
      File wordList = new File("wordlist.txt");
      // Read lines of wordlist.txt and load them into ArrayList<String> words with each line
      // going into a new element
      words = (ArrayList<String>) Files.readAllLines(wordList.toPath());
    }
    catch (FileNotFoundException ex) {
      System.out.println("File not found: " + ex.getMessage());
    }
    catch (IOException ex) {
      System.out.println("IOException: " + ex.getMessage());
    }
    return words;
  }

  public static String pickWord() {
    // Returns a random word from wordlist.txt
    // This is used to pick a random word to use in the game.
    
    File wordList = new File("wordlist.txt");
    List<String> words = getWordList();

    // Create Random object for generating a random number
    Random randomGenerator = new Random();
    int size = words.size();

    // Using random number variable, choose random element from Array<String> words
    // between 0 and the last element
    int randomChoice = randomGenerator.nextInt(size - 1);
    return words.get(randomChoice);
  }
    
  public static void printBoard(ArrayList<String> playerStatus, ArrayList<String> word, ArrayList<Boolean> wordStatus, ArrayList<Character> correctGuesses, ArrayList<Character> incorrectGuesses) {
    // Prints the game board (display of letters guessed and not guessed so far)
    // + the player's status (how many wrong guesses)

    int wordLength = word.size();
    System.out.print("\n\n\n\n\n");

    // Print game word in letters and underscores depending on which letters have been guessed
    // If the letter has been guessed, print the letter
    // If not, print an underscore
    for (int counter = 0; counter < wordLength; counter++) {
      boolean status = wordStatus.get(counter);
      if(status) {
        System.out.print(word.get(counter));
      }
      else {
        System.out.print("_");
      }
      System.out.print("  ");
    }

    // Print player status
    System.out.print("\n\n");
    System.out.print("Incorrect guesses so far: " + incorrectGuesses);
    System.out.print("\n\n");
    System.out.println("[Left Leg]\t" + playerStatus.get(0));
    System.out.println("[Right Leg]\t" + playerStatus.get(1));
    System.out.println("[Left Arm]\t" + playerStatus.get(2));
    System.out.println("[Right Arm]\t" + playerStatus.get(3));
    System.out.println("[Body]\t" + playerStatus.get(4));
    System.out.println("[Head]\t" + playerStatus.get(5) + "\n\n");
  }

  public static void printTitle(ArrayList<String> winner) {
    // Prints the title and message from previous winner before the Main Menu
    
    System.out.println(" _____                           ");
    System.out.println("|  |  |___ ___ ___ _____ ___ ___ ");
    System.out.println("|     | .'|   | . |     | .'|   |");
    System.out.println("|__|__|__,|_|_|_  |_|_|_|__,|_|_|");
    System.out.println("              |___|               by Jason Thomas");
    System.out.println("=================================================");
    System.out.println("\nAfter escaping the gallows, " + winner.get(0) + " was heard screaming:\n");
    System.out.println("\"" + winner.get(1) + "\"\n\n");
  }

  public static void addWord(String word) {
    // Adds word to word list based on player input
    
    File wordList = new File("wordlist.txt");

    // Appends entered word to the end of the list of words in wordlist.txt
    try {
      Files.writeString(wordList.toPath(), word.toUpperCase() + "\n", StandardOpenOption.CREATE, StandardOpenOption.APPEND);
      System.out.println("\n\n" + word + " added successfully!\n");
    }
    catch(IOException ex) {
      System.out.println("Error: " + ex.getMessage() + "\n");
    }
  }

  public static void printWords() {
    // Prints words listed in wordlist.txt to display

    // Declarations and initializations
    File wordList = new File("wordlist.txt");
    Scanner fileScanner = null;
    try {
      fileScanner = new Scanner(wordList);
    }
    catch(FileNotFoundException ex) {
      System.out.println("Word list is empty.  Please add at least one word to play.");
    }

    // Print word list header followed by a numbered list of the words in wordlist.txt
    System.out.println("WORD LIST");
    System.out.println("---------");
    int numOfEntry = 1;
    while(fileScanner != null && fileScanner.hasNextLine()) {
      String fileLine = fileScanner.nextLine();
      System.out.println(numOfEntry + ". " + fileLine);
      numOfEntry++;
    }
    if (fileScanner != null) {
      // Close Scanner
      fileScanner.close();
    }
  }
    
  public static void deleteWord() {
    // Deletes word (as # from list) based off of user input

    // Declarations and initializations
    Scanner input = new Scanner(System.in);
    ArrayList<String> wordList = getWordList();
    int size = wordList.size();

    // Print # of word to be deleted and take player input
    System.out.print("\nEnter number of word to delete: ");
    int delNum = input.nextInt();
    input.nextLine();

    // Remove word from wordList and replace wordlist.txt with the updated wordlist.txt
    if (delNum > 0 && delNum <= size) {
      String wordToDelete = wordList.get(delNum - 1);
      wordList.remove(wordToDelete);
      try {
        Files.write(Paths.get("wordlist.txt"), wordList, StandardOpenOption.TRUNCATE_EXISTING);
        System.out.println("\n" + wordToDelete + " was successfully deleted from the word list.");
      }
      catch (IOException e) {
        System.out.println("\nAn error occurred while deleting the word from the word list.");
      }
    } 
    else {
        System.out.println("\nInvalid selection. Please enter a valid number.");
    }
  }
}