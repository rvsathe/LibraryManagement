package com.application.library;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.input.ReversedLinesFileReader;

public class BookManager {

	ArrayList<Book> books = new ArrayList<Book>();

	public void viewAllBooks() {

		loadAllBooks();
		allBooksString();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true) {
			System.out.println("Please enter the ID of the book to see the details.Press Enter to exit ");
			String in = null;
			try {
				in = br.readLine();
				if (in.isEmpty() || in.equals("")) {
					break;
				} else {
					int bookId = Integer.parseInt(in);
					for (Book bk : books) {
						if (bk.getId() == bookId) {
							displayBookDetail(bk);
						}
					}

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The method adds a new book to the library. The title,author and description
	 * is kept optional. If the user has not entered any details, then a space is
	 * inserted in the file.
	 */
	public void addBook() {

		String csvFile = ApplicationConstants.LIB_NAME;

		// To get the latest id from the csv file
		File inputFile = new File(csvFile);
		Book bk = new Book();

		try (ReversedLinesFileReader rf = new ReversedLinesFileReader(inputFile, Charset.defaultCharset());
				FileWriter txtWriter = new FileWriter(ApplicationConstants.LIB_NAME, true);) {

			String line = rf.readLine();
			if (line != null) {
				String[] lastLine = line.split(",");
				// Auto-increment the ID in the library
				bk.setId(Integer.parseInt(lastLine[0]) + 1);
			} else {
				bk.setId(1);
			}

			// Inputs from the user
			System.out.println("======Please Enter the following information:");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

			System.out.println("Title:");
			String title = br.readLine();
			if (!title.isEmpty()) {
				bk.setTitle(title);
			} else {
				bk.setTitle(" ");
			}

			System.out.println("Author:");
			String author = br.readLine();
			if (!author.isEmpty()) {
				bk.setAuthor(author);
			} else {
				bk.setAuthor(" ");
			}
			System.out.println("Description:");
			String desc = br.readLine();
			if (!desc.isEmpty()) {
				bk.setDescription(desc);
			} else {
				bk.setDescription(" ");
			}

			// Output or Save to file
			txtWriter.write("\n");
			txtWriter.write(bookToString(bk));

			System.out.println("Book [" + bk.getId() + "] Saved.");
			System.out.println("================================");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * This method is used to edit a file already in the Library. All the fields
	 * except the ID can be changed. If the user does not change the detail of a
	 * particular field, then the field is set to its old value.
	 */
	public void editBook() {

		// To show all the books in the Lib
		loadAllBooks();
		allBooksString();

		File f = new File(ApplicationConstants.LIB_NAME);

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			String newLine = "";
			int index = -1;
			while (true) {
				System.out.println("Enter the book ID of the book you want to edit, to return press <Enter>.");
				String input = in.readLine();
				if (input.isEmpty() || input.equals("")) {
					System.out.println("==================================");
					break;
				} else {
					int bookId = Integer.parseInt(input);
					for (Book bk : books) {
						index++;
						if (bk.getId() == bookId) {
							System.out.println(
									"Input the following information. To leave a field unchanged, hit <Enter>");
							System.out.println("Title[" + bk.getTitle() + "] :");
							String title = in.readLine();
							if (!title.isEmpty()) {
								bk.setTitle(title);
							}
							System.out.println("Author[" + bk.getAuthor() + "] :");
							String author = in.readLine();
							if (!author.isEmpty()) {
								bk.setAuthor(author);
							}
							System.out.println("Description[" + bk.getDescription() + "] :");
							String desc = in.readLine();
							if (!desc.isEmpty()) {
								bk.setDescription(desc);
							}

							// this gives the line to be edited
							newLine = bookToString(bk);

							List<String> lines = new ArrayList<String>();
							lines = Files.readAllLines(Paths.get("Books.txt"),Charset.defaultCharset());
							// add the edited line to the list
							lines.set(index, newLine);

							// overwrite the file
							FileWriter fw = new FileWriter(f);
							Boolean first = true;
							for (String line : lines) {

								// To ensure that there are no extra line separators
								if (!first)
									fw.write(System.lineSeparator());
								else
									first = false;
								fw.write(line);
							}

							fw.close();
							break;
						}
					}

				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// Searched through the library for a book using the title of the book.
	public void searchBook(String inputName) {
		// To ensure that Search can be the first operation
		if (books.isEmpty()) {
			loadAllBooks();
		}
		if (!books.isEmpty()) {
			for (Book bk : books) {
				// To ensure that any string irrespective of the capitalization will be searched
				if (bk.getTitle().toLowerCase().contains(inputName.toLowerCase())) {
					System.out.println(
							"The following books matched your query. Enter the book ID to see more details, or <Enter> to return.");
					System.out.println(bookDetail(bk));
					break;
				}
			}
			while (true) {
				System.out.println("Please enter the ID of the book to see the details.Press Enter to exit ");
				String in = null;
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
				try {
					in = br.readLine();
					if (in.isEmpty() || in.equals("")) {
						System.out.println("==================================");
						break;
					} else {
						int bookId = Integer.parseInt(in);
						for (Book bk : books) {
							if (bk.getId() == bookId) {
								displayBookDetail(bk);
								break;
							}
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("No books in the Library to search");
		}
	}

	/* Util Functions */
	private void displayBookDetail(Book bk) {
		System.out.println("=====Book Details======");
		System.out.println("Book ID:      " + bk.getId());
		System.out.println("Title:        " + bk.getTitle());
		System.out.println("Author:       " + bk.getAuthor());
		System.out.println("Description:  " + bk.getDescription());
		System.out.println("===================================================");
	}

	private String bookDetail(Book bk) {
		return "[" + bk.getId() + "] " + bk.getTitle();
	}

	private void allBooksString() {

		if (!books.isEmpty()) {
			for (Book bk : books) {
				System.out.println("[" + bk.getId() + "] " + bk.getTitle());
			}
		} else {
			System.out.println("No books to show!Please add books to the library");
		}

	}

	// Format a book object to a string to be written to a file
	private String bookToString(Book b) {
		return b.getId() + "," + b.getTitle() + "," + b.getAuthor() + "," + b.getDescription();
	}

	// Get all the books in the file and store in a collection
	private void loadAllBooks() {
		String txtFile = ApplicationConstants.LIB_NAME;
		String line = "";
		String cvsSplitBy = ",";
		// Ensuring books do not reappear in the list
		books.clear();
		try (BufferedReader br = new BufferedReader(new FileReader(txtFile));) {

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] book = line.split(cvsSplitBy);
				Book bk = new Book();

				if (book != null) {
					bk.setId(Integer.parseInt(book[0]));
					if (book[1] != null || !book[1].equals("") || !book[1].isEmpty()) {
						bk.setTitle(book[1]);
					} else {
						bk.setTitle(" ");
					}
					if (book[2] != null || !book[2].equals("") || !book[2].isEmpty()) {
						bk.setAuthor(book[2]);
					} else {
						bk.setAuthor(" ");
					}
					if (book[3] != null || !book[3].equals("") || !book[3].isEmpty()) {
						bk.setDescription(book[3]);
					} else {
						bk.setDescription(" ");
					}

					books.add(bk);
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
