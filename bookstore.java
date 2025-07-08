import java.util.ArrayList;
import java.time.LocalDate;

public class bookstore{
    public static void main(String[] args) {
         BookInventory inventory = new BookInventory();

        
        inventory.addBook(new PaperBook("ISBN001", "The Silent Patient", 2019, 250, "Alex Michaelides", 5, 0.7));
        inventory.addBook(new EBook("ISBN002", "Atomic Habits", 2020, 180, "James Clear", "PDF"));
        inventory.addBook(new ShowcaseBook("ISBN003", "Ancient Manuscripts", 1965, 9999, "Unknown"));

        
        inventory.removeOutdatedBooks(10);

        
        try {
            inventory.buyBook("ISBN001", 2, "fatma@example.com", "Zamalek, Cairo");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        
        try {
            inventory.buyBook("ISBN002", 1, "fatma@example.com", "");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }

        
        try {
            inventory.buyBook("ISBN003", 1, "fatma@example.com", "Anywhere");
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }


        
    }
}


abstract class Book {
    protected String isbn;
    protected String title;
    protected int year;
    protected int price;
    protected String author;

    public Book(String isbn, String title, int year, int price, String author) {
        this.isbn = isbn;
        this.title = title;
        this.year = year;
        this.price = price;
        this.author = author;
    }

    public String getIsbn() {return isbn;}

    public int getYear() {return year;}

    public int getPrice() {return price;}

    public abstract boolean isPurchasable();

    public abstract void deliver(String email, String address, int quantity);

    
}


class PaperBook extends Book {
    private int stock;
    private double weight;

    public PaperBook(String isbn, String title, int year, int price, String author, int stock, double weight) {
        super(isbn, title, year, price, author);
        this.stock = stock;
        this.weight = weight;
    }

    public boolean isPurchasable() {
        return true;
    }

    public int getStock() {return stock;}

    public void reduceStock(int quantity) {
        this.stock -= quantity;
    }

    public void deliver(String email, String address, int quantity) {
        System.out.println("Quantum book store: Shipping " + quantity + " copy(ies) of '" + title + "' to " + address);
        
    }
    
}


class EBook extends Book {
    private String fileType;

    public EBook(String isbn, String title, int year, int price, String author, String fileType) {
        super(isbn, title, year, price, author);
        this.fileType = fileType;
    }

    public boolean isPurchasable() {
        return true;
    }

    public void deliver(String email, String address, int quantity) {
        System.out.println("Quantum book store: Sending EBook '" + title + "' to email: " + email);
        
    }
}


class ShowcaseBook extends Book {
    public ShowcaseBook(String isbn, String title, int year, int price, String author) {
        super(isbn, title, year, price, author);
    }

    public boolean isPurchasable() {
        return false;
    }

    public void deliver(String email, String address, int quantity) {
        System.out.println("Quantum book store: Showcase book '" + title + "' is not for sale.");
    }
}


class BookInventory {
    ArrayList<Book> inventory = new ArrayList<>();

    public void addBook(Book book) {
        inventory.add(book);
        System.out.println("Quantum book store: Book '" + book.title + "' added to inventory.");
    }

    public ArrayList<Book> removeOutdatedBooks(int maxYearsOld) {
    ArrayList<Book> removedBooks = new ArrayList<>();
    int currentYear =LocalDate.now().getYear();

    for (int i = inventory.size() - 1; i >= 0; i--) {
        Book book = inventory.get(i);
        if (currentYear - book.getYear() > maxYearsOld) {
            removedBooks.add(book);
            inventory.remove(i);
            System.out.println("Quantum book store: Removed outdated book '" + book.title + "'");
        }
    }

    return removedBooks;
}

    public int buyBook(String isbn, int quantity, String email, String address) {
    for (int i = 0; i < inventory.size(); i++) {
        Book book = inventory.get(i);

        if (book.getIsbn().equals(isbn)) {
            if (!book.isPurchasable()) {
                throw new RuntimeException("Quantum book store: Book is not for sale.");
            }

            if (book instanceof PaperBook) {
                PaperBook pb = (PaperBook) book;
                if (quantity > pb.getStock()) {
                    throw new RuntimeException("Quantum book store: Not enough stock for paper book.");
                }
                pb.reduceStock(quantity);
            }

            book.deliver(email, address, quantity);
            int totalPrice = book.getPrice() * quantity;
            System.out.println("Quantum book store: Purchase successful. Paid amount = " + totalPrice + " EGP");
            return totalPrice;
        }
    }

    throw new RuntimeException("Quantum book store: Book not found.");
}
}
