import java.util.*;

// Custom Exception Classes
class LibraryException extends Exception {
    public LibraryException(String message) {
        super(message);
    }
}

// Book Class
class Book {
    private String bookId;
    private String title;
    private String author;
    private boolean isAvailable;

    public Book(String bookId, String title, String author) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isAvailable = true;
    }

    public String getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        return "Book ID: " + bookId + ", Title: " + title + ", Author: " + author + ", Available: " + isAvailable;
    }
}

// Logging Interface
interface Logger {
    void log(String message);
}

// ConsoleLogger Class
class ConsoleLogger implements Logger {
    @Override
    public void log(String message) {
        System.out.println("LOG: " + message);
    }
}

// Library Management System
class Library {
    private Map<String, Book> books;
    private Logger logger;

    public Library(Logger logger) {
        this.books = new HashMap<>();
        this.logger = logger;
    }

    // Add Book
    public void addBook(String bookId, String title, String author) throws LibraryException {
        if (bookId == null || bookId.isEmpty() || title == null || title.isEmpty() || author == null
                || author.isEmpty()) {
            throw new LibraryException("Invalid book details provided.");
        }
        if (books.containsKey(bookId)) {
            throw new LibraryException("Book ID already exists.");
        }

        Book book = new Book(bookId, title, author);
        books.put(bookId, book);
        logger.log("Book added: " + book);
        System.out.println("Book successfully added: " + book);
    }

    // Borrow Book
    public void borrowBook(String bookId) throws LibraryException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new LibraryException("Book ID does not exist.");
        }
        if (!book.isAvailable()) {
            throw new LibraryException("Book is not available for borrowing.");
        }

        book.setAvailable(false);
        logger.log("Book borrowed: " + book);
        System.out.println("You have successfully borrowed the book: " + book);
    }

    // Return Book
    public void returnBook(String bookId) throws LibraryException {
        Book book = books.get(bookId);
        if (book == null) {
            throw new LibraryException("Book ID does not exist.");
        }
        if (book.isAvailable()) {
            throw new LibraryException("Book was not borrowed.");
        }

        book.setAvailable(true);
        logger.log("Book returned: " + book);
        System.out.println("Thank you for returning the book: " + book);
    }

    // View Available Books
    public void viewAvailableBooks() {
        System.out.println("Available Books:");
        for (Book book : books.values()) {
            if (book.isAvailable()) {
                System.out.println(book);
            }
        }
    }

    // Ask and Add Book
    public void askAndAddBook() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter Book ID: ");
            String bookId = scanner.nextLine();
            System.out.print("Enter Book Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Book Author: ");
            String author = scanner.nextLine();

            addBook(bookId, title, author);
            System.out.println("\nThe book has been successfully added. Here are its details:");
            System.out.println(books.get(bookId)); // Display the newly added book
        } catch (LibraryException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

// Main Class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Logger logger = new ConsoleLogger();
        Library library = new Library(logger);

        try (Scanner scanner = new Scanner(System.in)) {
            boolean exit = false;

            while (!exit) {
                System.out.println("\nLibrary Management System:");
                System.out.println("1. Add Book");
                System.out.println("2. View Available Books");
                System.out.println("3. Borrow Book");
                System.out.println("4. Return Book");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        library.askAndAddBook();
                        break;
                    case 2:
                        library.viewAvailableBooks();
                        break;
                    case 3:
                        System.out.print("Enter Book ID to borrow: ");
                        String borrowId = scanner.nextLine();
                        try {
                            library.borrowBook(borrowId);
                        } catch (LibraryException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 4:
                        System.out.print("Enter Book ID to return: ");
                        String returnId = scanner.nextLine();
                        try {
                            library.returnBook(returnId);
                        } catch (LibraryException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                        break;
                    case 5:
                        exit = true;
                        System.out.println("Exiting the Library Management System. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }
}