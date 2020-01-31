package com.application.library;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationClass {

	public static void main(String[] args) {

		// Scanner input = new Scanner(System.in);
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

		BookManager bm = new BookManager();
		String inputName = "";

		while (true) {
			System.out.println("==== Book Manager ====");
			System.out.println("     1) View all Books");
			System.out.println("     2) Add a Book");
			System.out.println("     3) Edit a Book");
			System.out.println("     4) Search for a Book");
			System.out.println("     5) Save and Exit");
			System.out.println("");
			System.out.print("Please choose a number from 1 to 5:");
			try {
				String str = input.readLine();
				int choice = Integer.parseInt(str);

				switch (choice) {
				case 1:
					bm.viewAllBooks();
					break;
				case 2:
					System.out.println("==== Add a Book ====");
					bm.addBook();
					break;
				case 3:
					System.out.println("==== Edit a Book ====");
					bm.editBook();
					break;
				case 4:
					System.out.println("Enter the name of the book to search:");
					try {
						inputName = input.readLine();
						if (!inputName.isEmpty()) {
							bm.searchBook(inputName);
						}
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				case 5:
					System.out.println("All changes saved!");
					input.close();
					System.exit(0);
					break;
				default:
					System.out.println("Invalid choice!Please pick a number from 1 to 5");
					break;

				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

}
